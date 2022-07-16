package com.woowacourse.levellog.application;

import static org.assertj.core.api.Assertions.assertThat;

import com.woowacourse.levellog.domain.Feedback;
import com.woowacourse.levellog.domain.FeedbackRepository;
import com.woowacourse.levellog.domain.Levellog;
import com.woowacourse.levellog.domain.LevellogRepository;
import com.woowacourse.levellog.domain.Member;
import com.woowacourse.levellog.domain.MemberRepository;
import com.woowacourse.levellog.domain.Team;
import com.woowacourse.levellog.domain.TeamRepository;
import com.woowacourse.levellog.dto.FeedbackContentDto;
import com.woowacourse.levellog.dto.FeedbackCreateRequest;
import com.woowacourse.levellog.dto.FeedbacksResponse;
import java.time.LocalDateTime;
import java.util.Optional;
import javax.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Transactional
@DisplayName("FeedbackService의")
class FeedbackServiceTest {

    @Autowired
    private FeedbackService feedbackService;

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private LevellogRepository levellogRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Test
    @DisplayName("save 메서드는 피드백을 저장한다.")
    void save() {
        // given
        final FeedbackContentDto feedbackContentDto = new FeedbackContentDto("Spring에 대한 학습을 충분히 하였습니다.",
                "아이 컨텍이 좋습니다.", "윙크하지 마세요.");
        final FeedbackCreateRequest request = new FeedbackCreateRequest("로마", feedbackContentDto);
        final Member eve = memberRepository.save(new Member("이브", 1111, "eve.img"));
        final Member roma = memberRepository.save(new Member("로마", 2222, "roma.img"));
        final Team team = teamRepository.save(new Team("잠실 네오조", "트랙룸", LocalDateTime.now(), "progile.img"));
        final Levellog levellog = levellogRepository.save(new Levellog(eve, team, "이브의 레벨로그"));

        // when
        final Long id = feedbackService.save(levellog.getId(), roma, request);

        // then
        final Optional<Feedback> feedback = feedbackRepository.findById(id);
        assertThat(feedback).isPresent();
    }

    @Test
    @DisplayName("findAll 메서드는 모든 피드백을 조회한다.")
    void findAll() {
        // given
        final Member eve = memberRepository.save(new Member("이브", 1111, "eve.img"));
        final Member roma = memberRepository.save(new Member("로마", 2222, "roma.img"));
        final Member alien = memberRepository.save(new Member("알린", 3333, "alien.img"));
        final Team team = teamRepository.save(new Team("잠실 네오조", "트랙룸", LocalDateTime.now(), "progile.img"));
        final Levellog levellog = levellogRepository.save(new Levellog(eve, team, "이브의 레벨로그"));
        feedbackRepository.save(new Feedback(roma, eve, levellog, "로마 스터디", "로마 말하기", "로마 기타"));
        feedbackRepository.save(new Feedback(alien, eve, levellog, "알린 스터디", "알린 말하기", "알린 기타"));

        // when
        final FeedbacksResponse feedbacksResponse = feedbackService.findAll(levellog.getId());

        // then
        assertThat(feedbacksResponse.getFeedbacks()).hasSize(2);
    }

    @Test
    @DisplayName("delete 메서드는 피드백을 삭제한다.")
    void delete() {
        // given
        final Member eve = memberRepository.save(new Member("이브", 1111, "eve.img"));
        final Member roma = memberRepository.save(new Member("로마", 2222, "roma.img"));
        final Member alien = memberRepository.save(new Member("알린", 3333, "alien.img"));
        final Team team = teamRepository.save(new Team("잠실 네오조", "트랙룸", LocalDateTime.now(), "progile.img"));
        final Levellog levellog = levellogRepository.save(new Levellog(eve, team, "이브의 레벨로그"));

        feedbackRepository.save(new Feedback(roma, eve, levellog, "로마 스터디", "로마 말하기", "로마 기타"));
        final Feedback alienFeedback = feedbackRepository.save(
                new Feedback(alien, eve, levellog, "알린 스터디", "알린 말하기", "알린 기타"));
        final Feedback savedFeedback = feedbackRepository.save(alienFeedback);
        final Long id = savedFeedback.getId();

        // when
        feedbackService.deleteById(id);

        // then
        final Optional<Feedback> deletedFeedback = feedbackRepository.findById(id);
        assertThat(deletedFeedback).isEmpty();
    }
}

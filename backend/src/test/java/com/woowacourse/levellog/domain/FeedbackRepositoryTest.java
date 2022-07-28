package com.woowacourse.levellog.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.woowacourse.levellog.common.config.JpaConfig;
import com.woowacourse.levellog.feedback.domain.Feedback;
import com.woowacourse.levellog.feedback.domain.FeedbackRepository;
import com.woowacourse.levellog.levellog.domain.Levellog;
import com.woowacourse.levellog.levellog.domain.LevellogRepository;
import com.woowacourse.levellog.member.domain.Member;
import com.woowacourse.levellog.member.domain.MemberRepository;
import com.woowacourse.levellog.team.domain.Participant;
import com.woowacourse.levellog.team.domain.ParticipantRepository;
import com.woowacourse.levellog.team.domain.Team;
import com.woowacourse.levellog.team.domain.TeamRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import(JpaConfig.class)
@DisplayName("FeedbackRepository의")
class FeedbackRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    LevellogRepository levellogRepository;

    @Autowired
    TeamRepository teamRepository;

    @Autowired
    ParticipantRepository participantRepository;

    @Autowired
    FeedbackRepository feedbackRepository;

    @Test
    @DisplayName("findAllByLevellog 메서드는 입력된 레벨로그에 등록 모든 피드백을 조회한다.")
    void findAllByLevellog() {
        // given
        final Member fromMember1 = memberRepository.save(new Member("fromMember1", 111, "profile.img"));
        final Member fromMember2 = memberRepository.save(new Member("fromMember2", 222, "profile.img"));
        final Member toMember = memberRepository.save(new Member("toMember", 333, "profile.img"));
        final Team team = teamRepository.save(new Team("잠실 네오조", "작은 강의실", LocalDateTime.now(), "team.img"));
        participantRepository.save(new Participant(team, fromMember1, true));
        participantRepository.save(new Participant(team, fromMember2, true));
        participantRepository.save(new Participant(team, toMember, true));
        final Levellog levellog = levellogRepository.save(new Levellog(toMember, team, "levellog"));

        final Feedback savedFeedback1 = feedbackRepository.save(
                new Feedback(fromMember1, toMember, levellog, "study", "speak", "etc"));
        final Feedback savedFeedback2 = feedbackRepository.save(
                new Feedback(fromMember2, toMember, levellog, "study", "speak", "etc"));

        // when
        final List<Feedback> feedbacks = feedbackRepository.findAllByLevellog(levellog);

        // then
        assertThat(feedbacks).hasSize(2)
                .contains(savedFeedback1, savedFeedback2);
    }

    @Test
    @DisplayName("findAllByToOrderByUpdatedAtDesc 메서드는 입력된 멤버가 받은 피드백을 수정일 기준 내림차순으로 조회한다.")
    void findAllByToOrderByUpdatedAtDesc() {
        // given
        final Member fromMember1 = memberRepository.save(new Member("fromMember1", 111, "profile.img"));
        final Member fromMember2 = memberRepository.save(new Member("fromMember2", 222, "profile.img"));
        final Member toMember = memberRepository.save(new Member("toMember", 333, "profile.img"));
        final Team team = teamRepository.save(new Team("잠실 네오조", "작은 강의실", LocalDateTime.now(), "team.img"));
        participantRepository.save(new Participant(team, fromMember1, true));
        participantRepository.save(new Participant(team, fromMember2, true));
        participantRepository.save(new Participant(team, toMember, true));
        final Levellog levellog = levellogRepository.save(new Levellog(toMember, team, "levellog"));

        final Feedback savedFeedback1 = feedbackRepository.save(
                new Feedback(fromMember1, toMember, levellog, "study", "speak", "etc"));
        final Feedback savedFeedback2 = feedbackRepository.save(
                new Feedback(fromMember2, toMember, levellog, "study", "speak", "etc"));

        savedFeedback2.updateFeedback("update", "update", "update");

        // when
        final List<Feedback> feedbacks = feedbackRepository.findAllByToOrderByUpdatedAtDesc(toMember);

        // then
        assertThat(feedbacks).hasSize(2)
                .containsExactly(savedFeedback2, savedFeedback1);
    }

    @Test
    @DisplayName("existsByLevellogIdAndFromId 메서드는 입력 받은 레벨로그 Id와 작성자 Id로 작성된 피드백의 존재 여부를 반환한다.")
    void existsByLevellogIdAndFromId() {
        // given
        final Member fromMember = memberRepository.save(new Member("fromMember", 111, "profile.img"));
        final Member toMember = memberRepository.save(new Member("toMember", 333, "profile.img"));
        final Team team = teamRepository.save(new Team("잠실 네오조", "작은 강의실", LocalDateTime.now(), "team.img"));
        participantRepository.save(new Participant(team, fromMember, true));
        participantRepository.save(new Participant(team, toMember, true));
        final Levellog levellog = levellogRepository.save(new Levellog(toMember, team, "levellog"));

        feedbackRepository.save(new Feedback(fromMember, toMember, levellog, "study", "speak", "etc"));

        // when
        final boolean isExist = feedbackRepository.existsByLevellogIdAndFromId(levellog.getId(), fromMember.getId());

        // then
        assertThat(isExist).isTrue();
    }
}

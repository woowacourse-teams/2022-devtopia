package com.woowacourse.levellog.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.woowacourse.levellog.interviewquestion.domain.InterviewQuestion;
import com.woowacourse.levellog.levellog.domain.Levellog;
import com.woowacourse.levellog.member.domain.Member;
import com.woowacourse.levellog.team.domain.Participant;
import com.woowacourse.levellog.team.domain.Team;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("InterviewQuestionRepository의")
class InterviewQuestionRepositoryTest extends RepositoryTest {

    @Test
    @DisplayName("findAllByLevellogAndAuthor 메서드는 levellog와 author 멤버가 모두 일치하는 인터뷰 질문 목록를 반환한다.")
    void findAllByLevellogAndAuthor() {
        // given
        final Member eve = memberRepository.save(new Member("eve", 111, "profile.img"));
        final Member toMember = memberRepository.save(new Member("toMember", 333, "profile.img"));
        final Team team = teamRepository.save(
                new Team("잠실 네오조", "작은 강의실", LocalDateTime.now().plusDays(3), "team.img", 1));
        participantRepository.save(new Participant(team, eve, true));
        participantRepository.save(new Participant(team, toMember, false));
        final Levellog levellog = levellogRepository.save(Levellog.of(toMember, team, "levellog"));

        final InterviewQuestion savedInterviewQuestion1 = interviewQuestionRepository.save(
                InterviewQuestion.of(eve, levellog, "스프링을 왜 사용하였나요?"));
        final InterviewQuestion savedInterviewQuestion2 = interviewQuestionRepository.save(
                InterviewQuestion.of(eve, levellog, "AOP란?"));

        // when
        final List<InterviewQuestion> interviewQuestions = interviewQuestionRepository.findAllByLevellogAndAuthor(
                levellog, eve);

        // then
        assertThat(interviewQuestions).hasSize(2)
                .contains(savedInterviewQuestion1, savedInterviewQuestion2);
    }
}

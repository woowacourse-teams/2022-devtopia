package com.woowacourse.levellog.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.woowacourse.levellog.common.exception.InvalidFieldException;
import com.woowacourse.levellog.common.exception.UnauthorizedException;
import com.woowacourse.levellog.interviewquestion.domain.InterviewQuestion;
import com.woowacourse.levellog.interviewquestion.dto.InterviewQuestionDetailDto;
import com.woowacourse.levellog.interviewquestion.dto.InterviewQuestionDto;
import com.woowacourse.levellog.interviewquestion.dto.InterviewQuestionsDto;
import com.woowacourse.levellog.interviewquestion.exception.InterviewQuestionNotFoundException;
import com.woowacourse.levellog.levellog.domain.Levellog;
import com.woowacourse.levellog.levellog.exception.LevellogNotFoundException;
import com.woowacourse.levellog.member.domain.Member;
import com.woowacourse.levellog.member.exception.MemberNotFoundException;
import com.woowacourse.levellog.team.domain.Participant;
import com.woowacourse.levellog.team.domain.Team;
import com.woowacourse.levellog.team.exception.InterviewTimeException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("InterviewQuestionService 클래스의")
class InterviewQuestionServiceTest extends ServiceTest {

    private Member getMember(final String nickname) {
        final Member member = new Member(nickname, ((int) System.nanoTime()), nickname + ".org");
        return memberRepository.save(member);
    }

    private Team getTeam(final Member host, final Member... members) {
        return getTeam(3, host, members);
    }

    private Team getTeam(final long days, final Member host, final Member... members) {
        final Team team = teamRepository.save(
                new Team("잠실 네오조", "트랙룸", LocalDateTime.now().plusDays(days), "jamsil.img", 1));

        participantRepository.save(new Participant(team, host, true));

        final List<Participant> participants = Arrays.stream(members)
                .map(it -> new Participant(team, it, false))
                .collect(Collectors.toList());
        participantRepository.saveAll(participants);

        return team;
    }

    private Levellog getLevellog(final Member author, final Team team) {
        final Levellog levellog = Levellog.of(author, team, "levellog content");
        return levellogRepository.save(levellog);
    }

    private InterviewQuestion getInterviewQuestion(final String content, final Levellog levellog, final Member author) {
        final InterviewQuestionDto request = InterviewQuestionDto.from(content);
        final InterviewQuestion interviewQuestion = request.toInterviewQuestion(author, levellog);
        return interviewQuestionRepository.save(interviewQuestion);
    }

    @Nested
    @DisplayName("save 메서드는")
    class Save {

        @Test
        @DisplayName("인터뷰 질문을 저장한다.")
        void success() {
            // given
            final Member pepper = getMember("페퍼");
            final Member eve = getMember("이브");
            final Team team = getTeam(pepper, eve);
            final Long pepperLevellogId = getLevellog(pepper, team).getId();
            final InterviewQuestionDto request = InterviewQuestionDto.from("스프링이란?");

            // when
            final Long id = interviewQuestionService.save(request, pepperLevellogId, eve.getId());

            // then
            assertThat(interviewQuestionRepository.findById(id))
                    .isPresent();
        }

        @ParameterizedTest
        @ValueSource(strings = {" "})
        @NullAndEmptySource
        @DisplayName("인터뷰 내용이 공백이나 null일 경우 예외를 던진다.")
        void save_contentBlank_exception(final String invalidContent) {
            // given
            final Member pepper = getMember("페퍼");
            final Member eve = getMember("이브");
            final Team team = getTeam(pepper, eve);
            final Long pepperLevellogId = getLevellog(pepper, team).getId();
            final InterviewQuestionDto request = InterviewQuestionDto.from(invalidContent);
            final Long authorId = eve.getId();

            // when & then
            assertThatThrownBy(() -> interviewQuestionService.save(request, pepperLevellogId, authorId))
                    .isInstanceOf(InvalidFieldException.class)
                    .hasMessage("인터뷰 질문은 공백이나 null일 수 없습니다.");
        }

        @Test
        @DisplayName("인터뷰 질문 작성자가 존재하지 않는 경우 예외를 던진다.")
        void save_memberNotFound_exception() {
            // given
            final Member pepper = getMember("페퍼");
            final Team team = getTeam(pepper);
            final Long pepperLevellogId = getLevellog(pepper, team).getId();
            final InterviewQuestionDto request = InterviewQuestionDto.from("스프링이란?");
            final Long invalidMemberId = 1000L;

            // when & then
            assertThatThrownBy(() -> interviewQuestionService.save(request, pepperLevellogId, invalidMemberId))
                    .isInstanceOf(MemberNotFoundException.class)
                    .hasMessageContainingAll("존재하지 않는 멤버", String.valueOf(invalidMemberId));
        }

        @Test
        @DisplayName("레벨로그가 존재하지 않는 경우 예외를 던진다.")
        void save_levellogNotFound_exception() {
            // given
            final Member pepper = getMember("페퍼");
            final Member eve = getMember("이브");
            getTeam(pepper, eve);
            final Long memberId = pepper.getId();
            final Long invalidLevellogId = 1000L;
            final InterviewQuestionDto request = InterviewQuestionDto.from("스프링이란?");

            // when & then
            assertThatThrownBy(() -> interviewQuestionService.save(request, invalidLevellogId, memberId))
                    .isInstanceOf(LevellogNotFoundException.class)
                    .hasMessageContainingAll("존재하지 않는 레벨로그", String.valueOf(invalidLevellogId));
        }

        @Test
        @DisplayName("팀에 속하지 않은 멤버가 인터뷰 질문을 작성할 경우 예외를 던진다.")
        void save_otherTeamMember_exception() {
            // given
            final Member pepper = getMember("페퍼");
            final Member eve = getMember("이브");
            final Long otherTeamMemberId = getMember("알린").getId();
            final Team team = getTeam(pepper, eve);
            final Long pepperLevellogId = getLevellog(pepper, team).getId();
            final InterviewQuestionDto request = InterviewQuestionDto.from("스프링이란?");

            // when & then
            assertThatThrownBy(() -> interviewQuestionService.save(request, pepperLevellogId, otherTeamMemberId))
                    .isInstanceOf(UnauthorizedException.class)
                    .hasMessageContainingAll("같은 팀에 속한 멤버만 인터뷰 질문을 작성할 수 있습니다.", String.valueOf(team.getId()),
                            String.valueOf(pepperLevellogId), String.valueOf(otherTeamMemberId));
        }

        @Test
        @DisplayName("인터뷰가 종료된 후면 예외를 던진다.")
        void save_isClosed_exception() {
            // given
            final Member pepper = getMember("페퍼");
            final Member eve = getMember("이브");
            final Team team = getTeam(pepper, eve);
            final Long pepperLevellogId = getLevellog(pepper, team).getId();
            final InterviewQuestionDto request = InterviewQuestionDto.from("스프링이란?");

            team.close(timeStandard.now());

            // when & then
            assertThatThrownBy(() -> interviewQuestionService.save(request, pepperLevellogId, eve.getId()))
                    .isInstanceOf(InterviewTimeException.class)
                    .hasMessageContainingAll("이미 종료된 인터뷰입니다.", String.valueOf(team.getId()));
        }

        @Test
        @DisplayName("인터뷰가 시작 전이면 예외를 던진다.")
        void save_isReady_exception() {
            // given
            final Member pepper = getMember("페퍼");
            final Member eve = getMember("이브");
            final Team team = getTeam(10, pepper, eve);
            final Long pepperLevellogId = getLevellog(pepper, team).getId();
            final InterviewQuestionDto request = InterviewQuestionDto.from("스프링이란?");

            // when & then
            assertThatThrownBy(() -> interviewQuestionService.save(request, pepperLevellogId, eve.getId()))
                    .isInstanceOf(InterviewTimeException.class)
                    .hasMessageContainingAll("인터뷰 시작 전입니다.", String.valueOf(team.getId()));
        }
    }

    @Nested
    @DisplayName("findAll 메서드는")
    class FindAll {

        @Test
        @DisplayName("레벨로그에 대해 특정 멤버가 작성한 인터뷰 질문 목록을 조회한다.")
        void success() {
            // given
            final Member pepper = getMember("페퍼");
            final Member eve = getMember("이브");
            final Team team = getTeam(pepper, eve);
            final Levellog pepperLevellog = getLevellog(pepper, team);
            getInterviewQuestion("스프링이란?", pepperLevellog, eve);
            getInterviewQuestion("스프링 빈이란?", pepperLevellog, eve);

            // when
            final InterviewQuestionsDto response = interviewQuestionService.findAllByLevellogAndAuthor(
                    pepperLevellog.getId(),
                    eve.getId());

            // then
            final List<String> actualInterviewQuestionContents = response.getInterviewQuestions()
                    .stream()
                    .map(InterviewQuestionDetailDto::getInterviewQuestion)
                    .collect(Collectors.toList());

            assertAll(
                    () -> assertThat(response.getInterviewQuestions()).hasSize(2),
                    () -> assertThat(actualInterviewQuestionContents).contains("스프링이란?", "스프링 빈이란?")
            );
        }

        @Test
        @DisplayName("레벨로그가 존재하지 않는 경우 예외를 던진다.")
        void findAll_levellogNotFound_exception() {
            // given
            final Member pepper = getMember("페퍼");
            final Member eve = getMember("이브");
            getTeam(pepper, eve);
            final Long memberId = eve.getId();
            final Long invalidLevellogId = 1000L;

            // when & then
            assertThatThrownBy(() -> interviewQuestionService.findAllByLevellogAndAuthor(invalidLevellogId, memberId))
                    .isInstanceOf(LevellogNotFoundException.class)
                    .hasMessageContainingAll("존재하지 않는 레벨로그", String.valueOf(invalidLevellogId));
        }

        @Test
        @DisplayName("인터뷰 질문 작성자가 존재하지 않는 경우 예외를 던진다.")
        void findAll_memberNotFound_exception() {
            // given
            final Member pepper = getMember("페퍼");
            final Team team = getTeam(pepper);
            final Long pepperLevellogId = getLevellog(pepper, team).getId();
            final Long invalidMemberId = 1000L;

            // when & then
            assertThatThrownBy(
                    () -> interviewQuestionService.findAllByLevellogAndAuthor(pepperLevellogId, invalidMemberId))
                    .isInstanceOf(MemberNotFoundException.class)
                    .hasMessageContainingAll("존재하지 않는 멤버", String.valueOf(invalidMemberId));
        }
    }

    @Nested
    @DisplayName("update 메서드는")
    class Update {

        @Test
        @DisplayName("인터뷰 질문을 수정한다.")
        void success() {
            // given
            final Member pepper = getMember("페퍼");
            final Member eve = getMember("이브");
            final Team team = getTeam(pepper, eve);
            final Levellog pepperLevellog = getLevellog(pepper, team);
            final Long interviewQuestionId = getInterviewQuestion("스프링이란?", pepperLevellog, eve).getId();
            final InterviewQuestionDto request = InterviewQuestionDto.from("업데이트된 질문 내용");

            // when
            interviewQuestionService.update(request, interviewQuestionId, eve.getId());

            // then
            final List<String> actualInterviewQuestions = interviewQuestionRepository
                    .findAllByLevellogAndAuthor(pepperLevellog, eve)
                    .stream()
                    .map(InterviewQuestion::getContent)
                    .collect(Collectors.toList());
            assertThat(actualInterviewQuestions).contains("업데이트된 질문 내용");
        }

        @Test
        @DisplayName("인터뷰 질문이 존재하지 않는 경우 예외를 던진다.")
        void update_interviewQuestionNotFound_exception() {
            // given
            final Member eve = getMember("이브");
            final InterviewQuestionDto request = InterviewQuestionDto.from("업데이트된 질문 내용");
            final Long invalidInterviewQuestionId = 1000L;
            final Long authorId = eve.getId();

            // when & then
            assertThatThrownBy(() -> interviewQuestionService.update(request, invalidInterviewQuestionId, authorId))
                    .isInstanceOf(InterviewQuestionNotFoundException.class)
                    .hasMessageContainingAll("존재하지 않는 인터뷰 질문", String.valueOf(invalidInterviewQuestionId));
        }

        @Test
        @DisplayName("인터뷰 질문 작성자가 아닌 경우 권한 없음 예외를 던진다.")
        void update_unauthorized_exception() {
            // given
            final Member pepper = getMember("페퍼");
            final Member eve = getMember("이브");
            final Long otherMemberId = getMember("릭").getId();
            final Team team = getTeam(pepper, eve);
            final Levellog pepperLevellog = getLevellog(pepper, team);
            final Long interviewQuestionId = getInterviewQuestion("스프링이란?", pepperLevellog, eve).getId();
            final InterviewQuestionDto request = InterviewQuestionDto.from("업데이트된 질문 내용");

            // when & then
            assertThatThrownBy(() -> interviewQuestionService.update(request, interviewQuestionId, otherMemberId))
                    .isInstanceOf(UnauthorizedException.class)
                    .hasMessageContainingAll("인터뷰 질문을 수정할 수 있는 권한이 없습니다.", String.valueOf(otherMemberId),
                            String.valueOf(eve.getId()), String.valueOf(pepperLevellog.getId()));
        }

        @Test
        @DisplayName("인터뷰가 종료된 후면 예외를 던진다.")
        void update_isClosed_exception() {
            // given
            final Member pepper = getMember("페퍼");
            final Member eve = getMember("이브");
            final Team team = getTeam(pepper, eve);
            final Levellog pepperLevellog = getLevellog(pepper, team);
            final Long interviewQuestionId = getInterviewQuestion("스프링이란?", pepperLevellog, eve).getId();
            final InterviewQuestionDto request = InterviewQuestionDto.from("업데이트된 질문 내용");

            team.close(timeStandard.now());

            // when & then
            assertThatThrownBy(() -> interviewQuestionService.update(request, interviewQuestionId, eve.getId()))
                    .isInstanceOf(InterviewTimeException.class)
                    .hasMessageContainingAll("이미 종료된 인터뷰입니다.", String.valueOf(team.getId()));
        }

        @Test
        @DisplayName("인터뷰가 시작 전이면 예외를 던진다.")
        void update_isReady_exception() {
            // given
            final Member pepper = getMember("페퍼");
            final Member eve = getMember("이브");
            final Team team = getTeam(10, pepper, eve);
            final Levellog pepperLevellog = getLevellog(pepper, team);
            final Long interviewQuestionId = getInterviewQuestion("스프링이란?", pepperLevellog, eve).getId();
            final InterviewQuestionDto request = InterviewQuestionDto.from("업데이트된 질문 내용");

            // when & then
            assertThatThrownBy(() -> interviewQuestionService.update(request, interviewQuestionId, eve.getId()))
                    .isInstanceOf(InterviewTimeException.class)
                    .hasMessageContainingAll("인터뷰 시작 전입니다.", String.valueOf(team.getId()));
        }
    }

    @Nested
    @DisplayName("deleteById 메서드는")
    class DeleteById {

        @Test
        @DisplayName("인터뷰 질문을 삭제한다.")
        void success() {
            // given
            final Member pepper = getMember("페퍼");
            final Member eve = getMember("이브");
            final Team team = getTeam(pepper, eve);
            final Levellog pepperLevellog = getLevellog(pepper, team);
            final Long interviewQuestionId = getInterviewQuestion("스프링이란?", pepperLevellog, eve).getId();

            // when
            interviewQuestionService.deleteById(interviewQuestionId, eve.getId());

            // then
            assertThat(interviewQuestionRepository.findById(interviewQuestionId))
                    .isEmpty();
        }

        @Test
        @DisplayName("인터뷰 질문이 존재하지 않는 경우 예외를 던진다.")
        void deleteById_interviewQuestionNotFound_exception() {
            // given
            final Member eve = getMember("이브");
            final Long invalidInterviewQuestionId = 1000L;
            final Long authorId = eve.getId();

            // when & then
            assertThatThrownBy(() -> interviewQuestionService.deleteById(invalidInterviewQuestionId, authorId))
                    .isInstanceOf(InterviewQuestionNotFoundException.class)
                    .hasMessageContainingAll("존재하지 않는 인터뷰 질문", String.valueOf(invalidInterviewQuestionId));
        }

        @Test
        @DisplayName("인터뷰 질문 작성자가 아닌 경우 권한 없음 예외를 던진다.")
        void deleteById_unauthorized_exception() {
            // given
            final Member pepper = getMember("페퍼");
            final Member eve = getMember("이브");
            final Long otherMemberId = getMember("릭").getId();
            final Team team = getTeam(pepper, eve);
            final Levellog pepperLevellog = getLevellog(pepper, team);
            final Long interviewQuestionId = getInterviewQuestion("스프링이란?", pepperLevellog, eve).getId();

            // when & then
            assertThatThrownBy(() -> interviewQuestionService.deleteById(interviewQuestionId, otherMemberId))
                    .isInstanceOf(UnauthorizedException.class)
                    .hasMessageContainingAll("인터뷰 질문을 삭제할 수 있는 권한이 없습니다.", String.valueOf(otherMemberId),
                            String.valueOf(eve.getId()), String.valueOf(pepperLevellog.getId()));
        }

        @Test
        @DisplayName("인터뷰가 종료된 후면 예외를 던진다.")
        void deleteById_isClosed_exception() {
            // given
            final Member pepper = getMember("페퍼");
            final Member eve = getMember("이브");
            final Team team = getTeam(pepper, eve);
            final Levellog pepperLevellog = getLevellog(pepper, team);
            final Long interviewQuestionId = getInterviewQuestion("스프링이란?", pepperLevellog, eve).getId();
            final InterviewQuestionDto request = InterviewQuestionDto.from("업데이트된 질문 내용");

            team.close(timeStandard.now());

            // when & then
            assertThatThrownBy(() -> interviewQuestionService.deleteById(interviewQuestionId, eve.getId()))
                    .isInstanceOf(InterviewTimeException.class)
                    .hasMessageContainingAll("이미 종료된 인터뷰입니다.", String.valueOf(team.getId()));
        }

        @Test
        @DisplayName("인터뷰가 시작 전이면 예외를 던진다.")
        void deleteById_isReady_exception() {
            // given
            final Member pepper = getMember("페퍼");
            final Member eve = getMember("이브");
            final Team team = getTeam(10, pepper, eve);
            final Levellog pepperLevellog = getLevellog(pepper, team);
            final Long interviewQuestionId = getInterviewQuestion("스프링이란?", pepperLevellog, eve).getId();
            final InterviewQuestionDto request = InterviewQuestionDto.from("업데이트된 질문 내용");

            // when & then
            assertThatThrownBy(() -> interviewQuestionService.deleteById(interviewQuestionId, eve.getId()))
                    .isInstanceOf(InterviewTimeException.class)
                    .hasMessageContainingAll("인터뷰 시작 전입니다.", String.valueOf(team.getId()));
        }
    }
}

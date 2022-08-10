package com.woowacourse.levellog.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.woowacourse.levellog.common.exception.InvalidFieldException;
import com.woowacourse.levellog.team.domain.Team;
import com.woowacourse.levellog.team.domain.TeamStatus;
import com.woowacourse.levellog.team.exception.InterviewTimeException;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("Team의")
class TeamTest {

    @Nested
    @DisplayName("생성자 메서드는")
    class Constructor {

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {" "})
        @DisplayName("팀 이름이 null 또는 공백이 들어오면 예외를 던진다.")
        void titleNullOrBlank_Exception(final String title) {
            // given
            final String place = "선릉 트랙룸";
            final LocalDateTime startAt = LocalDateTime.now().plusDays(3);
            final String profileUrl = "profile.img";

            // when & then
            assertThatThrownBy(() -> new Team(title, place, startAt, profileUrl, 1))
                    .isInstanceOf(InvalidFieldException.class)
                    .hasMessageContaining("팀 이름이 null 또는 공백입니다.");
        }

        @Test
        @DisplayName("팀 이름이 255자를 초과할 경우 예외를 던진다.")
        void titleInvalidLength_Exception() {
            // given
            final String title = "a".repeat(256);
            final String place = "선릉 트랙룸";
            final LocalDateTime startAt = LocalDateTime.now().plusDays(3);
            final String profileUrl = "profile.img";

            // when & then
            assertThatThrownBy(() -> new Team(title, place, startAt, profileUrl, 1))
                    .isInstanceOf(InvalidFieldException.class)
                    .hasMessageContaining("잘못된 팀 이름을 입력했습니다.");
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {" "})
        @DisplayName("팀 장소가 null 또는 공백이 들어오면 예외를 던진다.")
        void placeNullOrBlank_Exception(final String place) {
            // given
            final String title = "네오와 함께하는 레벨 인터뷰";
            final LocalDateTime startAt = LocalDateTime.now().plusDays(3);
            final String profileUrl = "profile.img";

            // when & then
            assertThatThrownBy(() -> new Team(title, place, startAt, profileUrl, 1))
                    .isInstanceOf(InvalidFieldException.class)
                    .hasMessageContaining("장소가 null 또는 공백입니다.");
        }

        @Test
        @DisplayName("팀 장소가 255자를 초과할 경우 예외를 던진다.")
        void placeInvalidLength_Exception() {
            // given
            final String title = "네오와 함께하는 레벨 인터뷰";
            final String place = "a".repeat(256);
            final LocalDateTime startAt = LocalDateTime.now().plusDays(3);
            final String profileUrl = "profile.img";

            // when & then
            assertThatThrownBy(() -> new Team(title, place, startAt, profileUrl, 1))
                    .isInstanceOf(InvalidFieldException.class)
                    .hasMessageContaining("잘못된 장소를 입력했습니다.");
        }

        @ParameterizedTest
        @NullSource
        @DisplayName("인터뷰 시작 시간이 null이 들어오면 예외를 던진다.")
        void startAtNull_Exception(final LocalDateTime startAt) {
            // given
            final String title = "네오와 함께하는 레벨 인터뷰";
            final String place = "선릉 트랙룸";
            final String profileUrl = "profile.img";

            // when & then
            assertThatThrownBy(() -> new Team(title, place, startAt, profileUrl, 1))
                    .isInstanceOf(InterviewTimeException.class)
                    .hasMessageContaining("시작 시간이 없습니다.");
        }

        @Test
        @DisplayName("인터뷰 시작 시간이 과거인 경우 예외를 던진다.")
        void startAtInvalidDateTime_Exception() {
            // given
            final String title = "네오와 함께하는 레벨 인터뷰";
            final String place = "선릉 트랙룸";
            final LocalDateTime startAt = LocalDateTime.now().minusDays(3);
            final String profileUrl = "profile.img";

            // when & then
            assertThatThrownBy(() -> new Team(title, place, startAt, profileUrl, 1))
                    .isInstanceOf(InterviewTimeException.class)
                    .hasMessageContaining("인터뷰 시작 시간은 현재 시간 이후여야 합니다.");
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {" "})
        @DisplayName("팀 프로필 사진 url이 null 또는 공백이 들어오면 예외를 던진다.")
        void profileUrlNullOrBlank_Exception(final String profileUrl) {
            // given
            final String title = "네오와 함께하는 레벨 인터뷰";
            final String place = "선릉 트랙룸";
            final LocalDateTime startAt = LocalDateTime.now().plusDays(3);

            // when & then
            assertThatThrownBy(() -> new Team(title, place, startAt, profileUrl, 1))
                    .isInstanceOf(InvalidFieldException.class)
                    .hasMessageContaining("팀 프로필 사진이 null 또는 공백입니다.");
        }

        @Test
        @DisplayName("팀 프로필 사진 url이 2048자를 초과할 경우 예외를 던진다.")
        void profileUrlInvalidLength_Exception() {
            // given
            final String title = "네오와 함께하는 레벨 인터뷰";
            final String place = "선릉 트랙룸";
            final LocalDateTime startAt = LocalDateTime.now().plusDays(3);
            final String profileUrl = "a".repeat(2049);

            // when & then
            assertThatThrownBy(() -> new Team(title, place, startAt, profileUrl, 1))
                    .isInstanceOf(InvalidFieldException.class)
                    .hasMessageContaining("잘못된 팀 프로필 사진을 입력했습니다.");
        }

        @Test
        @DisplayName("인터뷰어 수가 1명 미만이면 예외를 던진다.")
        void minInterviewerNumber_exceptionThrown() {
            // given
            final String title = "네오와 함께하는 레벨 인터뷰";
            final String place = "선릉 트랙룸";
            final LocalDateTime startAt = LocalDateTime.now().plusDays(3);
            final String profileUrl = "profile.org";

            // when & then
            assertThatThrownBy(() -> new Team(title, place, startAt, profileUrl, 0))
                    .isInstanceOf(InvalidFieldException.class)
                    .hasMessageContaining("팀 생성시 인터뷰어 수는 1명 이상이어야 합니다");
        }
    }

    @Nested
    @DisplayName("update 메서드는")
    class Update {

        @Test
        @DisplayName("팀 이름, 장소, 시작 시간을 수정한다.")
        void update() {
            // given
            final LocalDateTime presentTime = LocalDateTime.now();
            final Team team = new Team("네오와 함께하는 레벨 인터뷰", "선릉 트랙룸", presentTime.plusDays(3), "profile.img", 1);
            final Team updatedTeam = new Team("브라운과 카페 투어", "잠실 어드레스룸", presentTime.plusDays(10), "profile.img", 2);

            // when
            team.update(updatedTeam, presentTime);

            // then
            assertAll(
                    () -> assertThat(team.getTitle()).isEqualTo(updatedTeam.getTitle()),
                    () -> assertThat(team.getPlace()).isEqualTo(updatedTeam.getPlace()),
                    () -> assertThat(team.getStartAt()).isEqualTo(updatedTeam.getStartAt()),
                    () -> assertThat(team.getInterviewerNumber()).isEqualTo(updatedTeam.getInterviewerNumber())
            );
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {" "})
        @DisplayName("팀 이름이 null 또는 공백이 들어오면 예외를 던진다.")
        void titleNullOrBlank_Exception(final String updateTitle) {
            // given
            final LocalDateTime presentTime = LocalDateTime.now();
            final Team team = new Team("네오와 함께하는 레벨 인터뷰", "선릉 트랙룸", presentTime.plusDays(3), "profile.img", 1);

            // when & then
            assertThatThrownBy(
                    () -> team.update(new Team(updateTitle, "잠실 어드레스룸", presentTime.plusDays(10), "profile.img", 2),
                            presentTime))
                    .isInstanceOf(InvalidFieldException.class)
                    .hasMessageContaining("팀 이름이 null 또는 공백입니다.");
        }

        @Test
        @DisplayName("팀 이름이 255자를 초과할 경우 예외를 던진다.")
        void titleInvalidLength_Exception() {
            // given
            final LocalDateTime presentTime = LocalDateTime.now();
            final String updateTitle = "a".repeat(256);
            final Team team = new Team("네오와 함께하는 레벨 인터뷰", "선릉 트랙룸", presentTime.plusDays(3), "profile.img", 1);

            // when & then
            assertThatThrownBy(
                    () -> team.update(new Team(updateTitle, "잠실 어드레스룸", presentTime.plusDays(10), "profile.img", 2),
                            presentTime))
                    .isInstanceOf(InvalidFieldException.class)
                    .hasMessageContaining("잘못된 팀 이름을 입력했습니다.");
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {" "})
        @DisplayName("팀 장소가 null 또는 공백이 들어오면 예외를 던진다.")
        void placeNullOrBlank_Exception(final String updatePlace) {
            // given
            final LocalDateTime presentTime = LocalDateTime.now();
            final Team team = new Team("네오와 함께하는 레벨 인터뷰", "선릉 트랙룸", presentTime.plusDays(3), "profile.img", 1);

            // when & then
            assertThatThrownBy(
                    () -> team.update(new Team("브라운과 카페 투어", updatePlace, presentTime.plusDays(10), "profile.img", 2),
                            presentTime))
                    .isInstanceOf(InvalidFieldException.class)
                    .hasMessageContaining("장소가 null 또는 공백입니다.");
        }

        @Test
        @DisplayName("팀 장소가 255자를 초과할 경우 예외를 던진다.")
        void placeInvalidLength_Exception() {
            // given
            final LocalDateTime presentTime = LocalDateTime.now();
            final String updatePlace = "a".repeat(256);
            final Team team = new Team("네오와 함께하는 레벨 인터뷰", "선릉 트랙룸", presentTime.plusDays(3), "profile.img", 1);

            // when & then
            assertThatThrownBy(
                    () -> team.update(new Team("브라운과 카페 투어", updatePlace, presentTime.plusDays(10), "profile.img", 2),
                            presentTime))
                    .isInstanceOf(InvalidFieldException.class)
                    .hasMessageContaining("잘못된 장소를 입력했습니다.");
        }

        @ParameterizedTest
        @NullSource
        @DisplayName("인터뷰 시작 시간이 null이 들어오면 예외를 던진다.")
        void startAtNull_Exception(final LocalDateTime updateStartAt) {
            // given
            final LocalDateTime presentTime = LocalDateTime.now();
            final Team team = new Team("네오와 함께하는 레벨 인터뷰", "선릉 트랙룸", presentTime.plusDays(3), "profile.img", 1);

            // when & then
            assertThatThrownBy(
                    () -> team.update(new Team("브라운과 카페 투어", "잠실 어드레스룸", updateStartAt, "profile.img", 2), presentTime))
                    .isInstanceOf(InterviewTimeException.class)
                    .hasMessageContaining("시작 시간이 없습니다.");
        }

        @Test
        @DisplayName("인터뷰 시작 시간이 과거인 경우 예외를 던진다.")
        void startAtInvalidDateTime_Exception() {
            // given
            final LocalDateTime presentTime = LocalDateTime.now();
            final Team team = new Team("네오와 함께하는 레벨 인터뷰", "선릉 트랙룸", presentTime.plusDays(3), "profile.img", 1);

            // when & then
            assertThatThrownBy(
                    () -> team.update(new Team("브라운과 카페 투어", "잠실 어드레스룸", presentTime.minusDays(10), "profile.img", 2),
                            presentTime))
                    .isInstanceOf(InterviewTimeException.class)
                    .hasMessageContaining("인터뷰 시작 시간은 현재 시간 이후여야 합니다.");
        }

        @Test
        @DisplayName("인터뷰 시작 이후인 경우 예외를 던진다.")
        void updateAfterStartAt_Exception() {
            // given
            final LocalDateTime now = LocalDateTime.now();
            final LocalDateTime presentTime = now.plusDays(2);
            final Team team = new Team("네오와 함께하는 레벨 인터뷰", "선릉 트랙룸", now.plusDays(1), "profile.img", 1);
            final Team updatedTeam = new Team("브라운과 카페 투어", "잠실 어드레스룸", now.plusDays(3), "profile.img", 2);

            // when & then
            assertThatThrownBy(() -> team.update(updatedTeam, presentTime))
                    .isInstanceOf(InterviewTimeException.class)
                    .hasMessageContaining("인터뷰가 시작된 이후에는 수정할 수 없습니다.");
        }
    }

    @Nested
    @DisplayName("validParticipantNumber 메서드는")
    class validParticipantNumber {

        @ParameterizedTest
        @CsvSource(value = {"1,1", "2,1", "2,2"})
        @DisplayName("isValidParticipantNumber 메서드는 인터뷰어 수를 참고해서 참가자 수가 유효한지 계산한다.")
        void throwException(final int interviewerNumber, final int participantNumber) {
            // given
            final Team team = new Team("레벨로그팀", "우리집", LocalDateTime.now().plusDays(3), "profile.url",
                    interviewerNumber);

            // when & then
            assertThatThrownBy(() -> team.validParticipantNumber(participantNumber))
                    .isInstanceOf(InvalidFieldException.class);
        }

        @ParameterizedTest
        @CsvSource(value = {"1,2", "2,3"})
        @DisplayName("isValidParticipantNumber 메서드는 인터뷰어 수를 참고해서 참가자 수가 유효한지 계산한다.")
        void notThrownException(final int interviewerNumber, final int participantNumber) {
            // given
            final Team team = new Team("레벨로그팀", "우리집", LocalDateTime.now().plusDays(3), "profile.url",
                    interviewerNumber);

            // when & then
            assertThatCode(() -> team.validParticipantNumber(participantNumber))
                    .doesNotThrowAnyException();
        }
    }

    @Nested
    @DisplayName("close 메소드는")
    class Close {

        @Test
        @DisplayName("팀 인터뷰를 종료 상태로 바꾼다.")
        void close_success() {
            // given
            final LocalDateTime startAt = LocalDateTime.now().plusDays(3);
            final Team team = new Team("네오와 함께하는 레벨 인터뷰", "선릉 트랙룸", startAt, "profileUrl", 2);

            // when
            team.close(startAt.plusDays(1));

            // then
            assertThat(team.isClosed()).isTrue();
        }

        @Test
        @DisplayName("이미 종료된 인터뷰를 종료하려는 경우 예외가 발생한다.")
        void close_alreadyClosed_exceptionThrown() {
            // given
            final LocalDateTime startAt = LocalDateTime.now().plusDays(3);
            final Team team = new Team("네오와 함께하는 레벨 인터뷰", "선릉 트랙룸", startAt, "profileUrl", 2);

            team.close(startAt.plusDays(1));

            // when & then
            assertThatThrownBy(() -> team.close(startAt.plusDays(1)))
                    .isInstanceOf(InterviewTimeException.class)
                    .hasMessageContaining("이미 종료된 인터뷰");
        }

        @Test
        @DisplayName("인터뷰 시작 시간 전 종료를 요청할 경우 예외가 발생한다.")
        void close_beforeStart_exceptionThrown() {
            // given
            final LocalDateTime startAt = LocalDateTime.now().plusDays(3);
            final Team team = new Team("네오와 함께하는 레벨 인터뷰", "선릉 트랙룸", startAt, "profileUrl", 2);

            // when & then
            assertThatThrownBy(() -> team.close(startAt.minusDays(1)))
                    .isInstanceOf(InterviewTimeException.class)
                    .hasMessageContaining("인터뷰가 시작되기 전에 종료할 수 없습니다.");
        }
    }

    @Nested
    @DisplayName("delete 메서드는")
    class Delete {

        @Test
        @DisplayName("팀을 deleted 상태로 만든다.")
        void delete_success() {
            // given
            final LocalDateTime startAt = LocalDateTime.now().plusDays(3);
            final Team team = new Team("네오와 함께하는 레벨 인터뷰", "선릉 트랙룸", startAt, "profileUrl", 2);

            // when
            team.delete(startAt.minusDays(1));

            // then
            assertThat(team.isDeleted()).isTrue();
        }

        @Test
        @DisplayName("이미 삭제 상태인 팀을 삭제하려는 경우 예외가 발생한다.")
        void delete_alreadyDeleted_exceptionThrown() {
            // given
            final LocalDateTime startAt = LocalDateTime.now().plusDays(3);
            final Team team = new Team("네오와 함께하는 레벨 인터뷰", "선릉 트랙룸", startAt, "profileUrl", 2);

            team.delete(startAt.minusDays(2));

            // when & then
            final LocalDateTime deletedTime = startAt.minusDays(1);
            assertThatThrownBy(() -> team.delete(deletedTime))
                    .isInstanceOf(InterviewTimeException.class)
                    .hasMessageContaining("이미 삭제된 인터뷰");
        }

        @Test
        @DisplayName("인터뷰 시작 이후 삭제를 요청할 경우 예외가 발생한다.")
        void delete_afterStart_exceptionThrown() {
            // given
            final LocalDateTime startAt = LocalDateTime.now().plusDays(3);
            final Team team = new Team("네오와 함께하는 레벨 인터뷰", "선릉 트랙룸", startAt, "profileUrl", 2);

            // when & then
            final LocalDateTime deletedTime = startAt.plusDays(1);
            assertThatThrownBy(() -> team.delete(deletedTime))
                    .isInstanceOf(InterviewTimeException.class)
                    .hasMessageContaining("인터뷰가 시작된 이후에는 삭제할 수 없습니다.");
        }
    }

    @Nested
    @DisplayName("status 메서드는")
    class Status {

        @Test
        @DisplayName("현재 시간이 인터뷰 시작 시간보다 이전인 경우 READY를 반환한다.")
        void readyStatus_success() {
            // given
            final LocalDateTime presentTime = LocalDateTime.now();
            final Team team = new Team("네오와 함께하는 레벨 인터뷰", "선릉 트랙룸", presentTime.plusDays(3), "profileUrl", 2);

            // when
            final TeamStatus actual = team.status(presentTime);

            // then
            assertThat(actual).isEqualTo(TeamStatus.READY);
        }

        @Test
        @DisplayName("인터뷰 시작시간이 현재 시간보다 이후이면서 종료되지 않은 경우 IN_PROGRESS를 반환한다.")
        void inProgressStatus_success() {
            // given
            final LocalDateTime presentTime = LocalDateTime.now();
            final Team team = new Team("네오와 함께하는 레벨 인터뷰", "선릉 트랙룸", presentTime.plusDays(1), "profileUrl", 2);

            // when
            final TeamStatus actual = team.status(presentTime.plusDays(2));

            // then
            assertThat(actual).isEqualTo(TeamStatus.IN_PROGRESS);
        }

        @Test
        @DisplayName("인터뷰가 종료된 경우 CLOSED를 반환한다.")
        void closedStatus_success() {
            // given
            final LocalDateTime presentTime = LocalDateTime.now();
            final Team team = new Team("네오와 함께하는 레벨 인터뷰", "선릉 트랙룸", presentTime.plusDays(1), "profileUrl", 2);
            team.close(presentTime.plusDays(2));

            // when
            final TeamStatus actual = team.status(presentTime.plusDays(3));

            // then
            assertThat(actual).isEqualTo(TeamStatus.CLOSED);
        }
    }
}

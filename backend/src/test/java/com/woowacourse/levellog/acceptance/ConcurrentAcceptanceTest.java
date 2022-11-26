package com.woowacourse.levellog.acceptance;

import static com.woowacourse.levellog.fixture.MemberFixture.EVE;
import static com.woowacourse.levellog.fixture.MemberFixture.PEPPER;
import static com.woowacourse.levellog.fixture.RestAssuredTemplate.get;
import static org.assertj.core.api.Assertions.assertThat;

import com.woowacourse.levellog.feedback.dto.response.FeedbackResponses;
import com.woowacourse.levellog.team.dto.response.TeamDetailResponse;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("동시성 관련 기능")
class ConcurrentAcceptanceTest extends AcceptanceTest {

    /*
     * Scenario: 한 명의 사용자가 같은 대상에 대해 다수의 피드백을 작성
     *   when: 동시에 여러 개의 피드백 작성을 요청한다.
     *   then: 한 개의 피드백만 작성에 성공한다.
     */
    @Test
    @DisplayName("피드백 동시 작성")
    void concurrentlyCreateFeedback() throws InterruptedException {
        // given
        PEPPER.save();
        EVE.save();
        final String teamId = saveTeam("잠실 제이슨조", PEPPER, 1, PEPPER, EVE).getTeamId();
        final String levellogId = saveLevellog("동시성을 학습했습니다.", teamId, PEPPER).getLevellogId();
        timeStandard.setInProgress();

        final ExecutorService executorService = Executors.newFixedThreadPool(2);
        final CountDownLatch countDownLatch = new CountDownLatch(2);

        // when
        for (int i = 0; i < 2; i++) {
            executorService.execute(() -> {
                saveFeedback("이브가 페퍼의 레벨로그에 작성한 피드백", levellogId, EVE);
                countDownLatch.countDown();
            });
        }
        countDownLatch.await();

        // then
        final FeedbackResponses response = get("/api/levellogs/" + levellogId + "/feedbacks", PEPPER.getToken())
                .getResponse().extract().body().as(FeedbackResponses.class);
        final long numberOfPepperFeedbacks = response.getFeedbacks().stream()
                .filter(it -> it.getFrom().getId().equals(EVE.getId()) && it.getTo().getId().equals(PEPPER.getId()))
                .count();
        assertThat(numberOfPepperFeedbacks).isEqualTo(1);
    }

    /*
     * Scenario: 한 명의 사용자가 다수의 레벨로그 작성
     *   when: 동시에 여러 개의 레벨 로그 작성을 요청한다.
     *   then: 한 개의 레벨로그만 작성에 성공한다.
     */
    @Test
    @DisplayName("레벨로그 동시 작성")
    void concurrentlyCreateLevellog() throws InterruptedException {
        // given
        PEPPER.save();
        EVE.save();
        final String teamId = saveTeam("잠실 제이슨조", PEPPER, 1, PEPPER, EVE).getTeamId();

        final ExecutorService executorService = Executors.newFixedThreadPool(2);
        final CountDownLatch countDownLatch = new CountDownLatch(2);

        // when
        for (int i = 0; i < 2; i++) {
            executorService.execute(() -> {
                saveLevellog("Spring과 React를 학습했습니다.", teamId, PEPPER);
                countDownLatch.countDown();
            });
        }
        countDownLatch.await();

        // then
        final TeamDetailResponse response = get("/api/teams/" + teamId, PEPPER.getToken()).getResponse()
                .extract().body().as(TeamDetailResponse.class);
        final long numberOfLevellogs = response.getParticipants()
                .stream()
                .filter(it -> it.getLevellogId() != null)
                .count();
        assertThat(numberOfLevellogs).isEqualTo(1);
    }
}

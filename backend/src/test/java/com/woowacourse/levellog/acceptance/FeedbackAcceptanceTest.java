package com.woowacourse.levellog.acceptance;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

import com.woowacourse.levellog.feedback.dto.FeedbackContentDto;
import com.woowacourse.levellog.feedback.dto.FeedbackWriteDto;
import com.woowacourse.levellog.fixture.RestAssuredResponse;
import com.woowacourse.levellog.fixture.RestAssuredTemplate;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("피드백 관련 기능")
class FeedbackAcceptanceTest extends AcceptanceTest {

    /*
     * Scenario: 피드백 작성
     *   when: 피드백 작성을 요청한다.
     *   then: 201 Created 상태 코드와 Location 헤더에 /api/feedbacks를 담아 응답받는다.
     */
    @Test
    @DisplayName("피드백 작성")
    void createFeedback() {
        // given
        final String rickToken = login("릭").getToken();

        final RestAssuredResponse romaLoginResponse = login("로마");
        final Long romaId = romaLoginResponse.getMemberId();
        final String romaToken = romaLoginResponse.getToken();

        final String teamId = requestCreateTeam("릭 and 로마", rickToken, 1, List.of(romaId)).getTeamId();
        final String levellogId = requestCreateLevellog("레벨로그", teamId, rickToken).getLevellogId();

        timeStandard.setInProgress();

        // when
        final FeedbackContentDto feedbackContentDto = new FeedbackContentDto("Spring에 대한 학습을 충분히 하였습니다.",
                "아이 컨텍이 좋습니다.",
                "윙크하지 마세요.");
        final FeedbackWriteDto request = new FeedbackWriteDto(feedbackContentDto);

        final ValidatableResponse response = RestAssured.given(specification).log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + romaToken)
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .filter(document("feedback/save"))
                .when()
                .post("/api/levellogs/{levellogId}/feedbacks", levellogId)
                .then().log().all();

        // then
        response.statusCode(HttpStatus.CREATED.value())
                .header(HttpHeaders.LOCATION, notNullValue());
    }

    /*
     * Scenario: 피드백 전체 조회
     *   given: 피드백이 등록되어있다.
     *   when: 등록된 모든 피드백을 조회한다.
     *   then: 200 OK 상태 코드와 모든 피드백을 응답 받는다.
     */
    @Test
    @DisplayName("피드백 조회")
    void findAllFeedbacks() {
        // given
        final String rickToken = login("릭").getToken();

        final RestAssuredResponse romaLoginResponse = login("로마");
        final Long romaId = romaLoginResponse.getMemberId();
        final String romaToken = romaLoginResponse.getToken();

        final String teamId = requestCreateTeam("릭 and 로마", rickToken, 1, List.of(romaId)).getTeamId();
        final String levellogId = requestCreateLevellog("레벨로그", teamId, rickToken).getLevellogId();

        timeStandard.setInProgress();

        requestCreateFeedback("test", levellogId, romaToken);

        // when
        final ValidatableResponse response = RestAssured.given(specification).log().all()
                .filter(document("feedback/find-all"))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + romaToken)
                .when()
                .get("/api/levellogs/{levellogId}/feedbacks", levellogId)
                .then().log().all();

        // then
        response.statusCode(HttpStatus.OK.value())
                .body("feedbacks.from.nickname", contains("로마"),
                        "feedbacks.to.nickname", contains("릭"),
                        "feedbacks.feedback.study", contains("study test"),
                        "feedbacks.feedback.speak", contains("speak test"),
                        "feedbacks.feedback.etc", contains("etc test")
                );
    }

    /*
     * Scenario: 피드백 수정
     *   given: 피드백이 등록되어 있다.
     *   given: 등록된 피드백을 조회한다.
     *   when: 조회한 피드백 내용을 수정한다.
     *   then: 204 No Content 상태 코드를 응답받는다.
     */
    @Test
    @DisplayName("피드백 수정")
    void updateFeedback() {
        // given
        final String rickToken = login("릭").getToken();

        final RestAssuredResponse romaLoginResponse = login("로마");
        final Long roma_id = romaLoginResponse.getMemberId();
        final String romaToken = romaLoginResponse.getToken();

        final String teamId = requestCreateTeam("릭 and 로마", rickToken, 1, List.of(roma_id)).getTeamId();
        final String levellogId = requestCreateLevellog("레벨로그", teamId, rickToken).getLevellogId();

        timeStandard.setInProgress();

        final String feedbackId = requestCreateFeedback("test", levellogId, romaToken).getFeedbackId();

        // when
        final FeedbackContentDto feedbackContentDto = new FeedbackContentDto(
                "수정된 Study 피드백", "수정된 Speak 피드백", "수정된 Etc 피드백");
        final FeedbackWriteDto request = new FeedbackWriteDto(feedbackContentDto);

        final ValidatableResponse response = RestAssured.given(specification).log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + romaToken)
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .filter(document("feedback/update"))
                .when()
                .put("/api/levellogs/{levellogId}/feedbacks/{feedbackId}", levellogId, feedbackId)
                .then().log().all();

        // then
        response.statusCode(HttpStatus.NO_CONTENT.value());
        RestAssuredTemplate.get("/api/levellogs/" + levellogId + "/feedbacks", romaToken)
                .getResponse()
                .body("feedbacks.feedback.study", contains("수정된 Study 피드백"),
                        "feedbacks.feedback.speak", contains("수정된 Speak 피드백"),
                        "feedbacks.feedback.etc", contains("수정된 Etc 피드백")
                );
    }
}

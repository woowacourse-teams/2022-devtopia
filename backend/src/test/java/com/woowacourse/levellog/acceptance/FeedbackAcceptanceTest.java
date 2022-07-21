package com.woowacourse.levellog.acceptance;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

import com.woowacourse.levellog.dto.FeedbackContentDto;
import com.woowacourse.levellog.dto.FeedbackRequest;
import com.woowacourse.levellog.dto.FeedbacksResponse;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
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
        final ValidatableResponse levellogResponse = requestCreateLevellog("트렌젝션에 대해 학습함.", "제이슨조", MASTER, MASTER,
                "릭", "로마");
        final String levellogId = getLevellogId(levellogResponse);

        final FeedbackContentDto feedbackContentDto = new FeedbackContentDto("Spring에 대한 학습을 충분히 하였습니다.",
                "아이 컨텍이 좋습니다.",
                "윙크하지 마세요.");
        final FeedbackRequest request = new FeedbackRequest(feedbackContentDto);

        // when
        final ValidatableResponse response = RestAssured.given(specification).log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + masterToken)
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
        final ValidatableResponse levellogResponse = requestCreateLevellog("트렌젝션에 대해 학습함.", "제이슨조", MASTER, MASTER,
                "릭", "로마");
        final String levellogId = getLevellogId(levellogResponse);

        requestCreateFeedback(levellogId, "로마가 쓴", "로마");
        requestCreateFeedback(levellogId, "알린이 쓴", "알린");

        // when
        final ValidatableResponse response = RestAssured.given(specification).log().all()
                .filter(document("feedback/find-all"))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + masterToken)
                .when()
                .get("/api/levellogs/{levellogId}/feedbacks", levellogId)
                .then().log().all();

        // then
        response.statusCode(HttpStatus.OK.value())
                .body(  "feedbacks.feedback.study", contains("study - 로마가 쓴", "study - 알린이 쓴"),
                        "feedbacks.feedback.speak", contains("speak - 로마가 쓴", "speak - 알린이 쓴"),
                        "feedbacks.feedback.etc", contains("etc - 로마가 쓴", "etc - 알린이 쓴")
                );
    }

    /*
     * Scenario: 피드백 삭제
     *   given: 피드백이 등록되어 있다.
     *   given: 피드백을 모두 조회한다.
     *   when: 특정 피드백을 삭제한다.
     *   then: 204 No Content 상태 코드를 응답받는다.
     */

    @Test
    @DisplayName("피드백 삭제")
    void deleteFeedback() {
        // given
        final ValidatableResponse levellogResponse = requestCreateLevellog("트렌젝션에 대해 학습함.", "제이슨조", MASTER, MASTER,
                "릭", "로마");
        final String levellogId = getLevellogId(levellogResponse);

        requestCreateFeedback(levellogId, "로마가 쓴", "로마");
        requestCreateFeedback(levellogId, "알린이 쓴", "알린");

        final Long deleteId = requestFindAllFeedbacks(levellogId)
                .extract()
                .as(FeedbacksResponse.class)
                .getFeedbacks()
                .get(0)
                .getId();

        // when
        final ValidatableResponse response = RestAssured.given(specification).log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + masterToken)
                .filter(document("feedback/delete"))
                .when()
                .delete("/api/levellogs/{levellogId}/feedbacks/{feedbackId}", levellogId, deleteId)
                .then().log().all();

        // then
        response.statusCode(HttpStatus.NO_CONTENT.value());
        requestFindAllFeedbacks(levellogId)
                .body("feedbacks.id", not(contains(deleteId)));
    }

    private ValidatableResponse requestFindAllFeedbacks(final String levellogId) {
        return RestAssured.given().log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + masterToken)
                .when()
                .get("/api/levellogs/{levellogId}/feedbacks", levellogId)
                .then().log().all();
    }
}

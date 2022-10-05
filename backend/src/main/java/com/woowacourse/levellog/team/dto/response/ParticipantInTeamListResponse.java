package com.woowacourse.levellog.team.dto.response;

import com.woowacourse.levellog.team.dto.query.AllTeamListQueryResult;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ParticipantInTeamListResponse {

    private Long memberId;
    private String profileUrl;

    public static ParticipantInTeamListResponse from(final AllTeamListQueryResult allTeamListQueryResult) {
        return new ParticipantInTeamListResponse(
                allTeamListQueryResult.getMemberId(),
                allTeamListQueryResult.getMemberImage()
        );
    }
}

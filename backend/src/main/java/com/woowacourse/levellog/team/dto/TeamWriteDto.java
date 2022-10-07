package com.woowacourse.levellog.team.dto;

import com.woowacourse.levellog.team.domain.ParticipantsIngredient;
import com.woowacourse.levellog.team.domain.TeamDetail;
import java.time.LocalDateTime;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TeamWriteDto {

    @NotBlank
    private String title;

    @NotBlank
    private String place;

    @Positive
    private int interviewerNumber;

    @NotNull
    private LocalDateTime startAt;

    @Valid
    @NotEmpty
    private List<Long> participantIds;

    private List<Long> watcherIds;

    public TeamDetail toTeamDetail(final String profileUrl) {
        return new TeamDetail(title, place, startAt, profileUrl, interviewerNumber);
    }

    public ParticipantsIngredient toParticipantsIngredient(final Long hostId) {
        return new ParticipantsIngredient(hostId, participantIds, watcherIds);
    }
}

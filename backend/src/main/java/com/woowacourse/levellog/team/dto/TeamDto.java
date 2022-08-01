package com.woowacourse.levellog.team.dto;

import com.woowacourse.levellog.team.domain.Team;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
public class TeamDto {

    private Long id;
    private String title;
    private String place;
    private LocalDateTime startAt;
    private String teamImage;
    private Boolean isClosed;
    private Long hostId;
    private List<ParticipantDto> participants;

    public static TeamDto from(final Team team, final Long hostId, final List<ParticipantDto> participantResponses) {
        return new TeamDto(
                team.getId(),
                team.getTitle(),
                team.getPlace(),
                team.getStartAt(),
                team.getProfileUrl(),
                team.isClosed(),
                hostId,
                participantResponses
        );
    }
}

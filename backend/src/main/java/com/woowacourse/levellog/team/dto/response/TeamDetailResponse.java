package com.woowacourse.levellog.team.dto.response;

import com.woowacourse.levellog.team.domain.SimpleParticipants;
import com.woowacourse.levellog.team.domain.TeamStatus;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TeamDetailResponse {

    private Long id;
    private String title;
    private String place;
    private LocalDateTime startAt;
    private String teamImage;
    private TeamStatus status;
    private Long hostId;
    private Boolean isParticipant;
    private List<Long> interviewers;
    private List<Long> interviewees;
    private List<ParticipantResponse> participants;
    private List<WatcherResponse> watchers;

    public static TeamDetailResponse from(final TeamResponse teamResponse,
                                          final TeamStatus status,
                                          final Long memberId,
                                          final SimpleParticipants participants,
                                          final List<ParticipantResponse> participantResponses,
                                          final List<WatcherResponse> watcherResponses) {
        return new TeamDetailResponse(
                teamResponse.getId(),
                teamResponse.getTitle(),
                teamResponse.getPlace(),
                teamResponse.getStartAt(),
                teamResponse.getProfileUrl(),
                status,
                participants.toHostId(),
                participants.isContains(memberId),
                participants.toInterviewerIds(memberId, teamResponse.getInterviewerNumber()),
                participants.toIntervieweeIds(memberId, teamResponse.getInterviewerNumber()),
                participantResponses,
                watcherResponses
        );
    }
}

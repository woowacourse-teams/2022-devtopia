package com.woowacourse.levellog.team.presentation;

import com.woowacourse.levellog.authentication.support.Authentic;
import com.woowacourse.levellog.authentication.support.PublicAPI;
import com.woowacourse.levellog.team.application.TeamQueryService;
import com.woowacourse.levellog.team.application.TeamService;
import com.woowacourse.levellog.team.domain.TeamFilterCondition;
import com.woowacourse.levellog.team.dto.response.InterviewRoleResponse;
import com.woowacourse.levellog.team.dto.response.TeamDetailResponse;
import com.woowacourse.levellog.team.dto.response.TeamListResponses;
import com.woowacourse.levellog.team.dto.response.TeamStatusResponse;
import com.woowacourse.levellog.team.dto.request.TeamWriteRequest;
import java.net.URI;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/teams")
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;
    private final TeamQueryService teamQueryService;

    @PostMapping
    public ResponseEntity<Void> save(@RequestBody @Valid final TeamWriteRequest teamDto,
                                     @Authentic final Long memberId) {
        final Long teamId = teamService.save(teamDto, memberId);
        return ResponseEntity.created(URI.create("/api/teams/" + teamId)).build();
    }

    @GetMapping
    @PublicAPI
    public ResponseEntity<TeamListResponses> findAll(@RequestParam(defaultValue = "open") final String condition,
                                                     @RequestParam(defaultValue = "0") final int page,
                                                     @RequestParam(defaultValue = "20") final int size) {
        final TeamFilterCondition filterCondition = TeamFilterCondition.from(condition);
        final TeamListResponses response = teamQueryService.findAll(filterCondition, page, size);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{teamId}")
    @PublicAPI
    public ResponseEntity<TeamDetailResponse> findById(@PathVariable final Long teamId,
                                                       @Authentic final Long memberId) {
        final TeamDetailResponse response = teamQueryService.findByTeamIdAndMemberId(teamId, memberId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{teamId}/status")
    @PublicAPI
    public ResponseEntity<TeamStatusResponse> findStatus(@PathVariable final Long teamId) {
        final TeamStatusResponse response = teamService.findStatus(teamId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{teamId}/members/{targetMemberId}/my-role")
    public ResponseEntity<InterviewRoleResponse> findMyRole(@PathVariable final Long teamId,
                                                            @PathVariable final Long targetMemberId,
                                                            @Authentic final Long memberId) {
        final InterviewRoleResponse response = teamService.findMyRole(teamId, targetMemberId, memberId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{teamId}")
    public ResponseEntity<Void> update(@PathVariable final Long teamId,
                                       @RequestBody @Valid final TeamWriteRequest request,
                                       @Authentic final Long memberId) {
        teamService.update(request, teamId, memberId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{teamId}/close")
    public ResponseEntity<Void> close(@PathVariable final Long teamId,
                                      @Authentic final Long memberId) {
        teamService.close(teamId, memberId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{teamId}")
    public ResponseEntity<Void> delete(@PathVariable final Long teamId, @Authentic final Long memberId) {
        teamService.deleteById(teamId, memberId);
        return ResponseEntity.noContent().build();
    }
}

package com.woowacourse.levellog.levellog.domain;

import com.woowacourse.levellog.common.support.DebugMessage;
import com.woowacourse.levellog.levellog.exception.LevellogNotFoundException;
import com.woowacourse.levellog.team.domain.Team;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LevellogRepository extends JpaRepository<Levellog, Long> {

    default Levellog getLevellog(final Long levellogId) {
        return findById(levellogId)
                .orElseThrow(() -> new LevellogNotFoundException(DebugMessage.init()
                        .append("levellogId", levellogId)));
    }

    Optional<Levellog> findByAuthorIdAndTeamId(Long authorId, Long teamId);

    boolean existsByAuthorIdAndTeam(Long authorId, Team team);

    @Query("SELECT l FROM Levellog l INNER JOIN FETCH l.team WHERE l.authorId = :authorId")
    List<Levellog> findAllByAuthorId(Long authorId);
}

package com.woowacourse.levellog.prequestion.domain;

import com.woowacourse.levellog.levellog.domain.Levellog;
import com.woowacourse.levellog.member.domain.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PreQuestionRepository extends JpaRepository<PreQuestion, Long> {

    @Query("SELECT pq FROM PreQuestion pq INNER JOIN FETCH pq.author WHERE pq.author = :author AND pq.levellog = :levellog")
    Optional<PreQuestion> findByLevellogAndAuthor(@Param("levellog") Levellog levellog, @Param("author") Member author);

    boolean existsByLevellogAndAuthor(Levellog levellog, Member author);
}

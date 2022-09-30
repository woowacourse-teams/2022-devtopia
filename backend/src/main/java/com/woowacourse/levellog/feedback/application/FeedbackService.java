package com.woowacourse.levellog.feedback.application;

import com.woowacourse.levellog.authentication.support.Authentic;
import com.woowacourse.levellog.common.support.DebugMessage;
import com.woowacourse.levellog.feedback.domain.Feedback;
import com.woowacourse.levellog.feedback.domain.FeedbackQueryRepository;
import com.woowacourse.levellog.feedback.domain.FeedbackRepository;
import com.woowacourse.levellog.feedback.dto.FeedbackDto;
import com.woowacourse.levellog.feedback.dto.FeedbackWriteDto;
import com.woowacourse.levellog.feedback.dto.FeedbacksDto;
import com.woowacourse.levellog.feedback.exception.FeedbackAlreadyExistException;
import com.woowacourse.levellog.levellog.domain.Levellog;
import com.woowacourse.levellog.levellog.domain.LevellogRepository;
import com.woowacourse.levellog.member.domain.MemberRepository;
import com.woowacourse.levellog.team.domain.ParticipantRepository;
import com.woowacourse.levellog.team.domain.Team;
import com.woowacourse.levellog.team.exception.ParticipantNotSameTeamException;
import com.woowacourse.levellog.team.support.TimeStandard;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final FeedbackQueryRepository feedbackQueryRepository;
    private final LevellogRepository levellogRepository;
    private final ParticipantRepository participantRepository;
    private final TimeStandard timeStandard;

    @Transactional
    public Long save(final FeedbackWriteDto request, final Long levellogId, @Authentic final Long fromMemberId) {
        validateExistence(levellogId, fromMemberId);

        final Levellog levellog = levellogRepository.getLevellog(levellogId);
        final Team team = levellog.getTeam();

        levellog.validateSelfFeedback(fromMemberId);
        validateTeamMember(team, fromMemberId);
        team.validateInProgress(timeStandard.now());

        final Feedback feedback = request.getFeedback()
                .toFeedback(fromMemberId, levellog);

        return feedbackRepository.save(feedback)
                .getId();
    }

    public FeedbacksDto findAll(final Long levellogId, @Authentic final Long memberId) {
        final Levellog levellog = levellogRepository.getLevellog(levellogId);
        validateTeamMember(levellog.getTeam(), memberId);

        return feedbackQueryRepository.findAllByLevellog(levellogId);
    }

    public FeedbackDto findById(final Long levellogId, final Long feedbackId, @Authentic final Long memberId) {
        final Levellog levellog = levellogRepository.getLevellog(levellogId);

        validateTeamMember(levellog.getTeam(), memberId);

        return feedbackQueryRepository.findById(feedbackId);
    }

    public FeedbacksDto findAllByTo(@Authentic final Long memberId) {
        return feedbackQueryRepository.findAllByTo(memberId);
    }

    @Transactional
    public void update(final FeedbackWriteDto request, final Long feedbackId, @Authentic final Long memberId) {
        final Feedback feedback = feedbackRepository.getFeedback(feedbackId);
        final Team team = feedback.getLevellog().getTeam();

        feedback.validateAuthor(memberId);
        team.validateInProgress(timeStandard.now());

        feedback.updateFeedback(
                request.getFeedback().getStudy(),
                request.getFeedback().getSpeak(),
                request.getFeedback().getEtc());
    }

    private void validateExistence(final Long levellogId, final Long fromMemberId) {
        if (feedbackRepository.existsByLevellogIdAndFromId(levellogId, fromMemberId)) {
            throw new FeedbackAlreadyExistException(DebugMessage.init()
                    .append("levellogId", levellogId));
        }
    }

    private void validateTeamMember(final Team team, final Long memberId) {
        if (!participantRepository.existsByMemberIdAndTeam(memberId, team)) {
            throw new ParticipantNotSameTeamException(DebugMessage.init()
                    .append("teamId", team.getId())
                    .append("memberId", memberId));
        }
    }
}

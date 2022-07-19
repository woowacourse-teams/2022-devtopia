package com.woowacourse.levellog.application;

import com.woowacourse.levellog.authentication.exception.MemberNotFoundException;
import com.woowacourse.levellog.domain.Feedback;
import com.woowacourse.levellog.domain.FeedbackRepository;
import com.woowacourse.levellog.domain.Levellog;
import com.woowacourse.levellog.domain.LevellogRepository;
import com.woowacourse.levellog.domain.Member;
import com.woowacourse.levellog.domain.MemberRepository;
import com.woowacourse.levellog.dto.FeedbackContentDto;
import com.woowacourse.levellog.dto.FeedbackRequest;
import com.woowacourse.levellog.dto.FeedbackResponse;
import com.woowacourse.levellog.dto.FeedbacksResponse;
import com.woowacourse.levellog.dto.MemberResponse;
import com.woowacourse.levellog.exception.FeedbackNotFoundException;
import com.woowacourse.levellog.exception.LevellogNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final LevellogRepository levellogRepository;
    private final MemberRepository memberRepository;

    public Long save(final Long levellogId, final Long fromMemberId, final FeedbackRequest request) {
        final FeedbackContentDto feedbackContent = request.getFeedback();
        final Levellog levellog = getLevellog(levellogId);
        final Member member = getMember(fromMemberId);
        final Feedback feedback = new Feedback(
                member,
                levellog.getAuthor(),
                levellog,
                feedbackContent.getStudy(),
                feedbackContent.getSpeak(),
                feedbackContent.getEtc());

        final Feedback savedFeedback = feedbackRepository.save(feedback);
        return savedFeedback.getId();
    }

    public FeedbacksResponse findAll(final Long levellogId) {
        final Levellog levellog = getLevellog(levellogId);
        final List<FeedbackResponse> responses = getFeedbackResponses(feedbackRepository.findAllByLevellog(levellog));

        return new FeedbacksResponse(responses);
    }

    public FeedbacksResponse findAllByTo(final Long memberId) {
        final Member member = getMember(memberId);
        final List<Feedback> feedbacks = feedbackRepository.findAllByToOrderByUpdatedAtDesc(member);
        return new FeedbacksResponse(getFeedbackResponses(feedbacks));
    }

    public void update(final Long id, final FeedbackRequest request) {
        final Feedback feedback = feedbackRepository.findById(id)
                .orElseThrow(FeedbackNotFoundException::new);

        feedback.updateFeedback(
                request.getFeedback().getStudy(),
                request.getFeedback().getSpeak(),
                request.getFeedback().getEtc());
    }

    public void deleteById(final Long id) {
        feedbackRepository.deleteById(id);
    }

    private List<FeedbackResponse> getFeedbackResponses(final List<Feedback> feedbacks) {
        return feedbacks.stream()
                .map(it -> new FeedbackResponse(
                        it.getId(),
                        MemberResponse.from(it.getFrom()),
                        new FeedbackContentDto(it.getStudy(), it.getSpeak(), it.getEtc()),
                        it.getUpdatedAt()))
                .collect(Collectors.toList());
    }

    private Member getMember(final Long memberId) {
        return memberRepository
                .findById(memberId).orElseThrow(MemberNotFoundException::new);
    }

    private Levellog getLevellog(final Long levellogId) {
        return levellogRepository.findById(levellogId).orElseThrow(LevellogNotFoundException::new);
    }
}

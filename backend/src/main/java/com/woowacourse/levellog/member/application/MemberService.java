package com.woowacourse.levellog.member.application;

import com.woowacourse.levellog.authentication.dto.response.GithubProfileResponse;
import com.woowacourse.levellog.authentication.support.Verified;
import com.woowacourse.levellog.common.dto.LoginStatus;
import com.woowacourse.levellog.common.support.DebugMessage;
import com.woowacourse.levellog.member.domain.Member;
import com.woowacourse.levellog.member.domain.MemberRepository;
import com.woowacourse.levellog.member.domain.NicknameMapping;
import com.woowacourse.levellog.member.domain.NicknameMappingRepository;
import com.woowacourse.levellog.member.dto.request.MemberCreateRequest;
import com.woowacourse.levellog.member.dto.request.NicknameUpdateRequest;
import com.woowacourse.levellog.member.dto.response.MemberResponse;
import com.woowacourse.levellog.member.dto.response.MemberResponses;
import com.woowacourse.levellog.member.exception.MemberAlreadyExistException;
import com.woowacourse.levellog.member.exception.MemberNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final NicknameMappingRepository nicknameMappingRepository;

    @Transactional
    public Long save(final MemberCreateRequest request) {
        checkSameGithubId(request);

        final Member member = createMember(request);
        final Member savedMember = memberRepository.save(member);

        return savedMember.getId();
    }

    @Transactional
    public Long saveIfNotExist(final GithubProfileResponse request) {
        final int githubId = getGithubId(request);
        if (existsGithubId(githubId)) {
            return getByGithubId(githubId).getId();
        }

        return save(
            new MemberCreateRequest(request.getNickname(), githubId, request.getProfileUrl()));
    }

    public MemberResponse findMemberById(final LoginStatus loginStatus) {
        final Member member = memberRepository.getMember(loginStatus.getMemberId());

        return new MemberResponse(member.getId(), member.getNickname(), member.getProfileUrl());
    }

    public MemberResponses searchByNickname(final String nickname) {
        final List<MemberResponse> responses = memberRepository.findAllByNicknameContains(nickname)
            .stream()
                .map(member -> new MemberResponse(member.getId(), member.getNickname(), member.getProfileUrl()))
                .collect(Collectors.toList());

        return new MemberResponses(responses);
    }

    @Transactional
    public void updateNickname(final NicknameUpdateRequest request,
        @Verified final LoginStatus loginStatus) {
        final Member member = memberRepository.getMember(loginStatus.getMemberId());
        member.updateNickname(request.getNickname());
    }

    private int getGithubId(final GithubProfileResponse request) {
        return Integer.parseInt(request.getGithubId());
    }

    private void checkSameGithubId(final MemberCreateRequest request) {
        if (existsGithubId(request.getGithubId())) {
            throw new MemberAlreadyExistException(DebugMessage.init()
                .append("githubId", request.getGithubId()));
        }
    }

    private boolean existsGithubId(final int githubId) {
        return memberRepository.existsByGithubId(githubId);
    }

    private Member createMember(final MemberCreateRequest request) {
        final Member member = request.toEntity();
        final Optional<NicknameMapping> nickname = nicknameMappingRepository.findByGithubNickname(
            request.getNickname());
        if (nickname.isPresent()) {
            member.updateNickname(nickname.get().getCrewNickname());
        }

        return member;
    }

    private Member getByGithubId(final int githubId) {
        return memberRepository.findByGithubId(githubId)
                .orElseThrow(() -> new MemberNotFoundException(DebugMessage.init()
                        .append("githubId", githubId)));
    }
}

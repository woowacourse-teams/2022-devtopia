package com.woowacourse.levellog.team.domain;

import com.woowacourse.levellog.common.domain.BaseEntity;
import com.woowacourse.levellog.common.exception.InvalidFieldException;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Team extends BaseEntity {

    private static final int DEFAULT_STRING_SIZE = 255;
    private static final int PROFILE_URL_SIZE = 2048;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String place;

    @Column(nullable = false)
    private LocalDateTime startAt;

    @Column(nullable = false, length = PROFILE_URL_SIZE)
    private String profileUrl;

    public Team(final String title, final String place, final LocalDateTime startAt, final String profileUrl) {
        validateTitle(title);
        validatePlace(place);
        validateStartAt(startAt);
        validateProfileUrl(profileUrl);

        this.title = title;
        this.place = place;
        this.startAt = startAt;
        this.profileUrl = profileUrl;
    }

    private void validateTitle(final String title) {
        if (title == null) {
            throw new InvalidFieldException("팀 이름이 없습니다. 입력한 팀 이름 : [null]");
        }
        if (title.isEmpty()) {
            throw new InvalidFieldException("팀 이름이 비어있습니다. 입력한 팀 이름 : [" + title + "]");
        }
        if (title.length() > DEFAULT_STRING_SIZE) {
            throw new InvalidFieldException("잘못된 팀 이름을 입력했습니다. 입력한 팀 이름 : [" + title + "]");
        }
    }

    private void validatePlace(final String place) {
        if (place == null) {
            throw new InvalidFieldException("장소가 없습니다. 입력한 장소 이름 : [null]");
        }
        if (place.isEmpty()) {
            throw new InvalidFieldException("장소가 비어있습니다. 입력한 장소 이름 : [" + place + "]");
        }
        if (place.length() > DEFAULT_STRING_SIZE) {
            throw new InvalidFieldException("잘못된 장소를 입력했습니다. 입력한 장소 이름 : [" + place + "]");
        }
    }

    private void validateStartAt(final LocalDateTime startAt) {
        if (startAt == null) {
            throw new InvalidFieldException("시작 시간이 없습니다. 입력한 시작 시간 : [null]");
        }
        if (LocalDateTime.now().isAfter(startAt)) {
            throw new InvalidFieldException("잘못된 시작 시간을 입력했습니다. 입력한 시작 시간 : [" + startAt + "]");
        }
    }

    private void validateProfileUrl(final String profileUrl) {
        if (profileUrl == null) {
            throw new InvalidFieldException("팀 프로필 사진이 없습니다. 입력한 프로필 URL : [null]");
        }
        if (profileUrl.isEmpty()) {
            throw new InvalidFieldException("팀 프로필 사진이 비어있습니다. 입력한 프로필 URL : [" + profileUrl + "]");
        }
        if (profileUrl.length() > PROFILE_URL_SIZE) {
            throw new InvalidFieldException("잘못된 팀 프로필 사진을 입력했습니다. 입력한 프로필 URL : [" + profileUrl + "]");
        }
    }

    public void update(final String title, final String place, final LocalDateTime startAt) {
        validateTitle(title);
        validatePlace(place);
        validateStartAt(startAt);

        this.title = title;
        this.place = place;
        this.startAt = startAt;
    }
}

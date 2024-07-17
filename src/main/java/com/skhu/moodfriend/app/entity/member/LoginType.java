package com.skhu.moodfriend.app.entity.member;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public enum LoginType {

    NATIVE_LOGIN("NATIVE_LOGIN", "자체 로그인"),
    KAKAO_LOGIN("KAKAO_LOGIN", "카카오 로그인"),
    GOOGLE_LOGIN("GOOGLE_LOGIN", "구글 로그인");

    private final String code;
    private final String displayName;
}

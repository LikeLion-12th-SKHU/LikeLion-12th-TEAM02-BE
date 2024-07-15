package com.skhu.moodfriend.app.entity.user;

import com.skhu.moodfriend.global.exception.CustomException;
import com.skhu.moodfriend.global.exception.code.ErrorCode;
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

    public static LoginType getLoginTypeOfString(String loginType) {
        for (LoginType type : LoginType.values()) {
            if (type.code.equals(loginType)) {
                return type;
            }
        }

        throw new CustomException(ErrorCode.INVALID_ROLE_TYPE_EXCEPTION, ErrorCode.INVALID_TOKEN_EXCEPTION.getMessage());
    }
}

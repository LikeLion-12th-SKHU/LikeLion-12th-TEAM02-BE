package com.skhu.moodfriend.app.dto.auth.reqDto;

public record SignUpReqDto(
        String email,
        String password,
        String name,
        String loginType,
        String roleType
) {
}

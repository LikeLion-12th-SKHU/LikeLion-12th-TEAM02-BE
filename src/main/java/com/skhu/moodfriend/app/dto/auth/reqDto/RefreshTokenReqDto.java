package com.skhu.moodfriend.app.dto.auth.reqDto;

import lombok.Builder;

@Builder
public record RefreshTokenReqDto(
        String refreshToken
) {
}

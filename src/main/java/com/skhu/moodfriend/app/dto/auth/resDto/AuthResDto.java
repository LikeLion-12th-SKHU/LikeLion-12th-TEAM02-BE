package com.skhu.moodfriend.app.dto.auth.resDto;

import lombok.Builder;

@Builder
public record AuthResDto(
        String accessToken,
        String refreshToken
) {
    public static AuthResDto of(String accessToken, String refreshToken) {
        return AuthResDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}

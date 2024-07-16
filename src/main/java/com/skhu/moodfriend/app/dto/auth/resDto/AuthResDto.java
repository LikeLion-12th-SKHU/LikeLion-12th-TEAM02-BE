package com.skhu.moodfriend.app.dto.auth.resDto;

import lombok.Builder;

@Builder
public record AuthResDto(
        String accessToken,
        String refreshToken
) {
}

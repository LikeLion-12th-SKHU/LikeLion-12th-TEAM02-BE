package com.skhu.moodfriend.app.dto.auth.resDto;

import lombok.Builder;

@Builder
public record LoginResDto(
        String accessToken,
        String refreshToken
) {
}

package com.skhu.moodfriend.app.dto.auth.resDto;

import lombok.Builder;

@Builder
public record SignUpResDto(
        String accessToken,
        String refreshToken
) {
}

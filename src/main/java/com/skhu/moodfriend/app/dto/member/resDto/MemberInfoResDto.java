package com.skhu.moodfriend.app.dto.member.resDto;

import lombok.Builder;

@Builder
public record MemberInfoResDto(
        String email,
        String name,
        long mileage,
        String loginType
) {
}

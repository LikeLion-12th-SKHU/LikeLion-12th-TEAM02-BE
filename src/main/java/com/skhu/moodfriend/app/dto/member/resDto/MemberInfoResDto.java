package com.skhu.moodfriend.app.dto.member.resDto;

import com.skhu.moodfriend.app.entity.member.LoginType;
import lombok.Builder;

@Builder
public record MemberInfoResDto(
        String email,
        String name,
        long mileage,
        LoginType loginType
) {
}

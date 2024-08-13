package com.skhu.moodfriend.app.dto.member.resDto;

import com.skhu.moodfriend.app.domain.member.Member;
import lombok.Builder;

@Builder
public record MemberInfoResDto(
        String email,
        String name,
        long mileage,
        String loginType
) {
    public static MemberInfoResDto of(Member member) {
        return MemberInfoResDto.builder()
                .email(member.getEmail())
                .name(member.getName())
                .mileage(member.getMileage())
                .loginType(member.getLoginType().getDisplayName())
                .build();
    }
}

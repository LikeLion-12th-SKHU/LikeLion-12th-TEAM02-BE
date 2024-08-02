package com.skhu.moodfriend.app.dto.object;

import com.skhu.moodfriend.app.domain.member.object.MemberObject;
import lombok.Builder;

@Builder
public record MemberObjectDto(
        boolean status, Long objectId
) {
    public static MemberObjectDto of(MemberObject memberObject) {
        return MemberObjectDto.builder()
                .status(memberObject.isStatus())
                .objectId(memberObject.getMemberObjectId())
                .build();
    }
}

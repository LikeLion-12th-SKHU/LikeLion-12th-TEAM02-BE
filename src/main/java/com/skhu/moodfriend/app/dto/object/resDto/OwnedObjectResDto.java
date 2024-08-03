package com.skhu.moodfriend.app.dto.object.resDto;

import com.skhu.moodfriend.app.domain.member.object.MemberObject;
import lombok.Builder;

@Builder
public record OwnedObjectResDto(
        Long memberObjectId,
        String objectName,
        boolean status
) {
    public static OwnedObjectResDto of(MemberObject memberObject) {
        return OwnedObjectResDto.builder()
                .memberObjectId(memberObject.getMemberObjectId())
                .objectName(memberObject.getObject().getDisplayName())
                .status(memberObject.isStatus())
                .build();
    }
}
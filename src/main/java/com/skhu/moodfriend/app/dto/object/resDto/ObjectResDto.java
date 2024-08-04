package com.skhu.moodfriend.app.dto.object.resDto;

import com.skhu.moodfriend.app.domain.member.object.MemberObject;
import lombok.Builder;

@Builder
public record ObjectResDto(
        Long memberObjectId,
        String objectName,
        boolean status
) {
    public static ObjectResDto of(MemberObject memberObject) {
        return ObjectResDto.builder()
                .memberObjectId(memberObject.getMemberObjectId())
                .objectName(memberObject.getObject().getName())
                .status(memberObject.isStatus())
                .build();
    }
}
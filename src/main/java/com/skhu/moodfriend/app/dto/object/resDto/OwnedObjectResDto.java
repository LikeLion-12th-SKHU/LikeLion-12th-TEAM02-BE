package com.skhu.moodfriend.app.dto.object.resDto;

import com.skhu.moodfriend.app.domain.member.object.MemberObject;
import com.skhu.moodfriend.app.domain.store.ObjectEnum;
import lombok.Builder;

@Builder
public record OwnedObjectResDto(
        Long memberObjectId,
        ObjectEnum object,
        boolean status
) {
    public static OwnedObjectResDto of(MemberObject memberObject) {
        return OwnedObjectResDto.builder()
                .memberObjectId(memberObject.getMemberObjectId())
                .object(memberObject.getObject())
                .status(memberObject.isStatus())
                .build();
    }
}
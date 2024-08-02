package com.skhu.moodfriend.app.dto.object;

import com.skhu.moodfriend.app.domain.member.object.MemberObject;
import com.skhu.moodfriend.app.domain.store.Object;
import lombok.Builder;

@Builder
public record ObjectStoreDto(
        Object object, boolean status, Long objectId
) {
    public static ObjectStoreDto of(MemberObject memberObject) {
        return ObjectStoreDto.builder()
                .status(memberObject.isStatus())
                .objectId(memberObject.getMemberObjectId())
                .build();
    }
}

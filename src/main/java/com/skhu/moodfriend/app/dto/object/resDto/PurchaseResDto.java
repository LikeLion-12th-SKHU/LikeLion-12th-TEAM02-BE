package com.skhu.moodfriend.app.dto.object.resDto;

import com.skhu.moodfriend.app.domain.member.object.MemberObject;
import lombok.Builder;

@Builder
public record PurchaseResDto(
        Long memberObjectId,
        Object object,
        boolean status
) {
    public static PurchaseResDto of(MemberObject memberObject) {
        return PurchaseResDto.builder()
                .memberObjectId(memberObject.getMemberObjectId())
                .object(memberObject.getObject())
                .status(memberObject.isStatus())
                .build();
    }
}

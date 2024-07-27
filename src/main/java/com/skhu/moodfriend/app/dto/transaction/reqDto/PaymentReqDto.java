package com.skhu.moodfriend.app.dto.transaction.reqDto;

import com.skhu.moodfriend.app.entity.member.order.PaymentPlatform;

public record PaymentReqDto(
        Integer mileage,
        Integer price,
        PaymentPlatform platform
) {
}

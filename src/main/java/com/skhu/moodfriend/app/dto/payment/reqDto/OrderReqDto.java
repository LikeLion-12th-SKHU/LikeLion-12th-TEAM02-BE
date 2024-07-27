package com.skhu.moodfriend.app.dto.payment.reqDto;

import com.skhu.moodfriend.app.entity.member.order.PaymentPlatform;

public record OrderReqDto(
        String objectName,
        PaymentPlatform platform
) {
}

package com.skhu.moodfriend.app.dto.payment.reqDto;

import com.skhu.moodfriend.app.entity.member.order.PaymentPlatform;
import com.skhu.moodfriend.app.entity.object_store.ObjectName;

public record OrderReqDto(
        ObjectName objectName,
        Integer amount,
        PaymentPlatform platform
) {
}

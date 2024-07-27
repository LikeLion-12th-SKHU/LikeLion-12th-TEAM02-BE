package com.skhu.moodfriend.app.dto.payment.resDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.skhu.moodfriend.app.entity.member.order.OrderStatus;
import com.skhu.moodfriend.app.entity.member.order.PaymentPlatform;
import com.skhu.moodfriend.app.entity.object_store.ObjectName;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record OrderResDto(
        Long orderId,
        ObjectName objectName,
        PaymentPlatform platform,
        OrderStatus status,
        @JsonFormat(pattern = "yyyy-MM-dd - HH:mm", timezone = "Asia/Seoul")
        LocalDateTime orderAt
) {
}

package com.skhu.moodfriend.app.dto.transaction.resDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.skhu.moodfriend.app.entity.member.order.OrderStatus;
import com.skhu.moodfriend.app.entity.member.order.PaymentPlatform;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record PaymentResDto(
        Long orderId,
        Integer chargedMileage,
        Integer totalMileage,
        Integer price,
        PaymentPlatform platform,
        OrderStatus status,
        @JsonFormat(pattern = "yyyy-MM-dd - HH:mm", timezone = "Asia/Seoul")
        LocalDateTime orderAt
) {
}

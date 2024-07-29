package com.skhu.moodfriend.app.dto.payment.resDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.skhu.moodfriend.app.domain.payment.Order;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record OrderResDto(
        Long orderId,
        String productName,
        int price,
        String impUid,
        String merchantUid,
        @JsonFormat(pattern = "yyyy-MM-dd - HH:mm", timezone = "Asia/Seoul")
        LocalDateTime createdAt
) {
    public static OrderResDto of(Order order) {
        return OrderResDto.builder()
                .orderId(order.getOrderId())
                .productName(order.getProductName())
                .price(order.getPrice())
                .impUid(order.getImpUid())
                .merchantUid(order.getMerchantUid())
                .createdAt(order.getCreatedAt())
                .build();
    }
}

package com.skhu.moodfriend.app.dto.order.resDto;

import lombok.Builder;

@Builder
public record OrderResDto(
        Long orderId,
        String productName,
        int price,
        String impUid,
        String merchantUid,
        int updatedMileage
) {
}

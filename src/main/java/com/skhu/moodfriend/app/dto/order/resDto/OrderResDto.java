package com.skhu.moodfriend.app.dto.order.resDto;

import lombok.Builder;

@Builder
public record OrderResDto(
        Long productId,
        String productName,
        int price,
        String impUid,
        String merchantUid
) {
}

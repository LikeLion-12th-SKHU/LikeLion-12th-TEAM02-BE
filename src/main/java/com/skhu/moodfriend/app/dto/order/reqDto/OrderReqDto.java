package com.skhu.moodfriend.app.dto.order.reqDto;

public record OrderReqDto(
        String productName,
        int price,
        String impUid,
        String merchantUid,
        int mileageIncrement
) {
}

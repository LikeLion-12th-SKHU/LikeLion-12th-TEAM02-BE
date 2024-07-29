package com.skhu.moodfriend.app.dto.payment.reqDto;

import com.skhu.moodfriend.app.domain.member.Member;
import com.skhu.moodfriend.app.domain.payment.Order;

public record OrderReqDto(
        String productName,
        int price,
        String impUid,
        String merchantUid,
        int mileageIncrement
) {
    public Order toEntity(Member member) {
        return Order.builder()
                .productName(this.productName)
                .price(this.price)
                .impUid(this.impUid)
                .merchantUid(this.merchantUid)
                .member(member)
                .build();
    }
}

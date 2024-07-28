package com.skhu.moodfriend.app.dto.order;

import com.skhu.moodfriend.app.entity.member.order.Order;
import lombok.Getter;

@Getter
public class OrderDto {
    Long productId;
    String productName;
    int price;
    String impUid;
    String merchantUid;

    public Order toEntity() {
        return Order.builder()
                .productId(productId)
                .productName(productName)
                .price(price)
                .impUid(impUid)
                .merchantUid(merchantUid)
                .build();
    }
}

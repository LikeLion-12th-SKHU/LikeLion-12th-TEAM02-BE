package com.skhu.moodfriend.app.entity.member.order;

import com.skhu.moodfriend.app.entity.member.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ORDER_ID")
    private Long orderId;

    @Column(name = "PRODUCT_ID")
    private Long productId;

    @Column(name = "PRODUCT_NAME")
    private String productName;

    @Column(name = "PRICE")
    private int price;

    @Column(name = "IMP_UID")
    private String impUid;

    @Column(name = "MERCHANT_UID")
    private String merchantUid;

    @CreatedDate
    @Column(name = "CREATED_AT", updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @Builder
    private Order(Long productId, String productName, int price, String impUid, String merchantUid, Member member) {
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.impUid = impUid;
        this.merchantUid = merchantUid;
        this.member = member;
    }
}

package com.skhu.moodfriend.app.domain.payment;

import com.skhu.moodfriend.app.domain.member.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ORDER_ID")
    private Long orderId;

    @Column(name = "PRODUCT_NAME", nullable = false)
    private String productName;

    @Column(name = "PRICE", nullable = false)
    private int price;

    @Column(name = "IMP_UID", nullable = false)
    private String impUid;

    @Column(name = "MERCHANT_UID", nullable = false)
    private String merchantUid;

    @CreatedDate
    @Column(name = "CREATED_AT", updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @Builder
    private Order(String productName, int price, String impUid, String merchantUid, Member member) {
        this.productName = productName;
        this.price = price;
        this.impUid = impUid;
        this.merchantUid = merchantUid;
        this.member = member;
    }
}

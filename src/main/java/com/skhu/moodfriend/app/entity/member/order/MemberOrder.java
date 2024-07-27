package com.skhu.moodfriend.app.entity.member.order;

import com.skhu.moodfriend.app.entity.member.Member;
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
public class MemberOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ORDER_ID")
    private Long orderId;

    @Column(name = "ORDER_MILEAGE")
    private Integer mileage;

    @Column(name = "ORDER_AMOUNT")
    private Integer amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "PLATFORM")
    private PaymentPlatform platform;

    @Column(name = "ORDER_STATUS", nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @CreatedDate
    @Column(name = "ORDER_DATE", updatable = false)
    private LocalDateTime orderAt;

    @Column(name = "IMP_UID")
    private String impUid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @Builder
    private MemberOrder(Integer mileage, Integer amount, PaymentPlatform platform, OrderStatus status, Member member) {
        this.mileage = mileage;
        this.amount = amount;
        this.platform = platform;
        this.status = status;
        this.member = member;
    }

    public void completePayment(String impUid) {
        this.status = OrderStatus.PAID;
        this.impUid = impUid;
    }

    public void failPayment(String impUid) {
        this.status = OrderStatus.FAILED;
        this.impUid = impUid;
    }
}
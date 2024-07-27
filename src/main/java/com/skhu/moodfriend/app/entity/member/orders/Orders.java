package com.skhu.moodfriend.app.entity.member.orders;

import com.skhu.moodfriend.app.entity.member.Member;
import com.skhu.moodfriend.app.entity.object_store.ObjectStore;
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
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ORDER_ID")
    private Long orderId;

    @Column(name = "OBJECT_NAME", nullable = false)
    private String objectName;

    @Column(name = "ORDER_STATUS", nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @CreatedDate
    @Column(name = "ORDER_DATE", updatable = false)
    private LocalDateTime orderAt;

    @Column(name = "IMP_UID")
    private String impUid;

    @Column(name = "PAYMENT_INFO")
    private String paymentInfo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @Builder
    private Orders(String objectName, OrderStatus status, Member member) {
        this.objectName = objectName;
        this.status = status;
        this.member = member;
    }

    public void completePayment(String impUid, String paymentInfo) {
        this.status = OrderStatus.PAID;
        this.impUid = impUid;
        this.paymentInfo = paymentInfo;
    }
}
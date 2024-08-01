package com.skhu.moodfriend.app.domain.member.object;

import com.skhu.moodfriend.app.domain.store.Object;
import com.skhu.moodfriend.app.domain.member.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class MemberObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEMBER_OBJECT_ID")
    private Long memberObjectId;

    @Enumerated(EnumType.STRING)
    @Column(name = "MEMBER_OBJECT", nullable = false)
    private Object object;

    @Column(name = "OBJECT_STATUS", nullable = false)
    private boolean status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @Builder
    private MemberObject(Object object, boolean status, Member member) {
        this.object = object;
        this.status = status;
        this.member = member;
    }
}

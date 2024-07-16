package com.skhu.moodfriend.app.entity.member_object;

import com.skhu.moodfriend.app.entity.object_store.ObjectStore;
import com.skhu.moodfriend.app.entity.member.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
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
    @Column(name = "USER_OBJECT_ID")
    private Long userObjectId;

    @Column(name = "USER_OBJECT_STATUS", nullable = false)
    private boolean status = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "OBJECT_ID")
    private ObjectStore objectStore;
}

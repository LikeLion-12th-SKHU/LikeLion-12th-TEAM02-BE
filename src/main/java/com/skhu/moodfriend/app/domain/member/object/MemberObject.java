package com.skhu.moodfriend.app.domain.member.object;

import com.skhu.moodfriend.app.domain.member.Member;
import com.skhu.moodfriend.app.domain.store.ObjectStore;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "OBJECT_STORE_ID", nullable = false)
    private ObjectStore objectStore;

    @Column(name = "OBJECT_STATUS", nullable = false)
    private boolean status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID", nullable = false)
    private Member member;

    @Builder
    public MemberObject(ObjectStore objectStore, boolean status, Member member) {
        this.objectStore = objectStore;
        this.status = status;
        this.member = member;
    }
}

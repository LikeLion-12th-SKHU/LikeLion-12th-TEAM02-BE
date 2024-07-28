package com.skhu.moodfriend.app.entity.member.object;

import com.skhu.moodfriend.app.entity.object_store.ObjectName;
import com.skhu.moodfriend.app.entity.object_store.ObjectStore;
import com.skhu.moodfriend.app.entity.member.Member;
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
    @Column(name = "OBJECT_NAME", nullable = false)
    private ObjectName objectName;

    @Column(name = "OBJECT_STATUS", nullable = false)
    private boolean status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @Builder
    private MemberObject(ObjectName objectName, boolean status, Member member) {
        this.objectName = objectName;
        this.status = status;
        this.member = member;
    }
}

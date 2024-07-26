package com.skhu.moodfriend.app.entity.friend;

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
public class Friend {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FRIEND_ID")
    private Long friendId;

    @Column(name = "RECEIVER_EMAIL", nullable = false, length = 100)
    private String receiverEmail;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", nullable = false)
    private Status status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    // 필드 초기화를 위한 생성자
    public Friend(Member member, String receiverEmail, Status status) {
        this.member = member;
        this.receiverEmail = receiverEmail;
        this.status = status;
    }
}

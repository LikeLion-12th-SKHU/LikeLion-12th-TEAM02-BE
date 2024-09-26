package com.skhu.moodfriend.app.domain.friend;

import com.skhu.moodfriend.app.domain.member.Member;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "REQUESTER_ID")
    private Member requester;

    private Friend(String receiverEmail, Member member, Member requester) {
        this.receiverEmail = receiverEmail;
        this.status = Status.WAITING;
        this.member = member;
        this.requester = requester;
    }

    public static Friend createFriendRequest(String receiverEmail, Member member, Member requester) {
        return new Friend(receiverEmail, member, requester);
    }

    public void acceptRequest() {
        this.status = Status.ACCEPTED;
    }

    public void rejectedRequest() {
        this.status = Status.REJECTED;
    }
}

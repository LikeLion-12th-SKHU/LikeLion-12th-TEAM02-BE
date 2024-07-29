package com.skhu.moodfriend.app.domain.member.feedback;

import com.skhu.moodfriend.app.domain.member.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class FeedBack {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FEED_BACK_ID")
    private Long feedBackId;

    @Column(name = "FEED_BACK_TITLE", nullable = false)
    private String title;

    @Column(name = "FEED_BACK_CONTENT", length = 1024)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;
}

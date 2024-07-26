package com.skhu.moodfriend.app.entity.diary_ai;

import com.skhu.moodfriend.app.entity.member.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class DiaryAI {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DIARY_AI_ID")
    private Long diaryAIId;

    @CreatedDate
    @Column(name = "DIARY_AI_CREATED_AT", updatable = false)
    private LocalDate createdAt;

    @Column(name = "DIARY_AI_SUMMARY", length = 1024)
    private String summary;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @Builder
    private DiaryAI(String summary, Member member) {
        this.summary = summary;
        this.member = member;
    }
}

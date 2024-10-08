package com.skhu.moodfriend.app.domain.tracker.diary;

import com.skhu.moodfriend.app.domain.member.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Diary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DIARY_ID")
    private Long diaryId;

    @Enumerated(EnumType.STRING)
    @Column(name = "EMOTION_TYPE", nullable = false)
    private EmotionType emotionType;

    @Enumerated(EnumType.STRING)
    @Column(name = "WEATHER_TYPE", nullable = false)
    private WeatherType weatherType;

    @Column(name = "DIARY_TITLE", length = 30, nullable = false)
    private String title;

    @Column(name = "DIARY_CONTENT", length = 1024, nullable = false)
    private String content;

    @Column(name = "DIARY_CREATED_AT", nullable = false)
    private LocalDate createdAt;

    @LastModifiedDate
    @Column(name = "DIARY_UPDATED_AT")
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @Builder
    private Diary(EmotionType emotionType, WeatherType weatherType, String title, String content, LocalDate createdAt, Member member) {
        this.emotionType = emotionType;
        this.weatherType = weatherType;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.member = member;
    }

    public void update(EmotionType emotionType, WeatherType weatherType, String title, String content, LocalDate createdAt) {
        this.emotionType = emotionType;
        this.weatherType = weatherType;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
    }
}


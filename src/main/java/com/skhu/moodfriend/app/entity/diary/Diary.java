package com.skhu.moodfriend.app.entity.diary;

import com.skhu.moodfriend.app.entity.tracker.Tracker;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

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

    @Column(name = "DIARY_CONTENT", length = 1024)
    private String content;

    @CreatedDate
    @Column(name = "DIARY_CREATED_AT", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "DIARY_UPDATED_AT")
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TRACKER_ID")
    private Tracker tracker;

    @Builder
    private Diary(EmotionType emotionType, WeatherType weatherType, String content, Tracker tracker) {
        this.emotionType = emotionType;
        this.weatherType = weatherType;
        this.content = content;
        this.tracker = tracker;
    }
}


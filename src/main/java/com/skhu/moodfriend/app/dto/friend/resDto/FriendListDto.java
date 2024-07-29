package com.skhu.moodfriend.app.dto.friend.resDto;


import com.skhu.moodfriend.app.entity.tracker.diary.EmotionType;
import lombok.Builder;

@Builder
public record FriendListDto(

        Long memberId,
        String name,

        String email,
        EmotionType emotionType
) {
}
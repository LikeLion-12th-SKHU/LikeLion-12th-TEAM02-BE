package com.skhu.moodfriend.app.dto.friend.resDto;

import com.skhu.moodfriend.app.domain.friend.Friend;
import com.skhu.moodfriend.app.domain.tracker.diary.EmotionType;
import lombok.Builder;

@Builder
public record FriendResDto(
        Long memberId,
        String name,
        String email,
        EmotionType emotionType
) {
    public static FriendResDto of(Friend friend, EmotionType emotionType) {
        return FriendResDto.builder()
                .memberId(friend.getMember().getMemberId())
                .name(friend.getMember().getName())
                .email(friend.getMember().getEmail())
                .emotionType(emotionType)
                .build();
    }
}

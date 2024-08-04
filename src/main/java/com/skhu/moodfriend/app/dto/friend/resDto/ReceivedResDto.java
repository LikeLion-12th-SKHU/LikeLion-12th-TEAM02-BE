package com.skhu.moodfriend.app.dto.friend.resDto;

import com.skhu.moodfriend.app.domain.friend.Friend;
import lombok.Builder;

@Builder
public record ReceivedResDto(
        Long memberId,
        String name,
        String email
) {
    public static ReceivedResDto of(Friend friend) {
        return ReceivedResDto.builder()
                .memberId(friend.getRequester().getMemberId())
                .name(friend.getRequester().getName())
                .email(friend.getRequester().getEmail())
                .build();
    }
}

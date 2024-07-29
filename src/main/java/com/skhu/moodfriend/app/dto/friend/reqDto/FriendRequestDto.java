package com.skhu.moodfriend.app.dto.friend.reqDto;


import com.skhu.moodfriend.app.entity.friend.Status;
import lombok.Builder;

@Builder
public record FriendRequestDto(

        String senderMemberId,
        String senderEmail, // 친구 요청을 보낸 사람의 이메일
        String senderName, // 친구 요청을 보낸 사람의 이름
        Status status ,
        Long friendId


) {
}
package com.skhu.moodfriend.app.controller.friend;

import com.skhu.moodfriend.app.service.friend.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/friends")
public class FriendController {
    @Autowired
    private FriendService friendService;

    @PostMapping("/add")
    public ResponseEntity<?> addFriend(@RequestParam Long memberId, @RequestParam String receiverEmail) {
        System.out.println("Received request to add friend: memberId=" + memberId + ", receiverEmail=" + receiverEmail);
        friendService.sendFriendRequest(memberId, receiverEmail);
        return ResponseEntity.ok("친구 요청이 전송되었습니다.");
    }
}

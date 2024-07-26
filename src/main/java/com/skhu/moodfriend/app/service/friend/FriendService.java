package com.skhu.moodfriend.app.service.friend;

import com.skhu.moodfriend.app.entity.friend.Friend;
import com.skhu.moodfriend.app.entity.friend.Status;
import com.skhu.moodfriend.app.entity.member.Member;
import com.skhu.moodfriend.app.repository.FriendRepository;
import com.skhu.moodfriend.app.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FriendService {
    @Autowired
    private FriendRepository friendRepository;

    @Autowired
    private MemberRepository memberRepository;

    public void sendFriendRequest(Long memberId, String receiverEmail) {
        System.out.println("Checking if member exists with ID: " + memberId);
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid member ID: " + memberId));
        Friend request = new Friend(member, receiverEmail, Status.WAITING);
        friendRepository.save(request);
    }
}

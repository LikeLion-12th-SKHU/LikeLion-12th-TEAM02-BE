package com.skhu.moodfriend.app.repository;

import com.skhu.moodfriend.app.entity.friend.Friend;
import com.skhu.moodfriend.app.entity.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import com.skhu.moodfriend.app.entity.friend.*;

import java.util.List;

public interface FriendRepository extends JpaRepository<Friend, Long> {
    List<Friend> findByMember(Member member);

    List<Friend> findByMemberAndStatus(Member member, Status status);
}
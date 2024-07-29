package com.skhu.moodfriend.app.repository;

import com.skhu.moodfriend.app.domain.friend.Friend;
import com.skhu.moodfriend.app.domain.friend.Status;
import com.skhu.moodfriend.app.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendRepository extends JpaRepository<Friend, Long> {
    boolean existsByRequesterAndMemberAndStatus(Member requester, Member member, Status status);

    @Query("SELECT f FROM Friend f WHERE f.requester = :requester AND f.member = :member AND f.status = :status")
    Optional<Friend> findByRequesterAndMemberAndStatus(Member requester, Member member, Status status);

    @Query("SELECT f FROM Friend f WHERE f.requester = :requester AND f.status = :status")
    List<Friend> findByRequesterAndStatus(Member requester, Status status);
}

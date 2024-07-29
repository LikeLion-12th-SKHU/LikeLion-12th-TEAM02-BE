package com.skhu.moodfriend.app.repository;

import com.skhu.moodfriend.app.domain.friend.Friend;
import com.skhu.moodfriend.app.domain.friend.Status;
import com.skhu.moodfriend.app.domain.member.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FriendRepository extends JpaRepository<Friend,Long> {
    boolean existsByRequesterAndMemberAndStatus(Member requester, Member member, Status status);


    @Query("SELECT f FROM Friend f JOIN FETCH f.requester m where m.memberId=:memberId and f.status=:status")
    Page<Friend> findByMemberAndStatus(PageRequest pageRequest, Long memberId, Status status);

    @Query("SELECT f FROM Friend f JOIN FETCH f.member m JOIN FETCH f.requester r where m.memberId=:requestedMemberId  and r.memberId=:memberId and f.status=:waiting")
    Optional<Friend> findByRequesterAndMemberAndStatus(Long memberId, Long requestedMemberId, Status waiting);


    @Query("SELECT f FROM Friend f JOIN FETCH f.member m  JOIN FETCH f.requester r LEFT JOIN FETCH m.diaries d where r.memberId=:memberId  ORDER BY m.name ASC")
    Page<Friend> findFriendsByMember(Long memberId, Pageable pageable);


    @Query("SELECT f FROM Friend f JOIN FETCH f.member m JOIN FETCH f.requester r where m.memberId=:friendId and r.memberId=:memberId")
    Optional<Friend> findByMemberIdAndFriendId(Long memberId, Long friendId);
}

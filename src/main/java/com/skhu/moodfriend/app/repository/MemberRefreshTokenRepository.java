package com.skhu.moodfriend.app.repository;

import com.skhu.moodfriend.app.entity.member.Member;
import com.skhu.moodfriend.app.entity.member.MemberRefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRefreshTokenRepository extends JpaRepository<MemberRefreshToken, Long> {

    Optional<MemberRefreshToken> findByMember_MemberId(Long memberSeq);

    void deleteByMember(Member member);
}

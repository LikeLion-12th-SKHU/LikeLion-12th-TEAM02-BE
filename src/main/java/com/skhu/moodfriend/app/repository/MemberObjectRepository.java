package com.skhu.moodfriend.app.repository;

import com.skhu.moodfriend.app.domain.member.Member;
import com.skhu.moodfriend.app.domain.member.object.MemberObject;
import com.skhu.moodfriend.app.domain.store.ObjectEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberObjectRepository extends JpaRepository<MemberObject, Long> {

    boolean existsByMemberAndObject(Member member, ObjectEnum objectEnum);
}

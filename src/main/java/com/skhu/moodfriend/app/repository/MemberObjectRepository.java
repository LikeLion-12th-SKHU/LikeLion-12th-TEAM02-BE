package com.skhu.moodfriend.app.repository;

import com.skhu.moodfriend.app.entity.member.Member;
import com.skhu.moodfriend.app.entity.member.object.MemberObject;
import com.skhu.moodfriend.app.entity.object_store.ObjectName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberObjectRepository extends JpaRepository<MemberObject, Long> {

    Optional<MemberObject> findByObjectNameAndMember(ObjectName objectName, Member member);
}

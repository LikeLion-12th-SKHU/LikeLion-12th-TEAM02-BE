package com.skhu.moodfriend.app.repository;

import com.skhu.moodfriend.app.entity.member.object.MemberObject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberObjectRepository extends JpaRepository<MemberObject, Long> {
}

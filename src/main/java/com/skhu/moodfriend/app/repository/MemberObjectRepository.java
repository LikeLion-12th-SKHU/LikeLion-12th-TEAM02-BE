package com.skhu.moodfriend.app.repository;

import com.skhu.moodfriend.app.domain.member.object.MemberObject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface MemberObjectRepository extends JpaRepository<MemberObject, Long> {



    @Query("SELECT mo FROM MemberObject mo JOIN FETCH mo.objectStore os JOIN FETCH mo.member m where m.memberId=:memberId")
    List<MemberObject> findByMemberId(Long memberId);



    @Modifying
    @Transactional
    @Query("UPDATE MemberObject mo " +
            "SET mo.status = CASE WHEN mo.memberObjectId = :memberObjectId THEN TRUE ELSE FALSE END " +
            "WHERE mo.member.memberId = :memberId")
    void updateMemberObjectStatus(Long memberId, Long memberObjectId);
}

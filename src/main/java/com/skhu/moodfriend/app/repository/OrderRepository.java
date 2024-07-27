package com.skhu.moodfriend.app.repository;

import com.skhu.moodfriend.app.entity.member.order.MemberOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<MemberOrder, Long> {

    Optional<MemberOrder> findByImpUid(String impUid);
}

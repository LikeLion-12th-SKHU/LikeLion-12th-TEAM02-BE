package com.skhu.moodfriend.app.repository;

import com.skhu.moodfriend.app.entity.member.Member;
import com.skhu.moodfriend.app.entity.member.order.MemberOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<MemberOrder, Long> {

    List<MemberOrder> findByMember(Member member);
}

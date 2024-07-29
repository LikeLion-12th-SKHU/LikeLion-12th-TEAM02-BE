package com.skhu.moodfriend.app.repository;

import com.skhu.moodfriend.app.domain.member.Member;
import com.skhu.moodfriend.app.domain.payment.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByMember(Member member);
}

package com.skhu.moodfriend.app.repository;

import com.skhu.moodfriend.app.entity.member.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}

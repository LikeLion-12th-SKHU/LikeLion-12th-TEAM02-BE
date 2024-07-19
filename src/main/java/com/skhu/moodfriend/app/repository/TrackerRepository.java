package com.skhu.moodfriend.app.repository;

import com.skhu.moodfriend.app.entity.member.Member;
import com.skhu.moodfriend.app.entity.tracker.Tracker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TrackerRepository extends JpaRepository<Tracker, Long> {

    Optional<Tracker> findByMember(Member member);
}

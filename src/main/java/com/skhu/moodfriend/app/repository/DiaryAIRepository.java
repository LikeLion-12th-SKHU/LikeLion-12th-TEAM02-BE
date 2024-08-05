package com.skhu.moodfriend.app.repository;

import com.skhu.moodfriend.app.domain.member.Member;
import com.skhu.moodfriend.app.domain.tracker.diary_ai.DiaryAI;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DiaryAIRepository extends JpaRepository<DiaryAI, Long> {

    List<DiaryAI> findAllByCreatedAt(LocalDate createdAt);

    @Query("SELECT d FROM DiaryAI d WHERE d.member = :member ORDER BY d.createdAt DESC")
    Optional<DiaryAI> findTopByMemberOrderByCreatedAtDesc(Member member);

    Optional<DiaryAI> findByMemberAndCreatedAt(Member member, LocalDate createdAt);
}

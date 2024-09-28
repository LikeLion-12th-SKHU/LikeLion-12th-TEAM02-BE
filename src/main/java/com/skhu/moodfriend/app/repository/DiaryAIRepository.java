package com.skhu.moodfriend.app.repository;

import com.skhu.moodfriend.app.domain.member.Member;
import com.skhu.moodfriend.app.domain.tracker.diary_ai.DiaryAI;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface DiaryAIRepository extends JpaRepository<DiaryAI, Long> {

    Optional<DiaryAI> findByMemberAndCreatedAt(Member member, LocalDate createdAt);
}

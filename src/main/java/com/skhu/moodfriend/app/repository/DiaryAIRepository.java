package com.skhu.moodfriend.app.repository;

import com.skhu.moodfriend.app.entity.diary_ai.DiaryAI;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface DiaryAIRepository extends JpaRepository<DiaryAI, Long> {

    List<DiaryAI> findAllByCreatedAt(LocalDate createdAt);
}

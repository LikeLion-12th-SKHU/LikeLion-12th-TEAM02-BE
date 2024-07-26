package com.skhu.moodfriend.app.repository;

import com.skhu.moodfriend.app.entity.diary_ai.DiaryAI;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiaryAIRepository extends JpaRepository<DiaryAI, Long> {
}

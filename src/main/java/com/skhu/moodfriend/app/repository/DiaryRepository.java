package com.skhu.moodfriend.app.repository;

import com.skhu.moodfriend.app.entity.diary.Diary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiaryRepository extends JpaRepository<Diary, Long> {

}

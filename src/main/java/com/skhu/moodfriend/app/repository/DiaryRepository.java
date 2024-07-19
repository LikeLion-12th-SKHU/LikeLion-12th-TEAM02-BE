package com.skhu.moodfriend.app.repository;

import com.skhu.moodfriend.app.entity.diary.Diary;
import com.skhu.moodfriend.app.entity.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface DiaryRepository extends JpaRepository<Diary, Long> {

    @Query("SELECT CASE WHEN COUNT(d) > 0 THEN true ELSE false END " +
            "FROM Diary d " +
            "WHERE d.tracker.member = :member " +
            "AND d.createdAt = :date")
    boolean existsByMemberAndCreatedAtDate(@Param("member") Member member, @Param("date") LocalDate date);
}

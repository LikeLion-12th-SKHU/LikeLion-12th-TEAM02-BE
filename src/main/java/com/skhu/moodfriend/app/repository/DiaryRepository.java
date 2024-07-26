package com.skhu.moodfriend.app.repository;

import com.skhu.moodfriend.app.entity.diary.Diary;
import com.skhu.moodfriend.app.entity.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DiaryRepository extends JpaRepository<Diary, Long> {

    @Query("SELECT CASE WHEN COUNT(d) > 0 THEN true ELSE false END " +
            "FROM Diary d " +
            "WHERE d.member = :member " +
            "AND d.createdAt = :date")
    boolean existsByMemberAndCreatedAtDate(@Param("member") Member member, @Param("date") LocalDate date);

    List<Diary> findByMember(Member member);

    @Query(value = "SELECT d FROM Diary d " +
            "WHERE d.member = :member " +
            "AND FUNCTION('YEAR', d.createdAt) = :year " +
            "AND FUNCTION('MONTH', d.createdAt) = :month " +
            "ORDER BY d.createdAt ASC")
    List<Diary> findMonthlyDailyEmotions(
            @Param("member") Member member,
            @Param("year") int year,
            @Param("month") int month
    );
}

package com.skhu.moodfriend.app.repository;

import com.skhu.moodfriend.app.entity.attendance.Attendance;
import com.skhu.moodfriend.app.entity.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    Optional<Attendance> findByMemberAndAttendanceDate(Member member, LocalDate attendanceDate);
}

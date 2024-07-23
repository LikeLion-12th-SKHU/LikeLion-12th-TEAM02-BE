package com.skhu.moodfriend.app.service.member;

import com.skhu.moodfriend.app.entity.attendance.Attendance;
import com.skhu.moodfriend.app.entity.member.Member;
import com.skhu.moodfriend.app.repository.AttendanceRepository;
import com.skhu.moodfriend.app.repository.MemberRepository;
import com.skhu.moodfriend.global.exception.CustomException;
import com.skhu.moodfriend.global.exception.code.ErrorCode;
import com.skhu.moodfriend.global.exception.code.SuccessCode;
import com.skhu.moodfriend.global.template.ApiResponseTemplate;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class AttendanceService {

    private static final long MILEAGE_INCREMENT = 3;

    private final AttendanceRepository attendanceRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public ApiResponseTemplate<String> recordAttendance(Principal principal) {
        Long memberId = Long.parseLong(principal.getName());

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER_EXCEPTION, ErrorCode.NOT_FOUND_MEMBER_EXCEPTION.getMessage()));

        LocalDate today = LocalDate.now();

        Optional<Attendance> attendance = attendanceRepository.findByMemberAndAttendanceDate(member, today);
        if (attendance.isPresent()) {
            throw new CustomException(ErrorCode.ALREADY_EXIST_ATTENDED_EXCEPTION, ErrorCode.ALREADY_EXIST_ATTENDED_EXCEPTION.getMessage());
        }

        Attendance newAttendance = Attendance.builder()
                .member(member)
                .build();
        attendanceRepository.save(newAttendance);

        member.updateMileage(MILEAGE_INCREMENT);
        memberRepository.save(member);

        return ApiResponseTemplate.success(SuccessCode.ATTENDANCE_SUCCESS, SuccessCode.ATTENDANCE_SUCCESS.getMessage());
    }
}

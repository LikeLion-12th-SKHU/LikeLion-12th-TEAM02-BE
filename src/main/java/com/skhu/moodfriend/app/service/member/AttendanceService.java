package com.skhu.moodfriend.app.service.member;

import com.skhu.moodfriend.app.dto.member.resDto.MemberInfoResDto;
import com.skhu.moodfriend.app.domain.member.attendance.Attendance;
import com.skhu.moodfriend.app.domain.member.Member;
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

    private static final Integer MILEAGE_INCREMENT = 3;

    private final AttendanceRepository attendanceRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public ApiResponseTemplate<MemberInfoResDto> recordAttendance(Principal principal) {
        Long memberId = Long.parseLong(principal.getName());

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER_EXCEPTION, ErrorCode.NOT_FOUND_MEMBER_EXCEPTION.getMessage()));

        Optional<Attendance> attendance = attendanceRepository.findByMemberAndAttendanceDate(member, LocalDate.now());
        if (attendance.isPresent()) {
            throw new CustomException(ErrorCode.ALREADY_EXIST_ATTENDED_EXCEPTION, ErrorCode.ALREADY_EXIST_ATTENDED_EXCEPTION.getMessage());
        }

        Attendance newAttendance = Attendance.builder()
                .member(member)
                .build();
        attendanceRepository.save(newAttendance);

        member.updateMileage(MILEAGE_INCREMENT);
        memberRepository.save(member);

        MemberInfoResDto resDto = MemberInfoResDto.builder()
                .email(member.getEmail())
                .name(member.getName())
                .mileage(member.getMileage())
                .loginType(member.getLoginType().getDisplayName())
                .build();

        return ApiResponseTemplate.success(SuccessCode.ATTENDANCE_SUCCESS, resDto);
    }
}

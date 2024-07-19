package com.skhu.moodfriend.app.service.tracker;

import com.skhu.moodfriend.app.dto.tracker.reqDto.DiaryCreateReqDto;
import com.skhu.moodfriend.app.dto.tracker.resDto.DiaryCreateResDto;
import com.skhu.moodfriend.app.entity.diary.Diary;
import com.skhu.moodfriend.app.entity.member.Member;
import com.skhu.moodfriend.app.entity.tracker.Tracker;
import com.skhu.moodfriend.app.repository.DiaryRepository;
import com.skhu.moodfriend.app.repository.MemberRepository;
import com.skhu.moodfriend.app.repository.TrackerRepository;
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

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class DiaryCreateService {

    private final TrackerRepository trackerRepository;
    private final DiaryRepository diaryRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public ApiResponseTemplate<DiaryCreateResDto> createDiary(
            DiaryCreateReqDto reqDto,
            Principal principal) {

        Long memberId = Long.parseLong(principal.getName());

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER_EXCEPTION, ErrorCode.NOT_FOUND_MEMBER_EXCEPTION.getMessage()));

        LocalDate createdAt = reqDto.createdAt();
        LocalDate now = LocalDate.now();

        if (createdAt.isAfter(now)) {
            throw new CustomException(ErrorCode.INVALID_DATE_EXCEPTION, ErrorCode.INVALID_DATE_EXCEPTION.getMessage());
        }

        boolean exists = diaryRepository.existsByMemberAndCreatedAtDate(member, createdAt);
        if (exists) {
            throw new CustomException(ErrorCode.ALREADY_EXIST_DIARY_EXCEPTION, ErrorCode.ALREADY_EXIST_DIARY_EXCEPTION.getMessage());
        }

        if (reqDto.content().length() > 1024) {
            throw new CustomException(ErrorCode.CONTENT_LENGTH_EXCEEDED, ErrorCode.CONTENT_LENGTH_EXCEEDED.getMessage());
        }

        Tracker tracker = trackerRepository.findByMember(member)
                .orElseGet(() -> trackerRepository.save(Tracker.builder().member(member).build()));

        Diary diary = Diary.builder()
                .emotionType(reqDto.emotionType())
                .weatherType(reqDto.weatherType())
                .content(reqDto.content())
                .createdAt(createdAt)
                .tracker(tracker)
                .build();

        diaryRepository.save(diary);

        DiaryCreateResDto resDto = DiaryCreateResDto.builder()
                .emotionType(diary.getEmotionType().getDisplayName())
                .weatherType(diary.getWeatherType().getDisplayName())
                .content(diary.getContent())
                .createdAt(diary.getCreatedAt())
                .updatedAt(diary.getUpdatedAt())
                .build();

        return ApiResponseTemplate.success(SuccessCode.CREATE_DIARY_SUCCESS, resDto);
    }
}

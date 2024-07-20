package com.skhu.moodfriend.app.service.tracker;

import com.skhu.moodfriend.app.dto.tracker.reqDto.DiaryCreateReqDto;
import com.skhu.moodfriend.app.dto.tracker.resDto.DiaryResDto;
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
    public ApiResponseTemplate<DiaryResDto> createDiary(
            DiaryCreateReqDto reqDto,
            Principal principal) {

        Long memberId = Long.parseLong(principal.getName());

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER_EXCEPTION, ErrorCode.NOT_FOUND_MEMBER_EXCEPTION.getMessage()));

        LocalDate createdAt = reqDto.createdAt();

        boolean exists = diaryRepository.existsByMemberAndCreatedAtDate(member, createdAt);
        if (exists) {
            throw new CustomException(ErrorCode.ALREADY_EXIST_DIARY_EXCEPTION, ErrorCode.ALREADY_EXIST_DIARY_EXCEPTION.getMessage());
        }

        Tracker tracker = trackerRepository.findByMember(member)
                .orElseGet(() -> trackerRepository.save(Tracker.builder().member(member).build()));

        Diary diary = Diary.builder()
                .emotionType(reqDto.emotionType())
                .weatherType(reqDto.weatherType())
                .title(reqDto.title())
                .content(reqDto.content())
                .createdAt(createdAt)
                .tracker(tracker)
                .build();

        diaryRepository.save(diary);

        DiaryResDto resDto = DiaryResDto.builder()
                .emotionType(diary.getEmotionType())
                .weatherType(diary.getWeatherType())
                .title(diary.getTitle())
                .content(diary.getContent())
                .createdAt(diary.getCreatedAt())
                .updatedAt(diary.getUpdatedAt())
                .build();

        return ApiResponseTemplate.success(SuccessCode.CREATE_DIARY_SUCCESS, resDto);
    }
}

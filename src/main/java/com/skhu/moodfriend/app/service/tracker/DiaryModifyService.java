package com.skhu.moodfriend.app.service.tracker;

import com.skhu.moodfriend.app.dto.tracker.reqDto.DiaryUpdateReqDto;
import com.skhu.moodfriend.app.dto.tracker.resDto.DiaryResDto;
import com.skhu.moodfriend.app.entity.diary.Diary;
import com.skhu.moodfriend.app.entity.member.Member;
import com.skhu.moodfriend.app.repository.DiaryRepository;
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

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class DiaryModifyService {

    private final DiaryRepository diaryRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public ApiResponseTemplate<DiaryResDto> updateDiary(
            DiaryUpdateReqDto reqDto,
            Principal principal) {

        Long memberId = Long.parseLong(principal.getName());

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER_EXCEPTION, ErrorCode.NOT_FOUND_MEMBER_EXCEPTION.getMessage()));

        LocalDate createdAt = reqDto.createdAt();
        Diary diary = diaryRepository.findByCreatedAtAndTrackerMember(createdAt, member)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_DIARY_EXCEPTION, ErrorCode.NOT_FOUND_DIARY_EXCEPTION.getMessage()));

        diary.update(reqDto.emotionType(), reqDto.weatherType(), reqDto.title(), reqDto.content());
        diaryRepository.save(diary);

        DiaryResDto resDto = DiaryResDto.builder()
                .emotionType(diary.getEmotionType())
                .weatherType(diary.getWeatherType())
                .title(diary.getTitle())
                .content(diary.getContent())
                .createdAt(diary.getCreatedAt())
                .updatedAt(diary.getUpdatedAt())
                .build();

        return ApiResponseTemplate.success(SuccessCode.UPDATE_DIARY_SUCCESS, resDto);
    }

    @Transactional
    public ApiResponseTemplate<Void> deleteDiary(
            Long diaryId,
            Principal principal) {

        Long memberId = Long.parseLong(principal.getName());

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER_EXCEPTION, ErrorCode.NOT_FOUND_MEMBER_EXCEPTION.getMessage()));

        Diary diary = diaryRepository.findById(diaryId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_DIARY_EXCEPTION, ErrorCode.NOT_FOUND_DIARY_EXCEPTION.getMessage()));

        if (!diary.getTracker().getMember().equals(member)) {
            throw new CustomException(ErrorCode.ONLY_OWN_DIARY_ACCESS_EXCEPTION, ErrorCode.ONLY_OWN_DIARY_ACCESS_EXCEPTION.getMessage());
        }

        diaryRepository.delete(diary);

        return ApiResponseTemplate.success(SuccessCode.DELETE_DIARY_SUCCESS, null);
    }
}

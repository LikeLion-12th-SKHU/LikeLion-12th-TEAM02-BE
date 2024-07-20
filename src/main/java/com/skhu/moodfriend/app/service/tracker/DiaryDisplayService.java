package com.skhu.moodfriend.app.service.tracker;

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
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class DiaryDisplayService {

    private final DiaryRepository diaryRepository;
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public ApiResponseTemplate<DiaryResDto> getDiaryById(
            Long diaryId, Principal principal) {

        Long memberId = Long.parseLong(principal.getName());

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER_EXCEPTION, ErrorCode.NOT_FOUND_MEMBER_EXCEPTION.getMessage()));

        Diary diary = diaryRepository.findById(diaryId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_DIARY_EXCEPTION, ErrorCode.NOT_FOUND_DIARY_EXCEPTION.getMessage()));

        if (!diary.getTracker().getMember().equals(member)) {
            throw new CustomException(ErrorCode.ONLY_OWN_DIARY_ACCESS_EXCEPTION, ErrorCode.ONLY_OWN_DIARY_ACCESS_EXCEPTION.getMessage());
        }

        DiaryResDto resDto = DiaryResDto.builder()
                .emotionType(diary.getEmotionType())
                .weatherType(diary.getWeatherType())
                .title(diary.getTitle())
                .content(diary.getContent())
                .createdAt(diary.getCreatedAt())
                .updatedAt(diary.getUpdatedAt())
                .build();

        return ApiResponseTemplate.success(SuccessCode.GET_DIARY_SUCCESS, resDto);
    }

    @Transactional(readOnly = true)
    public ApiResponseTemplate<List<DiaryResDto>> getAllDiariesByMember(Principal principal) {

        Long memberId = Long.parseLong(principal.getName());

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER_EXCEPTION, ErrorCode.NOT_FOUND_MEMBER_EXCEPTION.getMessage()));

        List<Diary> diaries = diaryRepository.findByTrackerMember(member);

        List<DiaryResDto> resDtos = diaries.stream()
                .map(diary -> DiaryResDto.builder()
                        .emotionType(diary.getEmotionType())
                        .weatherType(diary.getWeatherType())
                        .title(diary.getTitle())
                        .content(diary.getContent())
                        .createdAt(diary.getCreatedAt())
                        .updatedAt(diary.getUpdatedAt())
                        .build())
                .collect(Collectors.toList());

        return ApiResponseTemplate.success(SuccessCode.GET_ALL_DIARIES_SUCCESS, resDtos);
    }
}

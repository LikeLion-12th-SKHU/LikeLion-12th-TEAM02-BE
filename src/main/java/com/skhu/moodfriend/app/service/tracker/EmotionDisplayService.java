package com.skhu.moodfriend.app.service.tracker;

import com.skhu.moodfriend.app.dto.tracker.reqDto.MonthlyEmotionReqDto;
import com.skhu.moodfriend.app.dto.tracker.resDto.MonthlyEmotionResDto;
import com.skhu.moodfriend.app.entity.tracker.diary.Diary;
import com.skhu.moodfriend.app.entity.tracker.diary.EmotionType;
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
import java.time.YearMonth;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class EmotionDisplayService {

    private final DiaryRepository diaryRepository;
    private final MemberRepository memberRepository;

    public ApiResponseTemplate<MonthlyEmotionResDto> getMonthlyEmotions(
            MonthlyEmotionReqDto reqDto, Principal principal) {

        Long memberId = Long.parseLong(principal.getName());

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER_EXCEPTION, ErrorCode.NOT_FOUND_MEMBER_EXCEPTION.getMessage()));

        YearMonth yearMonth = reqDto.yearMonth();

        List<Diary> diaries = diaryRepository.findMonthlyDailyEmotions(
                member,
                yearMonth.getYear(),
                yearMonth.getMonthValue()
        );

        Map<LocalDate, EmotionType> dailyEmotions = diaries.stream()
                .collect(Collectors.toMap(
                        Diary::getCreatedAt,
                        Diary::getEmotionType,
                        (existing, replacement) -> existing,
                        LinkedHashMap::new));

        MonthlyEmotionResDto resDto = MonthlyEmotionResDto.builder()
                .dailyEmotions(dailyEmotions)
                .build();

        return ApiResponseTemplate.success(SuccessCode.GET_MONTHLY_EMOTION_TYPES_SUCCESS, resDto);
    }

}

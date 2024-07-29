package com.skhu.moodfriend.app.service.member;

import com.skhu.moodfriend.app.dto.member.reqDto.MonthlyEmotionReqDto;
import com.skhu.moodfriend.app.dto.member.resDto.MonthlyEmotionResDto;
import com.skhu.moodfriend.app.domain.tracker.diary.Diary;
import com.skhu.moodfriend.app.domain.member.Member;
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
import java.time.YearMonth;
import java.util.List;

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

        return ApiResponseTemplate.success(SuccessCode.GET_MONTHLY_EMOTION_TYPES_SUCCESS, MonthlyEmotionResDto.of(diaries));
    }

}

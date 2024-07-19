package com.skhu.moodfriend.app.service.tracker;

import com.skhu.moodfriend.app.dto.tracker.reqDto.DiaryCreateReqDto;
import com.skhu.moodfriend.app.dto.tracker.resDto.DiaryCreateResDto;
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

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class DiaryCreateService {

    private final DiaryRepository diaryRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public ApiResponseTemplate<DiaryCreateResDto> createDiary(
            DiaryCreateReqDto reqDto,
            Principal principal) {

        Long memberId = Long.parseLong(principal.getName());

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER_EXCEPTION, ErrorCode.NOT_FOUND_MEMBER_EXCEPTION.getMessage()));

        boolean exists = diaryRepository.existsByMemberAndCreatedAtDate(member);
        if (exists) {
            throw new CustomException(ErrorCode.ALREADY_EXIST_DIARY_EXCEPTION, ErrorCode.ALREADY_EXIST_DIARY_EXCEPTION.getMessage());
        }

        Diary diary = Diary.builder()
                .emotionType(reqDto.emotionType())
                .weatherType(reqDto.weatherType())
                .content(reqDto.content())
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

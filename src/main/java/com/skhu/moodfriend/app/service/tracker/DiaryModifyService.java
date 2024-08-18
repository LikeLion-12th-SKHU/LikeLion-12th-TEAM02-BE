package com.skhu.moodfriend.app.service.tracker;

import com.skhu.moodfriend.app.dto.tracker.reqDto.DiaryUpdateReqDto;
import com.skhu.moodfriend.app.dto.tracker.resDto.DiaryResDto;
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

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class DiaryModifyService {

    private final DiaryRepository diaryRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public ApiResponseTemplate<DiaryResDto> updateDiary(
            DiaryUpdateReqDto reqDto, Principal principal) {

        Long memberId = Long.parseLong(principal.getName());
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER_EXCEPTION, ErrorCode.NOT_FOUND_MEMBER_EXCEPTION.getMessage()));

        Diary diary = diaryRepository.findByDiaryIdAndMember(reqDto.diaryId(), member)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_DIARY_EXCEPTION, ErrorCode.NOT_FOUND_DIARY_EXCEPTION.getMessage()));

        if (diaryRepository.existsByMemberAndCreatedAtDateExcludingDiary(member, reqDto.createdAt(), reqDto.diaryId())) {
            throw new CustomException(ErrorCode.ALREADY_EXIST_DIARY_EXCEPTION, ErrorCode.ALREADY_EXIST_DIARY_EXCEPTION.getMessage());
        }

        diary.update(reqDto.emotionType(), reqDto.weatherType(), reqDto.title(), reqDto.content(), reqDto.createdAt());
        diaryRepository.save(diary);

        return ApiResponseTemplate.success(SuccessCode.UPDATE_DIARY_SUCCESS, DiaryResDto.of(diary));
    }

    @Transactional
    public ApiResponseTemplate<Void> deleteDiary(
            Long diaryId, Principal principal) {

        Long memberId = Long.parseLong(principal.getName());
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER_EXCEPTION, ErrorCode.NOT_FOUND_MEMBER_EXCEPTION.getMessage()));

        Diary diary = diaryRepository.findByDiaryIdAndMember(diaryId, member)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_DIARY_EXCEPTION, ErrorCode.NOT_FOUND_DIARY_EXCEPTION.getMessage()));

        diaryRepository.delete(diary);

        return ApiResponseTemplate.success(SuccessCode.DELETE_DIARY_SUCCESS, null);
    }
}

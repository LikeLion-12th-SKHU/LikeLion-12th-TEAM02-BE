package com.skhu.moodfriend.app.service.tracker;

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
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class DiaryDisplayService {

    private final DiaryRepository diaryRepository;
    private final MemberRepository memberRepository;

    public ApiResponseTemplate<DiaryResDto> getDiaryById(
            Long diaryId, Principal principal) {

        Long memberId = Long.parseLong(principal.getName());
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER_EXCEPTION, ErrorCode.NOT_FOUND_MEMBER_EXCEPTION.getMessage()));

        Diary diary = diaryRepository.findByDiaryIdAndMember(diaryId, member)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_DIARY_EXCEPTION, ErrorCode.NOT_FOUND_DIARY_EXCEPTION.getMessage()));

        if (!diary.getMember().equals(member)) {
            throw new CustomException(ErrorCode.ONLY_OWN_DIARY_ACCESS_EXCEPTION, ErrorCode.ONLY_OWN_DIARY_ACCESS_EXCEPTION.getMessage());
        }

        return ApiResponseTemplate.success(SuccessCode.GET_DIARY_SUCCESS, DiaryResDto.of(diary));
    }

    public ApiResponseTemplate<List<DiaryResDto>> getAllDiariesByMember(Principal principal) {

        Long memberId = Long.parseLong(principal.getName());
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER_EXCEPTION, ErrorCode.NOT_FOUND_MEMBER_EXCEPTION.getMessage()));

        List<Diary> diaries = diaryRepository.findByMemberOrderByCreatedAtAsc(member);

        List<DiaryResDto> resDtos = diaries.stream()
                .map(DiaryResDto::of)
                .collect(Collectors.toList());

        return ApiResponseTemplate.success(SuccessCode.GET_ALL_DIARIES_SUCCESS, resDtos);
    }
}

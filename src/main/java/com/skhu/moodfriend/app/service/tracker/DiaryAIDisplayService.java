package com.skhu.moodfriend.app.service.tracker;

import com.skhu.moodfriend.app.dto.tracker.resDto.DiaryAIResDto;
import com.skhu.moodfriend.app.entity.member.Member;
import com.skhu.moodfriend.app.entity.tracker.diary_ai.DiaryAI;
import com.skhu.moodfriend.app.repository.DiaryAIRepository;
import com.skhu.moodfriend.app.repository.MemberRepository;
import com.skhu.moodfriend.global.exception.CustomException;
import com.skhu.moodfriend.global.exception.code.ErrorCode;
import com.skhu.moodfriend.global.exception.code.SuccessCode;
import com.skhu.moodfriend.global.template.ApiResponseTemplate;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class DiaryAIDisplayService {

    private final DiaryAIRepository diaryAIRepository;
    private final MemberRepository memberRepository;

    public ApiResponseTemplate<List<DiaryAIResDto>> getAllDiarySummariesByMember(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER_EXCEPTION, ErrorCode.NOT_FOUND_MEMBER_EXCEPTION.getMessage()));

        List<DiaryAI> diaryAIs = diaryAIRepository.findByMemberOrderByCreatedAt(member);

        List<DiaryAIResDto> resDtos = diaryAIs.stream()
                .map(diaryAI -> DiaryAIResDto.builder()
                        .diaryAIId(diaryAI.getDiaryAIId())
                        .createdAt(diaryAI.getCreatedAt())
                        .summary(diaryAI.getSummary())
                        .build())
                .collect(Collectors.toList());

        return ApiResponseTemplate.success(SuccessCode.GET_ALL_DIARY_SUMMARIES_SUCCESS, resDtos);
    }
}

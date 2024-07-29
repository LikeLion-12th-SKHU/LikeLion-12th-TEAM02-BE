package com.skhu.moodfriend.app.service.tracker;

import com.skhu.moodfriend.app.domain.member.Member;
import com.skhu.moodfriend.app.domain.tracker.diary_ai.DiaryAI;
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

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class DiaryAIModifyService {

    private final DiaryAIRepository diaryAIRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public ApiResponseTemplate<Void> deleteDiaryAI(
            Long diaryAIId,
            Long memberId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER_EXCEPTION, ErrorCode.NOT_FOUND_MEMBER_EXCEPTION.getMessage()));

        DiaryAI diaryAI = diaryAIRepository.findById(diaryAIId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_DIARY_EXCEPTION, ErrorCode.NOT_FOUND_DIARY_EXCEPTION.getMessage()));

        if (!diaryAI.getMember().equals(member)) {
            throw new CustomException(ErrorCode.ONLY_OWN_DIARY_ACCESS_EXCEPTION, ErrorCode.ONLY_OWN_DIARY_ACCESS_EXCEPTION.getMessage());
        }

        diaryAIRepository.delete(diaryAI);

        return ApiResponseTemplate.success(SuccessCode.DELETE_DIARY_SUCCESS, null);
    }
}

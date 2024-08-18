package com.skhu.moodfriend.app.service.object;

import com.skhu.moodfriend.app.domain.member.Member;
import com.skhu.moodfriend.app.domain.member.object.MemberObject;
import com.skhu.moodfriend.app.dto.object.reqDto.UpdateObjectStatusReqDto;
import com.skhu.moodfriend.app.dto.object.resDto.ObjectResDto;
import com.skhu.moodfriend.app.repository.MemberObjectRepository;
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
public class ObjectStatusService {

    private final MemberRepository memberRepository;
    private final MemberObjectRepository memberObjectRepository;

    @Transactional
    public ApiResponseTemplate<ObjectResDto> updateObjectStatus(
            UpdateObjectStatusReqDto reqDto, Principal principal) {

        Long memberId = Long.parseLong(principal.getName());
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER_EXCEPTION, ErrorCode.NOT_FOUND_MEMBER_EXCEPTION.getMessage()));

        MemberObject memberObject = memberObjectRepository.findById(reqDto.memberObjectId())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_OBJECT_EXCEPTION, ErrorCode.NOT_FOUND_OBJECT_EXCEPTION.getMessage()));

        if (!memberObject.getMember().equals(member)) {
            throw new CustomException(ErrorCode.FORBIDDEN_ACCESS_EXCEPTION, ErrorCode.FORBIDDEN_ACCESS_EXCEPTION.getMessage());
        }

        memberObject.updateStatus(reqDto.status());
        memberObjectRepository.save(memberObject);

        return ApiResponseTemplate.success(SuccessCode.UPDATE_OBJECT_STATUS_SUCCESS, ObjectResDto.of(memberObject));
    }
}

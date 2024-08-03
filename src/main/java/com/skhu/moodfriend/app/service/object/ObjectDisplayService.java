package com.skhu.moodfriend.app.service.object;

import com.skhu.moodfriend.app.domain.member.Member;
import com.skhu.moodfriend.app.domain.member.object.MemberObject;
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
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ObjectDisplayService {

    private final MemberRepository memberRepository;
    private final MemberObjectRepository memberObjectRepository;

    public ApiResponseTemplate<List<ObjectResDto>> getOwnedObjects(Principal principal) {

        Long memberId = Long.parseLong(principal.getName());
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER_EXCEPTION, ErrorCode.NOT_FOUND_MEMBER_EXCEPTION.getMessage()));

        List<MemberObject> ownedObjects = memberObjectRepository.findByMember(member);

        List<ObjectResDto> resDtos = ownedObjects.stream()
                .map(ObjectResDto::of)
                .collect(Collectors.toList());

        return ApiResponseTemplate.success(SuccessCode.GET_OBJECTS_SUCCESS, resDtos);
    }
}

package com.skhu.moodfriend.app.service.object;

import com.skhu.moodfriend.app.domain.member.Member;
import com.skhu.moodfriend.app.domain.member.object.MemberObject;
import com.skhu.moodfriend.app.domain.store.Objects;
import com.skhu.moodfriend.app.domain.store.ObjectStore;
import com.skhu.moodfriend.app.dto.object.reqDto.PurchaseReqDto;
import com.skhu.moodfriend.app.dto.object.resDto.ObjectResDto;
import com.skhu.moodfriend.app.repository.MemberObjectRepository;
import com.skhu.moodfriend.app.repository.MemberRepository;
import com.skhu.moodfriend.app.repository.ObjectStoreRepository;
import com.skhu.moodfriend.global.exception.CustomException;
import com.skhu.moodfriend.global.exception.code.ErrorCode;
import com.skhu.moodfriend.global.exception.code.SuccessCode;
import com.skhu.moodfriend.global.template.ApiResponseTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class PurchaseService {

    private final MemberRepository memberRepository;
    private final ObjectStoreRepository objectStoreRepository;
    private final MemberObjectRepository memberObjectRepository;

    @Transactional
    public ApiResponseTemplate<ObjectResDto> purchaseObject(
            PurchaseReqDto reqDto,
            Principal principal) {

        Long memberId = Long.parseLong(principal.getName());
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER_EXCEPTION, ErrorCode.NOT_FOUND_MEMBER_EXCEPTION.getMessage()));

        Objects objects = Arrays.stream(Objects.values())
                .filter(obj -> obj.getDisplayName().equalsIgnoreCase(reqDto.objectName()))
                .findFirst()
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_OBJECT_EXCEPTION, ErrorCode.NOT_FOUND_OBJECT_EXCEPTION.getMessage()));

        ObjectStore objectStore = objectStoreRepository.findByObject(objects)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_OBJECT_EXCEPTION, ErrorCode.NOT_FOUND_OBJECT_EXCEPTION.getMessage()));

        if (memberObjectRepository.existsByMemberAndObject(member, objectStore.getObject())) {
            throw new CustomException(ErrorCode.DUPLICATE_OBJECT_EXCEPTION, ErrorCode.DUPLICATE_OBJECT_EXCEPTION.getMessage());
        }

        int price = objectStore.getPrice();
        if (member.getMileage() < price) {
            throw new CustomException(ErrorCode.INSUFFICIENT_MILEAGE_EXCEPTION, ErrorCode.INSUFFICIENT_MILEAGE_EXCEPTION.getMessage());
        }

        member.updateMileage(-price);
        memberRepository.save(member);

        MemberObject memberObject = MemberObject.builder()
                .object(objectStore.getObject())
                .status(false)
                .member(member)
                .build();

        memberObjectRepository.save(memberObject);

        return ApiResponseTemplate.success(SuccessCode.PURCHASE_OBJECT_SUCCESS, ObjectResDto.of(memberObject));
    }
}

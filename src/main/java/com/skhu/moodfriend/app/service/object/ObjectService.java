package com.skhu.moodfriend.app.service.object;

import com.skhu.moodfriend.app.domain.member.Member;
import com.skhu.moodfriend.app.domain.member.object.MemberObject;
import com.skhu.moodfriend.app.domain.store.ObjectStore;

import com.skhu.moodfriend.app.dto.object.MemberObjectDto;
import com.skhu.moodfriend.app.dto.object.ObjectStoreDto;
import com.skhu.moodfriend.app.repository.MemberObjectRepository;
import com.skhu.moodfriend.app.repository.MemberRepository;
import com.skhu.moodfriend.app.repository.ObjectStoreRepository;
import com.skhu.moodfriend.global.exception.CustomException;
import com.skhu.moodfriend.global.exception.code.ErrorCode;
import com.skhu.moodfriend.global.exception.code.SuccessCode;
import com.skhu.moodfriend.global.template.ApiResponseTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ObjectService {

    private final MemberRepository memberRepository;
    private final MemberObjectRepository memberObjectRepository;
    private final ObjectStoreRepository objectStoreRepository;

    public ApiResponseTemplate<Void> addNewObject(ObjectStoreDto objectDto) {

        objectStoreRepository.findByObjectId(objectDto.objectId()).orElseThrow(()-> new CustomException(ErrorCode.DUPLICATE_OBJECT_EXCEPTION,"오브제가 중복됩니다"));

        ObjectStore objectStore = ObjectStore.builder()
                .object(objectDto.object())
                .build();

        objectStoreRepository.save(objectStore);

        return ApiResponseTemplate.success(SuccessCode.CREATE_OBJECT_SUCCESS, null);

    }

    public ApiResponseTemplate< List<ObjectStoreDto>> fetchObjectList(Principal principal) {
        Long memberId = Long.parseLong(principal.getName());
        List<MemberObject> memberObject = memberObjectRepository.findByMemberId(memberId);

        List<ObjectStoreDto> list = new ArrayList<>();
        memberObject.stream().forEach(target -> {
            ObjectStoreDto memberObjectDto = new ObjectStoreDto(target.getObjectStore().getObject(), target.isStatus(), target.getMemberObjectId());
            list.add(memberObjectDto);
        });
        return ApiResponseTemplate.success(SuccessCode.GET_OBJECTS_SUCCESS, null);

    }

    public ApiResponseTemplate<Void> useMemberObject(Long memberObjectId, Principal principal) {
        Long memberId = Long.parseLong(principal.getName());

        memberObjectRepository.updateMemberObjectStatus(memberId, memberObjectId);
        return ApiResponseTemplate.success(SuccessCode.UPDATE_MEMBER_OEBJECT, null);
    }

    public ApiResponseTemplate<List<ObjectStore>> listAllObjects() {
        List<ObjectStore> objectList = objectStoreRepository.findAll();
        return ApiResponseTemplate.success(SuccessCode.GET_OBJECTS_SUCCESS, objectList);
    }

    public ApiResponseTemplate<?> addMyNewObject(MemberObjectDto objectDto, Principal principal) {
        Long memberId = Long.parseLong(principal.getName());
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER_EXCEPTION, ErrorCode.NOT_FOUND_MEMBER_EXCEPTION.getMessage()));

        ObjectStore objectStore = objectStoreRepository.findByObjectId(objectDto.objectId()).get();
        if (objectStore==null) {
            throw new CustomException(ErrorCode.NOT_FOUND_OBJECT_EXCEPTION, ErrorCode.NOT_FOUND_OBJECT_EXCEPTION.getMessage());
        }


        MemberObject memberObject = MemberObject.builder()
                .member(member)
                .objectStore(objectStore)
                .status(objectDto.status()).build();
        memberObjectRepository.save(memberObject);

        return ApiResponseTemplate.success(SuccessCode.CREATE_OBJECT_SUCCESS, null);

    }
}

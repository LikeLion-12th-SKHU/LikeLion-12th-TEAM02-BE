package com.skhu.moodfriend.app.service.hospital;

import com.skhu.moodfriend.app.dto.hospital.KakaoMapDocument;
import com.skhu.moodfriend.app.dto.hospital.KakaoMapKeyword;
import com.skhu.moodfriend.app.dto.hospital.reqDto.KakaoMapKeywordReqDto;
import com.skhu.moodfriend.app.dto.hospital.resDto.KakaoMapKeywordResDto;
import com.skhu.moodfriend.app.repository.KakaoMapRepository;
import com.skhu.moodfriend.global.exception.code.SuccessCode;
import com.skhu.moodfriend.global.template.ApiResponseTemplate;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class KakaoMapService {
    @Value("${kakao-map.client-id}")
    private String KAKAO_CLIENT_ID;

    private static final String KAKAO_AUTH_PREFIX = "KakaoAK ";

    private final KakaoMapRepository kakaoMapRepository;

    public ApiResponseTemplate<List<KakaoMapKeywordResDto>> retrieveByKeyword(KakaoMapKeywordReqDto request) {
        KakaoMapKeyword response = kakaoMapRepository.searchByKeyword(
                KAKAO_AUTH_PREFIX + KAKAO_CLIENT_ID,
                request.query(),
                request.x(),
                request.y(),
                request.radius()
        );

        List<KakaoMapKeywordResDto> results = response.documents()
                .stream()
                .map(it -> new KakaoMapKeywordResDto(
                        it.categoryName(),
                        it.placeName(),
                        it.distance(),
                        it.placeUrl()
                ))
                .collect(Collectors.toList());

        return ApiResponseTemplate.success(SuccessCode.ATTENDANCE_SUCCESS, results);
    }
}
package com.skhu.moodfriend.app.service.hospital;

import com.skhu.moodfriend.app.dto.hospital.reqDto.KakaoMapKeywordReqDto;
import com.skhu.moodfriend.app.dto.hospital.resDto.KakaoMapKeywordResDto;
import com.skhu.moodfriend.global.exception.code.SuccessCode;
import com.skhu.moodfriend.global.template.ApiResponseTemplate;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class HospitalService {
    private static final List<String> DEFAULT_KEYWORDS = List.of("심리과", "심리병원", "정신과", "정신병원");
    private final KakaoMapService kakaoMapService;

    public ApiResponseTemplate<List<KakaoMapKeywordResDto>> retrieve(
            String x,
            String y,
            String radius
    ) {
        Set<KakaoMapKeywordResDto> results = new HashSet<>();

        for (String keyword : DEFAULT_KEYWORDS) {
            KakaoMapKeywordReqDto reqDto = new KakaoMapKeywordReqDto(keyword, x, y, radius, null);
            List<KakaoMapKeywordResDto> resDtos = kakaoMapService.retrieveByKeyword(reqDto);
            results.addAll(resDtos.stream()
                    .map(it -> new KakaoMapKeywordResDto(
                            parseCategoryName(it.categoryName()),
                            it.placeName(),
                            it.distance(),
                            it.placeUrl()
                    )).collect(Collectors.toList()));
        }
        List<KakaoMapKeywordResDto> sortedResults = new ArrayList<>(results);
        sortedResults.sort(Comparator.comparing(KakaoMapKeywordResDto::distance));

        return ApiResponseTemplate.success(SuccessCode.ATTENDANCE_SUCCESS, sortedResults);
    }

    private String parseCategoryName(String categoryName) {
        String[] parts = categoryName.split("의료,건강 > 병원 > ");
        return parts.length > 1 ? parts[1] : categoryName;
    }
}

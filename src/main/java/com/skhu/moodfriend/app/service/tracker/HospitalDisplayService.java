package com.skhu.moodfriend.app.service.tracker;

import com.skhu.moodfriend.app.dto.tracker.reqDto.HospitalReqDto;
import com.skhu.moodfriend.app.dto.tracker.resDto.HospitalResDto;
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
public class HospitalDisplayService {
    private static final List<String> DEFAULT_KEYWORDS = List.of("심리과", "심리병원", "정신과", "정신병원");
    private final KakaoMapService kakaoMapService;

    public ApiResponseTemplate<List<HospitalResDto>> retrieve(
            String x,
            String y,
            String radius
    ) {
        Set<HospitalResDto> results = new HashSet<>();

        for (String keyword : DEFAULT_KEYWORDS) {
            HospitalReqDto reqDto = new HospitalReqDto(keyword, x, y, radius, null);
            ApiResponseTemplate<List<HospitalResDto>> apiResponse = kakaoMapService.retrieveByKeyword(reqDto);

            if (apiResponse.isSuccess()) {
                results.addAll(apiResponse.getData().stream()
                        .map(resDto -> new HospitalResDto(
                                resDto.placeName(),
                                resDto.placeUrl(),
                                resDto.distance(),
                                parseCategoryName(resDto.categoryName())
                        )).collect(Collectors.toList()));
            }
        }
        List<HospitalResDto> resDtos = new ArrayList<>(results);
        resDtos.sort(Comparator.comparing(HospitalResDto::distance));

        return ApiResponseTemplate.success(SuccessCode.GET_HOSPITALS_SUCCESS, resDtos);
    }

    private String parseCategoryName(String categoryName) {
        String[] parts = categoryName.split("의료,건강 > 병원 > ");
        return parts.length > 1 ? parts[1] : categoryName;
    }
}
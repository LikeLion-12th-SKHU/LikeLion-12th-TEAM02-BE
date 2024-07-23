package com.skhu.moodfriend.app.service.hospital;

import com.skhu.moodfriend.app.dto.hospital.HospitalResDto;
import com.skhu.moodfriend.app.dto.hospital.kakaoMap.KakaoMapKeywordRequest;
import com.skhu.moodfriend.app.dto.hospital.kakaoMap.KakaoMapKeywordResDto;
import com.skhu.moodfriend.app.service.hospital.kakaoMap.KakaoMapService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class HospitalService {
    private static final List<String> DEFAULT_KEYWORDS = List.of("심리과", "심리병원", "정신과", "정신병원");
    private final KakaoMapService kakaoMapService;

    @Transactional(readOnly = true)
    public List<HospitalResDto> retrieve(
            String x,
            String y,
            String radius
    ) {
        Set<HospitalResDto> results = new HashSet<>();

        for (String keyword : DEFAULT_KEYWORDS) {
            KakaoMapKeywordRequest request = new KakaoMapKeywordRequest(keyword, x, y, radius, null);
            List<KakaoMapKeywordResDto> response = kakaoMapService.retrieveByKeyword(request);
            results.addAll(response.stream()
                    .map(it -> new HospitalResDto(
                            parseCategoryName(it.categoryName()),
                            it.placeName(),
                            it.distance(),
                            it.placeUrl()
                    )).collect(Collectors.toList()));
        }

        return new ArrayList<>(results);
    }

    private String parseCategoryName(String categoryName) {
        String[] parts = categoryName.split("의료,건강 > 병원 > ");
        return parts.length > 1 ? parts[1] : categoryName;
    }
}

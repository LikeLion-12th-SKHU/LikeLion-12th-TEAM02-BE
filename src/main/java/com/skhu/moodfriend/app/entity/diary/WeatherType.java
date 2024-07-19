package com.skhu.moodfriend.app.entity.diary;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public enum WeatherType {

    SUNNY("SUNNY", "맑음"),
    SNOW("SNOW", "눈"),
    CLOUD("CLOUD", "흐림"),
    RAIN("RAIN", "비"),
    WIND("WIND", "바람");

    private final String code;
    private final String displayName;
}

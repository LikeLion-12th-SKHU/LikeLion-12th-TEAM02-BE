package com.skhu.moodfriend.app.entity.diary;

import com.skhu.moodfriend.global.exception.CustomException;
import com.skhu.moodfriend.global.exception.code.ErrorCode;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public enum EmotionType {

    JOY("JOY", "즐거움"),
    SO_SO("SO_SO", "그저그럼"),
    SADNESS("SADNESS", "슬픔"),
    DISPLEASURE("DISPLEASURE", "언짢음"),
    ANGER("ANGER", "화남");

    private final String code;
    private final String displayName;

    public static EmotionType getEmotionOfString(String emotionType) {
        for (EmotionType type : EmotionType.values()) {
            if (type.code.equals(emotionType)) {
                return type;
            }
        }

        throw new CustomException(ErrorCode.INVALID_DISPLAY_NAME_EXCEPTION,
                ErrorCode.INVALID_DISPLAY_NAME_EXCEPTION.getMessage());
    }
}

package com.skhu.moodfriend.app.dto.chat.reqDto;

import java.util.List;

public record TranslationReqDto(
        List<String> text,
        String target_lang
) {
}

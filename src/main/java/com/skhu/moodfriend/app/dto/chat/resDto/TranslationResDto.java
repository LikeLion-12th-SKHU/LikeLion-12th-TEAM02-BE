package com.skhu.moodfriend.app.dto.chat.resDto;

import com.skhu.moodfriend.app.dto.chat.Translation;

import java.util.List;

public record TranslationResDto(
        List<Translation> translations
) {
}

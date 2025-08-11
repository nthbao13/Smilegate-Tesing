package com.example.game.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LanguageResponse {
    public String languageId;

    private String languageName;
}

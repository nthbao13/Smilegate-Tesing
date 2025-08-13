package com.example.game.service;

import com.example.game.dto.response.LanguageResponse;
import com.example.game.entity.Game;

import java.util.List;

public interface LanguageService {
    String getDefaultName(Game game);
    List<LanguageResponse> getFullLanguages();
//    boolean validateLanguage(HashSet<InputGameNameRequest> requests) throws APIError;
}

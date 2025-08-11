package com.example.game.service;

import com.detectlanguage.errors.APIError;
import com.example.game.dto.request.InputGameNameRequest;
import com.example.game.dto.response.LanguageResponse;
import com.example.game.entity.Game;

import java.util.HashSet;
import java.util.List;

public interface LanguageService {
    public String getDefaultName(Game game);
    public List<LanguageResponse> getFullLanguages();
    public boolean validateLanguage(HashSet<InputGameNameRequest> requests) throws APIError;
}

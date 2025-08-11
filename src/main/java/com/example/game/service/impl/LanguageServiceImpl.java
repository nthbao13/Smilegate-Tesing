package com.example.game.service.impl;

import com.detectlanguage.Result;
import com.detectlanguage.errors.APIError;
import com.example.game.dto.request.InputGameNameRequest;
import com.example.game.dto.response.LanguageResponse;
import com.example.game.entity.Game;
import com.example.game.entity.GameName;
import com.example.game.entity.Language;
import com.example.game.exception.ErrorCode;
import com.example.game.exception.InputException;
import com.example.game.repository.GameNameRepository;
import com.example.game.repository.LanguageRepository;
import com.example.game.service.LanguageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.detectlanguage.DetectLanguage;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class LanguageServiceImpl implements LanguageService {

    private final String API_DETECT_LANGUAGE;
    private final double THRESH_HOLD = 0.5;
    private final GameNameRepository gameNameRepository;
    private final LanguageRepository languageRepository;

    @Autowired
    public LanguageServiceImpl(GameNameRepository gameNameRepository,
                               LanguageRepository languageRepository,
                               @Value("${api.key.languages") String API_DETECT_LANGUAGE) {
        this.gameNameRepository = gameNameRepository;
        this.languageRepository = languageRepository;
        this.API_DETECT_LANGUAGE = API_DETECT_LANGUAGE;

        DetectLanguage.apiKey = this.API_DETECT_LANGUAGE;
    }

    @Override
    public String getDefaultName(Game game) {
        Set<GameName> gameNames = game.getGameNames();
        String defaultName = null;
        for (GameName gameName : gameNames) {
            if (gameName.isDefault()) {
                defaultName = gameName.getName();
                break;
            }
        }
        return defaultName;
    }

    @Override
    public List<LanguageResponse> getFullLanguages() {
        List<Language> languages = languageRepository.findAll();
        List<LanguageResponse> languageResponses = languages.stream()
                .map(language -> LanguageResponse.builder()
                        .languageId(language.getLanguageId())
                        .languageName(language.getLanguageName())
                        .build())
                .toList();

        return languageResponses;
    }

    @Override
    public boolean validateLanguage(HashSet<InputGameNameRequest> requests) throws APIError {
        for (InputGameNameRequest request : requests) {
            List<Result> results = DetectLanguage.detect(request.getName());

            Result result = results.get(0);
            if (!result.isReliable || !result.language.equals(request.getLanguageId()) || result.confidence < THRESH_HOLD) {
                ErrorCode errorCode = ErrorCode.GAME_LANGUAGE_ERROR;
                errorCode.setMessage(errorCode.getMessage() + " " + request.getLanguageId());
                throw new InputException(errorCode);
            }
        }
        return true;
    }
}

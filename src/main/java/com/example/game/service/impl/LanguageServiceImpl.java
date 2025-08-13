package com.example.game.service.impl;

import com.example.game.dto.response.LanguageResponse;
import com.example.game.entity.Game;
import com.example.game.entity.GameName;
import com.example.game.entity.Language;
import com.example.game.repository.LanguageRepository;
import com.example.game.service.LanguageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class LanguageServiceImpl implements LanguageService {

    private final LanguageRepository languageRepository;

    @Autowired
    public LanguageServiceImpl(LanguageRepository languageRepository) {
        this.languageRepository = languageRepository;
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

        return languages.stream()
                .map(language -> LanguageResponse.builder()
                        .languageId(language.getLanguageId())
                        .languageName(language.getLanguageName())
                        .build())
                .toList();
    }

//    @Override
//    public boolean validateLanguage(HashSet<InputGameNameRequest> requests) throws APIError {
//        for (InputGameNameRequest request : requests) {
//            List<Result> results = DetectLanguage.detect(request.getName());
//
//            Result result = results.getFirst();
//            if (!result.isReliable || !result.language.equalsIgnoreCase(request.getLanguageId())
//                    || result.confidence < Constants.THRESH_HOLD_DETECT_LANGUAGE) {
//                ErrorCode errorCode = ErrorCode.GAME_LANGUAGE_ERROR;
//                errorCode.setMessage("This language is not correct " + request.getLanguageId() + " "
//                + result.isReliable + " " + result.confidence + result.language);
//                throw new InputException(errorCode);
//            }
//        }
//        return true;
//    }
}

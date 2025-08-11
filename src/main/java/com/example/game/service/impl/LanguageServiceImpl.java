package com.example.game.service.impl;

import com.example.game.entity.Game;
import com.example.game.entity.GameName;
import com.example.game.repository.GameNameRepository;
import com.example.game.repository.LanguageRepository;
import com.example.game.service.LanguageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class LanguageServiceImpl implements LanguageService {

    private final GameNameRepository gameNameRepository;

    @Autowired
    public LanguageServiceImpl(GameNameRepository gameNameRepository) {
        this.gameNameRepository = gameNameRepository;
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
}

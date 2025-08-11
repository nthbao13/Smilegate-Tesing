package com.example.game.service.impl;

import com.example.game.dto.GameDTO;
import com.example.game.entity.Game;
import com.example.game.repository.GameRepository;
import com.example.game.service.GameService;
import com.example.game.service.LanguageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameServiceImpl implements GameService {

    private final GameRepository gameRepository;
    private final LanguageService languageService;

    @Autowired
    public GameServiceImpl(GameRepository gameRepository, LanguageService languageService) {
        this.gameRepository = gameRepository;
        this.languageService = languageService;
    }

    @Override
    public Page<GameDTO> getGames(int page, int pageSize, String keyword, Integer categoryId) {
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        Page<Game> games = gameRepository.searchGames(keyword, categoryId,pageable);
        return convertToDTOList(games);
    }

    private Page<GameDTO> convertToDTOList(Page<Game> games) {
        return games.map(this::convertToDTO);
    }

    private GameDTO convertToDTO(Game game) {
        return GameDTO.builder()
                .gameId(game.getGameId())
                .gameName(languageService.getDefaultName(game))
                .gameCode(game.getGameCode())
                .build();
    }
}

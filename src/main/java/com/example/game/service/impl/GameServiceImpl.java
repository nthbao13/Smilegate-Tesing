package com.example.game.service.impl;

import com.detectlanguage.errors.APIError;
import com.example.game.dto.request.InputCategoryRequest;
import com.example.game.dto.request.InputGameNameRequest;
import com.example.game.dto.request.InputGameRequest;
import com.example.game.dto.response.GameResponse;
import com.example.game.entity.Category;
import com.example.game.entity.Game;
import com.example.game.entity.GameName;
import com.example.game.exception.CustomException;
import com.example.game.exception.ErrorCode;
import com.example.game.exception.InputException;
import com.example.game.repository.GameRepository;
import com.example.game.service.CategoryService;
import com.example.game.service.GameService;
import com.example.game.service.LanguageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class GameServiceImpl implements GameService {

    private final GameRepository gameRepository;
    private final LanguageService languageService;
    private final CategoryService categoryService;

    @Autowired
    public GameServiceImpl(GameRepository gameRepository, LanguageService languageService, CategoryService categoryService) {
        this.gameRepository = gameRepository;
        this.languageService = languageService;
        this.categoryService = categoryService;
    }

    @Override
    public Page<GameResponse> getGames(int page, int pageSize, String keyword, Integer categoryId) {
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        Page<Game> games = gameRepository.searchGames(keyword, categoryId, pageable);
        return convertToDTOList(games);
    }

    @Override
    public boolean registerGame(InputGameRequest inputGameRequest) throws APIError {
        validateInput(inputGameRequest);
        Game newGame = mapToGameEntity(inputGameRequest);
        Game savedGame = gameRepository.save(newGame);

        if (savedGame == null || savedGame.getGameId() < 0) {
            throw new CustomException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
        return true;
    }

    @Override
    public boolean isGameExist(String gameCode) {
        Game game = gameRepository.findByGameCode(gameCode);
        if (game != null && game.getGameId() > 0) {
            return  true;
        }
        return false;
    }

    private Page<GameResponse> convertToDTOList(Page<Game> games) {
        return games.map(this::convertToDTO);
    }

    private GameResponse convertToDTO(Game game) {
        return GameResponse.builder()
                .gameId(game.getGameId())
                .gameName(languageService.getDefaultName(game))
                .gameCode(game.getGameCode())
                .gameCategory(game.getCategory().getCategoryName())
                .build();
    }

    private void validateInput(InputGameRequest inputGameRequest) throws APIError {
        List<InputGameNameRequest> gameNameRequests = inputGameRequest.getGameNameRequests();
        InputCategoryRequest categoryRequest = inputGameRequest.getCategoryRequest();
        HashSet<InputGameNameRequest> gameNameRequestsSet = new HashSet<>(inputGameRequest.getGameNameRequests());

        boolean checkGameName = languageService.validateLanguage(gameNameRequestsSet);
        boolean checkCategory = categoryService.validateInputCategory(categoryRequest);

        if (this.isGameExist(inputGameRequest.getGameCode())) throw new InputException(ErrorCode.GAME_CODE_EXISTS);

        if (!checkCategory || !checkGameName) {
            throw new CustomException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }

    private Game mapToGameEntity(InputGameRequest inputGameRequest) {
        Category category = categoryService.getCategoryById(inputGameRequest
                .getCategoryRequest().getCategoryId());
        Game newGame = Game.builder()
                .category(category)
                .gameCode(inputGameRequest.getGameCode()).build();

        List<GameName> gameNames = inputGameRequest.getGameNameRequests().stream()
                .map(dto -> GameName.builder()
                        .name(dto.getName())
                        .isDefault(dto.isDefault())
                        .languageId(dto.getLanguageId())
                        .game(newGame)
                        .build()
                ).toList();

        newGame.setGameNames(new LinkedHashSet<>(gameNames));

        return newGame;
    }

}

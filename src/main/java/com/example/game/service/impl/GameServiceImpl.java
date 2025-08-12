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
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
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
        validateInput(inputGameRequest, false);
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

    @Override
    public InputGameRequest getGameRequestById(int id) {
        Optional<Game> game = gameRepository.findById(id);
        if (!game.isPresent() || game.get().getGameId() < 1) {
            throw new CustomException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }

        return maptoInputGameRequest(game.get());
    }

    @Override
    public boolean editGame(InputGameRequest inputGameRequest) throws APIError {
        Game game = gameRepository.findByGameCode(inputGameRequest.getGameCode());

        validateEditGame(inputGameRequest, game);
        Category category = categoryService.getCategoryById(inputGameRequest.getCategoryRequest().getCategoryId());

        Set<GameName> newGameNames = inputGameRequest.getGameNameRequests().stream().map(
                gameName -> GameName.builder().name(gameName.getName())
                        .isDefault(gameName.isDefault())
                        .languageId(gameName.getLanguageId()).build()
        ).collect(Collectors.toSet());

        Set<GameName> oldGameNames = game.getGameNames();

        updateGameNames(oldGameNames, newGameNames, game);

        game.setGameNames(oldGameNames);
        game.setGameCode(inputGameRequest.getGameCode());
        game.setCategory(category);

        Game gameSaved = gameRepository.save(game);

        if (gameSaved == null || gameSaved.getGameId() < 0) {
            throw new CustomException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }

        return true;
    }

    @Override
    public void deleteGamesByIds(int[] ids) {
        for (int id : ids) {
            try {
                gameRepository.deleteById(id);
            } catch (EmptyResultDataAccessException e) {
            }
        }
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

    private void validateInput(InputGameRequest inputGameRequest, boolean isEdit) throws APIError {
        List<InputGameNameRequest> gameNameRequests = inputGameRequest.getGameNameRequests();
        InputCategoryRequest categoryRequest = inputGameRequest.getCategoryRequest();
        HashSet<InputGameNameRequest> gameNameRequestsSet = new HashSet<>(inputGameRequest.getGameNameRequests());

        boolean checkGameName = languageService.validateLanguage(gameNameRequestsSet);
        boolean checkCategory = categoryService.validateInputCategory(categoryRequest);

        if (!isEdit && this.isGameExist(inputGameRequest.getGameCode())) throw new InputException(ErrorCode.GAME_CODE_EXISTS);

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

    private InputGameRequest maptoInputGameRequest(Game game) {
        InputCategoryRequest categoryRequest = InputCategoryRequest.builder()
                .categoryId(game.getCategory().getCategoryId())
                .categoryName(game.getCategory().getCategoryName())
                .build();

        List<InputGameNameRequest> gameNameRequests = game.getGameNames().stream().map(
                entity -> InputGameNameRequest.builder()
                        .name(entity.getName())
                        .languageId(entity.getLanguageId())
                        .isDefault(entity.isDefault())
                        .build()
        ).collect(Collectors.toSet()).stream().toList();

        return InputGameRequest.builder()
                .categoryRequest(categoryRequest)
                .gameNameRequests(gameNameRequests)
                .id(game.getGameId())
                .gameCode(game.getGameCode())
                .updateAt(game.getUpdateAt())
                .build();
    }

    private void updateGameNames(Set<GameName> oldGameNames, Set<GameName> newGameNames, Game game) {
        boolean changed = false;

        if (oldGameNames.removeIf(old -> newGameNames.stream()
                .noneMatch(n -> n.getLanguageId().equals(old.getLanguageId())))) {
            changed = true;
        }

        for (GameName gn : newGameNames) {
            gn.setGame(game);
            Optional<GameName> existing = oldGameNames.stream()
                    .filter(o -> o.getLanguageId().equals(gn.getLanguageId()))
                    .findFirst();

            if (existing.isPresent()) {
                GameName oldGn = existing.get();
                if (!Objects.equals(oldGn.getName(), gn.getName()) ||
                        oldGn.isDefault() != gn.isDefault()) {
                    oldGn.setName(gn.getName());
                    oldGn.setDefault(gn.isDefault());
                    changed = true;
                }
            } else {
                oldGameNames.add(gn);
                changed = true;
            }
        }

        if (changed) {
            game.setUpdateAt(LocalDateTime.now());
        }
    }


    public void validateEditGame(InputGameRequest inputGameRequest, Game game) throws APIError {
        validateInput(inputGameRequest, true);
        if (isGameExist(inputGameRequest.getGameCode()) && !inputGameRequest.getGameCode().equals(game.getGameCode()))
            throw new InputException(ErrorCode.GAME_CODE_EXISTS);
        if (game.getUpdateAt() != null && !game.getUpdateAt().isEqual(inputGameRequest.getUpdateAt())) {
            throw new InputException(ErrorCode.GAME_UPDATED);
        }
    }

}

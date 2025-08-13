package com.example.game.service;

import com.example.game.dto.request.InputGameRequest;
import com.example.game.dto.response.GameResponse;
import org.springframework.data.domain.Page;

public interface GameService {
    Page<GameResponse> getGames(int page, int pageSize, String keyword, Integer categoryId);
    void registerGame(InputGameRequest inputGameRequest);
    boolean isGameExist(String gameCode);
    InputGameRequest getGameRequestById(int id);
    void editGame(InputGameRequest inputGameRequest);
    void deleteGamesByIds(int[] ids);
}

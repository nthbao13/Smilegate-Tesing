package com.example.game.service;

import com.detectlanguage.errors.APIError;
import com.example.game.dto.request.InputGameRequest;
import com.example.game.dto.response.GameResponse;
import org.springframework.data.domain.Page;

public interface GameService {
    public Page<GameResponse> getGames(int page, int pageSize, String keyword, Integer categoryId);
    public boolean registerGame(InputGameRequest inputGameRequest) throws APIError;
    public boolean isGameExist(String gameCode);
}

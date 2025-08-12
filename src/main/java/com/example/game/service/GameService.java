package com.example.game.service;

import com.detectlanguage.errors.APIError;
import com.example.game.dto.request.InputGameRequest;
import com.example.game.dto.response.GameResponse;
import com.example.game.entity.Game;
import com.example.game.entity.GameName;
import org.springframework.data.domain.Page;

import java.util.Set;

public interface GameService {
    public Page<GameResponse> getGames(int page, int pageSize, String keyword, Integer categoryId);
    public boolean registerGame(InputGameRequest inputGameRequest) throws APIError;
    public boolean isGameExist(String gameCode);
    public InputGameRequest getGameRequestById(int id);
    public boolean editGame(InputGameRequest inputGameRequest) throws APIError;
    public void deleteGamesByIds(int[] ids);
}

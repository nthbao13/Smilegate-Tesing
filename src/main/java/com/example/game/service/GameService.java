package com.example.game.service;

import com.example.game.dto.GameDTO;
import org.springframework.data.domain.Page;

public interface GameService {
    public Page<GameDTO> getGames(int page, int pageSize, String keyword, Integer categoryId);
}

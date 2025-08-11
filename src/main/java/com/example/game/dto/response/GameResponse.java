package com.example.game.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GameResponse {
    private int gameId;
    String gameName;
    String gameCode;
}

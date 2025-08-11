package com.example.game.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GameDTO {
    private int gameId;
    String gameName;
    String gameCode;
}

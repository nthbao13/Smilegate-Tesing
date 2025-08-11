package com.example.game.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.util.HashSet;

@Data
@Builder
public class InputGameRequest {

    @NotBlank(message = "Game Code must not be empty")
    @Size(min = 5, message = "Game Code must be at least 5 characters")
    private String gameCode;

    @NotNull(message = "Please choose Category")
    private InputCategoryRequest categoryRequest;

    @NotNull(message = "Please insert at least 1 language")
    private HashSet<InputGameNameRequest> gameNameRequests;
}

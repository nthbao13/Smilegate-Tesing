package com.example.game.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InputGameRequest {

    @NotBlank(message = "Game Code must not be empty")
    @Size(min = 5, message = "Game Code must be at least 5 characters")
    private String gameCode;

    @NotNull(message = "Please choose Category")
    private InputCategoryRequest categoryRequest;

    @NotNull(message = "Please insert at least 1 language")
    private List<InputGameNameRequest> gameNameRequests = new ArrayList<>();
}

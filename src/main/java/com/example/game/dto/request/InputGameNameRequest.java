package com.example.game.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InputGameNameRequest {

    @NotBlank(message = "Please enter name")
    private String name;
    @NotNull(message = "Please choose language corresponding")
    private String languageId;
    private boolean isDefault;
}

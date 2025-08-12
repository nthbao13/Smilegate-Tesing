package com.example.game.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InputGameNameRequest {

    @NotBlank(message = "Please enter name")
    private String name;
    @NotNull(message = "Please choose language corresponding")
    private String languageId;
    private Boolean isDefault;

    public boolean isDefault() {
        return this.isDefault;
    }
}

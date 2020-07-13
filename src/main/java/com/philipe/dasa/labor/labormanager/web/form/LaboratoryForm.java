package com.philipe.dasa.labor.labormanager.web.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@Data
public class LaboratoryForm {
    @Schema(description = "Laboratory name")
    @NotBlank
    @NotNull
    private String name;

    @Schema(description = "Laboratory address")
    @NotBlank
    @NotNull
    private String address;
}

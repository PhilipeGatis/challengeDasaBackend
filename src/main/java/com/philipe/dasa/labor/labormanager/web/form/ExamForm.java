package com.philipe.dasa.labor.labormanager.web.form;

import com.philipe.dasa.labor.labormanager.web.domain.ExamKind;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@Data
public class ExamForm {
    @Schema(description = "Exam name")
    @NotBlank(message = "Exam name must not be blank!")
    private String name;

    @Schema(description = "Exam type")
    @NotBlank(message = "Exam type must not be blank!")
    private ExamKind type;
}

package com.philipe.dasa.labor.labormanager.web.response;

import com.philipe.dasa.labor.labormanager.web.domain.ActiveStatus;
import com.philipe.dasa.labor.labormanager.web.domain.ExamKind;
import com.philipe.dasa.labor.labormanager.web.entity.Exam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class ExamResponse {
    @Schema(description = "Exam id")
    private String id;

    @Schema(description = "Exam name")
    private String name;

    @Schema(description = "Exam status")
    private ActiveStatus status;

    @Schema(description = "Exam createdAt")
    private LocalDateTime createdAt;

    @Schema(description = "Exam updatedAt")
    private LocalDateTime updatedAt;

    @Schema(description = "Exam type")
    private ExamKind type;

    @Schema(description = "Exam laboratories")
    private List<LaboratoryResponse> laboratories;

    private ExamResponse(Exam exam) {
        this.id = exam.getId().toString();
        this.name = exam.getName();
        this.status = ActiveStatus.get(exam.getStatus());
        this.type = exam.getType();
        this.createdAt = exam.getCreatedAt();
        this.updatedAt = exam.getUpdatedAt();
    }

    private ExamResponse(Exam exam, Boolean isToGetLabs) {
        this(exam);
        if (isToGetLabs) {
            this.laboratories = exam.getLaboratories().stream().map(laboratory -> LaboratoryResponse.of(laboratory, false)).collect(Collectors.toList());
        }
    }

    public static ExamResponse of(Exam exam, Boolean isToGetLabs) {
        return new ExamResponse(exam, isToGetLabs);
    }
}

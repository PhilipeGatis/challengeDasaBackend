package com.philipe.dasa.labor.labormanager.web.response;

import com.philipe.dasa.labor.labormanager.web.domain.ActiveStatus;
import com.philipe.dasa.labor.labormanager.web.entity.Exam;
import com.philipe.dasa.labor.labormanager.web.entity.Laboratory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class LaboratoryResponse {
    @Schema(description = "Laboratory id")
    private String id;

    @Schema(description = "Laboratory name")
    private String name;

    @Schema(description = "Laboratory address")
    private String address;

    @Schema(description = "Laboratory status")
    private ActiveStatus status;

    @Schema(description = "Laboratory createdAt")
    private LocalDateTime createdAt;

    @Schema(description = "Laboratory updatedAt")
    private LocalDateTime updatedAt;

    @Schema(description = "Laboratory laboratories")
    private List<ExamResponse> exams;

    private LaboratoryResponse(Laboratory laboratory) {
        this.id = laboratory.getId().toString();
        this.name = laboratory.getName();
        this.address = laboratory.getAddress();
        this.status = ActiveStatus.get(laboratory.getStatus());
        this.createdAt = laboratory.getCreatedAt();
        this.updatedAt = laboratory.getUpdatedAt();
    }

    private LaboratoryResponse(Laboratory laboratory, Boolean isToGetExams) {
        this(laboratory);
        if (isToGetExams) {
            this.exams = laboratory.getExams().stream().map(exam -> ExamResponse.of(exam, false)).collect(Collectors.toList());
        }
    }

    public static LaboratoryResponse of(Laboratory laboratory, Boolean isToGetExams) {
        return new LaboratoryResponse(laboratory, isToGetExams);
    }
}

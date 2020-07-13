package com.philipe.dasa.labor.labormanager.web.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.Map;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorResponse {

    @Schema(description = "HTTP status code")
    private int statusCode;

    @Schema(description = "HTTP status message")
    private String statusMessage;

    @Schema(description = "Error list")
    private List<Map<String, Object>> errors;
}

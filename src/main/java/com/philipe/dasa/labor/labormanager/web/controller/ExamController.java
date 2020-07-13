package com.philipe.dasa.labor.labormanager.web.controller;

import com.philipe.dasa.labor.labormanager.web.entity.Exam;
import com.philipe.dasa.labor.labormanager.web.form.ExamBatchForm;
import com.philipe.dasa.labor.labormanager.web.form.ExamForm;
import com.philipe.dasa.labor.labormanager.web.response.ErrorResponse;
import com.philipe.dasa.labor.labormanager.web.response.ExamResponse;
import com.philipe.dasa.labor.labormanager.web.service.ExamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/exams")
public class ExamController {

  @Autowired private ExamService service;

  @Operation(summary = "List all exams")
  @ApiResponses(
    value = {
      @ApiResponse(
        responseCode = "200",
        description = "Successful operation",
        content =
            @Content(
              mediaType = "application/json",
              array = @ArraySchema(schema = @Schema(implementation = ExamResponse.class))
            )
      )
    }
  )
  @GetMapping("")
  public ResponseEntity<List<ExamResponse>> index(
      @RequestParam(name = "laboratoryName", required = false) String laboratoryName) {
    List<ExamResponse> entities;

    if (laboratoryName == null || laboratoryName.isEmpty()) {
      entities =
          service
              .findAll()
              .stream()
              .map((exam) -> ExamResponse.of(exam, true))
              .collect(Collectors.toList());
    } else {
      entities =
          service
              .findByLaboratoriesNameIgnoreCase(laboratoryName)
              .stream()
              .map((exam) -> ExamResponse.of(exam, true))
              .collect(Collectors.toList());
    }

    return ResponseEntity.ok().body(entities);
  }

  @Operation(summary = "Get one by id")
  @ApiResponses(
    value = {
      @ApiResponse(
        responseCode = "200",
        description = "Successful operation",
        content =
            @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = ExamResponse.class)
            )
      ),
      @ApiResponse(
        responseCode = "404",
        description = "Not found",
        content =
            @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class)
            )
      ),
      @ApiResponse(
        responseCode = "400",
        description = "Bad request",
        content =
            @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class)
            )
      )
    }
  )
  @GetMapping("/{id}")
  public ResponseEntity<ExamResponse> show(@PathVariable("id") UUID id) {
    return service
        .findById(id)
        .map(exam -> ResponseEntity.ok().body(ExamResponse.of(exam, true)))
        .orElseThrow(
            () ->
                new ResponseStatusException(
                    HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.getReasonPhrase()));
  }

  @Operation(summary = "Create a new exam")
  @ApiResponses(
    value = {
      @ApiResponse(
        responseCode = "201",
        description = "Successful operation",
        content =
            @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = ExamResponse.class)
            )
      ),
      @ApiResponse(
        responseCode = "400",
        description = "Bad request",
        content =
            @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class)
            )
      )
    }
  )
  @PostMapping("")
  public ResponseEntity<ExamResponse> create(@Valid @RequestBody ExamForm form) {
    if (form.getName() == null && form.getType() == null)
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.getReasonPhrase());
    Exam exam = Exam.builder().name(form.getName()).type(form.getType()).build();

    Exam created = service.save(exam);
    return ResponseEntity.created(URI.create("/exams/" + created.getId()))
        .body(ExamResponse.of(created, true));
  }

  @Operation(summary = "Update an exam")
  @ApiResponses(
    value = {
      @ApiResponse(
        responseCode = "200",
        description = "Successful operation",
        content =
            @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = ExamResponse.class)
            )
      ),
      @ApiResponse(
        responseCode = "422",
        description = "Unprocessable Entity",
        content =
            @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class)
            )
      ),
      @ApiResponse(
        responseCode = "404",
        description = "Not found",
        content =
            @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class)
            )
      ),
      @ApiResponse(
        responseCode = "400",
        description = "Bad request",
        content =
            @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class)
            )
      )
    }
  )
  @PutMapping("/{id}")
  public ResponseEntity<ExamResponse> update(
      @PathVariable("id") UUID id, @Valid @RequestBody ExamForm form) {
    return service
        .findById(id)
        .map(
            exam -> {
              exam.setName(form.getName());
              exam.setType(form.getType());
              Exam updated = service.save(exam);
              return ResponseEntity.ok().body(ExamResponse.of(updated, true));
            })
        .orElseThrow(
            () ->
                new ResponseStatusException(
                    HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.getReasonPhrase()));
  }

  @Operation(summary = "Delete an exam")
  @ApiResponses(value = {@ApiResponse(responseCode = "204", description = "Successful operation")})
  @DeleteMapping("/{id}")
  @ResponseStatus(value = HttpStatus.NO_CONTENT)
  public void delete(@PathVariable("id") UUID id) {
    Exam exam =
        service
            .findById(id)
            .orElseThrow(
                () ->
                    new ResponseStatusException(
                        HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.getReasonPhrase()));

    service.delete(exam);
  }

  @Operation(summary = "Batch update an exams")
  @ApiResponses(
    value = {
      @ApiResponse(
        responseCode = "200",
        description = "Successful operation",
        content =
            @Content(
              mediaType = "application/json",
              array = @ArraySchema(schema = @Schema(implementation = ExamResponse.class))
            )
      ),
      @ApiResponse(
        responseCode = "422",
        description = "Unprocessable Entity",
        content =
            @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class)
            )
      ),
      @ApiResponse(
        responseCode = "404",
        description = "Not found",
        content =
            @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class)
            )
      ),
      @ApiResponse(
        responseCode = "400",
        description = "Bad request",
        content =
            @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class)
            )
      )
    }
  )
  @PostMapping("batch-update")
  public ResponseEntity<List<ExamResponse>> batchUpdate(
      @Valid @RequestBody List<ExamBatchForm> forms) {
    List<Exam> savedBatch =
        service.saveAll(
            forms
                .stream()
                .map(
                    form ->
                        Exam.builder()
                            .id(form.getId())
                            .name(form.getName())
                            .type(form.getType())
                            .build())
                .collect(Collectors.toList()));
    List<ExamResponse> response =
        savedBatch.stream().map(saved -> ExamResponse.of(saved, true)).collect(Collectors.toList());
    return ResponseEntity.ok(response);
  }

  @Operation(summary = "Batch create an exams")
  @ApiResponses(
    value = {
      @ApiResponse(
        responseCode = "201",
        description = "Successful operation",
        content =
            @Content(
              mediaType = "application/json",
              array = @ArraySchema(schema = @Schema(implementation = ExamResponse.class))
            )
      ),
      @ApiResponse(
        responseCode = "400",
        description = "Bad request",
        content =
            @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class)
            )
      ),
    }
  )
  @PostMapping("batch-create")
  public ResponseEntity<List<ExamResponse>> batchCreate(@Valid @RequestBody List<ExamForm> forms) {
    List<Exam> savedBatch =
        service.saveAll(
            forms
                .stream()
                .map(form -> Exam.builder().name(form.getName()).type(form.getType()).build())
                .collect(Collectors.toList()));
    return ResponseEntity.created(URI.create("batches-exams"))
        .body(
            savedBatch
                .stream()
                .map(created -> ExamResponse.of(created, true))
                .collect(Collectors.toList()));
  }

  @Operation(summary = "Batch delete a exams")
  @ApiResponses(value = {@ApiResponse(responseCode = "204", description = "Successful operation")})
  @PostMapping("batch-delete")
  @ResponseStatus(value = HttpStatus.NO_CONTENT)
  public void delete(@RequestBody List<UUID> ids) {
    List<Exam> examsToDelete =
        ids.stream()
            .map(
                id ->
                    service
                        .findById(id)
                        .orElseThrow(
                            () ->
                                new ResponseStatusException(
                                    HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.getReasonPhrase())))
            .collect(Collectors.toList());
    service.deleteAll(examsToDelete);
  }

  @Operation(summary = "connect laboratories to an exam")
  @ApiResponses(
    value = {
      @ApiResponse(
        responseCode = "200",
        description = "Successful operation",
        content =
            @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = ExamResponse.class)
            )
      ),
      @ApiResponse(
        responseCode = "404",
        description = "Not found",
        content =
            @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class)
            )
      ),
      @ApiResponse(
        responseCode = "400",
        description = "Bad request",
        content =
            @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class)
            )
      )
    }
  )
  @PostMapping("/{id}/connect")
  public ResponseEntity<ExamResponse> connectLabsToExam(
      @PathVariable("id") UUID id, @RequestBody List<UUID> laboratoriesIds) {
    Exam addExam =
        service
            .connect(id, laboratoriesIds)
            .orElseThrow(
                () ->
                    new ResponseStatusException(
                        HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.getReasonPhrase()));
    return ResponseEntity.ok().body(ExamResponse.of(addExam, true));
  }

  @Operation(summary = "disconnect laboratories to an exam")
  @ApiResponses(
    value = {
      @ApiResponse(
        responseCode = "200",
        description = "Successful operation",
        content =
            @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = ExamResponse.class)
            )
      ),
      @ApiResponse(
        responseCode = "404",
        description = "Not found",
        content =
            @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class)
            )
      ),
      @ApiResponse(
        responseCode = "400",
        description = "Bad request",
        content =
            @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class)
            )
      )
    }
  )
  @PostMapping("/{id}/disconnect")
  public ResponseEntity<ExamResponse> disconnectLabsToExam(
      @PathVariable("id") UUID id, @RequestBody List<UUID> laboratoriesIds) {
    Exam removedExam =
        service
            .disconnect(id, laboratoriesIds)
            .orElseThrow(
                () ->
                    new ResponseStatusException(
                        HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.getReasonPhrase()));
    ;
    return ResponseEntity.ok().body(ExamResponse.of(removedExam, true));
  }
}

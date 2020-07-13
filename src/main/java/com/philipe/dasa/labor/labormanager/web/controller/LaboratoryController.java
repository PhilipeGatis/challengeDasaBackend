package com.philipe.dasa.labor.labormanager.web.controller;

import com.philipe.dasa.labor.labormanager.web.entity.Laboratory;
import com.philipe.dasa.labor.labormanager.web.form.LaboratoryBatchForm;
import com.philipe.dasa.labor.labormanager.web.form.LaboratoryForm;
import com.philipe.dasa.labor.labormanager.web.response.ErrorResponse;
import com.philipe.dasa.labor.labormanager.web.response.ExamResponse;
import com.philipe.dasa.labor.labormanager.web.response.LaboratoryResponse;
import com.philipe.dasa.labor.labormanager.web.service.LaboratoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/laboratories")
public class LaboratoryController {

    @Autowired
    private LaboratoryService service;

    @Operation(summary = "List all laboratories")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful operation",
                            content =
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = LaboratoryResponse.class))))
            })
    @GetMapping("")
    public ResponseEntity<List<LaboratoryResponse>> index(@RequestParam(name = "examName",required = false) String examName) {
        List<LaboratoryResponse> entities;

        if (examName == null || examName.isEmpty()) {
            entities = service
                    .findAll()
                    .stream()
                    .map(laboratory -> LaboratoryResponse.of(laboratory, true))
                    .collect(Collectors.toList());
        } else {
            entities = service
                    .findByExamsNameIgnoreCase(examName)
                    .stream()
                    .map(laboratory -> LaboratoryResponse.of(laboratory, true))
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
                                    schema = @Schema(implementation = LaboratoryResponse.class))),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not found",
                            content =
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad request",
                            content =
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)))
            })
    @GetMapping("/{id}")
    public ResponseEntity<LaboratoryResponse> show(@PathVariable("id") UUID id) {
        return service
                .findById(id)
                .map(laboratory -> ResponseEntity.ok().body(LaboratoryResponse.of(laboratory, true)))
                .orElseThrow(
                        () ->
                                new ResponseStatusException(
                                        HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.getReasonPhrase()));
    }

    @Operation(summary = "Create a new laboratory")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Successful operation",
                            content =
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = LaboratoryResponse.class))),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad request",
                            content =
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)))
            })
    @PostMapping("")
    public ResponseEntity<LaboratoryResponse> create(@Valid @RequestBody LaboratoryForm form) {
        Laboratory laboratory =
                Laboratory.builder()
                        .name(form.getName())
                        .address(form.getAddress())
                        .build();

        Laboratory created = service.save(laboratory);
        return ResponseEntity.created(URI.create("/laboratorys/" + created.getId()))
                .body(LaboratoryResponse.of(created, true));
    }

    @Operation(summary = "Update an laboratory")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful operation",
                            content =
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = LaboratoryResponse.class))),
                    @ApiResponse(
                            responseCode = "422",
                            description = "Unprocessable Entity",
                            content =
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not found",
                            content =
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad request",
                            content =
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)))
            })
    @PutMapping("/{id}")
    public ResponseEntity<LaboratoryResponse> update(
            @PathVariable("id") UUID id, @Valid @RequestBody LaboratoryForm form) {
        return service
                .findById(id)
                .map(
                        laboratory -> {
                            laboratory.setName(form.getName());
                            laboratory.setAddress(form.getAddress());

                            Laboratory updated = service.save(laboratory);
                            return ResponseEntity.ok().body(LaboratoryResponse.of(updated, true));
                        })
                .orElseThrow(
                        () ->
                                new ResponseStatusException(
                                        HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.getReasonPhrase()));
    }

    @Operation(summary = "Delete an laboratory")
    @ApiResponses(value = {@ApiResponse(responseCode = "204", description = "Successful operation")})
    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") UUID id) {
        Optional<Laboratory> laboratory = service.findById(id);

        if (laboratory.isPresent()) {
            service.delete(laboratory.get());
        }
    }

    @Operation(summary = "Batch create a laboratories")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Successful operation",
                            content =
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = LaboratoryResponse.class)))),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad request",
                            content =
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)))
            })
    @PostMapping("/batch-create")
    public ResponseEntity<List<LaboratoryResponse>> create(@Valid @RequestBody List<LaboratoryBatchForm> forms) {
        List<Laboratory> savedBatch = service.saveAll(forms.stream().map(form -> Laboratory.builder()
                .name(form.getName())
                .address(form.getAddress())
                .build()
        ).collect(Collectors.toList()));
        return ResponseEntity.created(URI.create("batches-laboratories"))
                .body(savedBatch.stream().map(created-> LaboratoryResponse.of(created, true)).collect(Collectors.toList()));
    }

    @Operation(summary = "Batch update a laboratories")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful operation",
                            content =
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = LaboratoryResponse.class)))),
                    @ApiResponse(
                            responseCode = "422",
                            description = "Unprocessable Entity",
                            content =
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not found",
                            content =
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad request",
                            content =
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)))
            })
    @PostMapping("/batch-update")
    public ResponseEntity<List<LaboratoryResponse>> update(@Valid @RequestBody List<LaboratoryBatchForm> forms) {
        List<Laboratory> savedBatch = service.saveAll(forms.stream().map(form -> Laboratory.builder()
                .id(form.getId())
                .name(form.getName())
                .address(form.getAddress())
                .build()
        ).collect(Collectors.toList()));
        List<LaboratoryResponse> response = savedBatch.stream()
                .map(saved-> LaboratoryResponse.of(saved, true)).collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Batch delete a laboratories")
    @ApiResponses(value = {@ApiResponse(responseCode = "204", description = "Successful operation")})
    @PostMapping("/batch-delete")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@RequestBody List<UUID> ids) {
        List<Laboratory> examsToDelete = ids.stream().map(id -> service.findById(id)
                .orElseThrow(
                        () ->
                                new ResponseStatusException(
                                        HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.getReasonPhrase()))
        ).collect(Collectors.toList());
        service.deleteAll(examsToDelete);
    }

    @Operation(summary = "connect exams to a laboratory")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful operation",
                            content =
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ExamResponse.class))),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not found",
                            content =
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad request",
                            content =
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)))
            })
    @PostMapping("/{id}/connect")
    public ResponseEntity<LaboratoryResponse> connectLabsToLaboratory(@PathVariable("id") UUID id,  @RequestBody List<UUID> examsIds) {
        Laboratory addLaboratory = service.connect(id, examsIds).orElseThrow(
                () ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.getReasonPhrase()));
        return ResponseEntity.ok().body(LaboratoryResponse.of(addLaboratory, true));
    }

    @Operation(summary = "disconnect exams from a laboratory")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful operation",
                            content =
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ExamResponse.class))),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not found",
                            content =
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad request",
                            content =
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)))
            })
    @PostMapping("/{id}/disconnect")
    public ResponseEntity<LaboratoryResponse> disconnectLabsFromLaboratory(@PathVariable("id") UUID id, @RequestBody List<UUID> examsIds) {
        Laboratory removedLaboratory = service.disconnect(id, examsIds).orElseThrow(
                () ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.getReasonPhrase()));;
        return ResponseEntity.ok().body(LaboratoryResponse.of(removedLaboratory, true));
    }
}

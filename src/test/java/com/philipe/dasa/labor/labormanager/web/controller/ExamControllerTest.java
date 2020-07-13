package com.philipe.dasa.labor.labormanager.web.controller;

import static java.util.UUID.randomUUID;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.philipe.dasa.labor.labormanager.web.domain.ExamKind;
import com.philipe.dasa.labor.labormanager.web.entity.Exam;
import com.philipe.dasa.labor.labormanager.web.form.ExamBatchForm;
import com.philipe.dasa.labor.labormanager.web.form.ExamForm;
import com.philipe.dasa.labor.labormanager.web.response.ExamResponse;
import com.philipe.dasa.labor.labormanager.web.service.ExamService;
import com.philipe.dasa.labor.labormanager.web.service.LaboratoryService;
import java.util.*;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest
public class ExamControllerTest {
  @Autowired private ObjectMapper mapper;

  @Autowired private MockMvc mockMvc;

  @MockBean private ExamService service;

  @MockBean private LaboratoryService examService;

  private Exam e1;
  private Exam e2;
  private Exam e3;

  @BeforeEach
  public void set_up() {
    e1 =
        Exam.builder()
            .id(randomUUID())
            .name("Exam1")
            .type(ExamKind.IMAGEM)
            .laboratories(new ArrayList<>())
            .build();
    e2 =
        Exam.builder()
            .id(randomUUID())
            .name("Exam2")
            .type(ExamKind.ANALISE_CLINICA)
            .laboratories(new ArrayList<>())
            .build();
    e3 =
        Exam.builder()
            .id(randomUUID())
            .name("Exam3")
            .type(ExamKind.ANALISE_CLINICA)
            .laboratories(new ArrayList<>())
            .build();
  }

  @Test
  public void test_index() throws Exception {
    List<Exam> examEntities = Arrays.asList(e1, e2);
    doReturn(examEntities).when(service).findAll();

    List<ExamResponse> examResponses =
        examEntities
            .stream()
            .map((exam) -> ExamResponse.of(exam, true))
            .collect(Collectors.toList());

    this.mockMvc
        .perform(get("/exams"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[*]", hasSize(2)))
        .andExpect(jsonPath("$[0].id", is(examResponses.get(0).getId())))
        .andExpect(jsonPath("$[0].name", is(examResponses.get(0).getName())))
        .andExpect(jsonPath("$[0].type", is(examResponses.get(0).getType().value())))
        .andExpect(jsonPath("$[0].laboratories", is(examResponses.get(1).getLaboratories())))
        .andExpect(jsonPath("$[1].id", is(examResponses.get(1).getId())))
        .andExpect(jsonPath("$[1].name", is(examResponses.get(1).getName())))
        .andExpect(jsonPath("$[1].type", is(examResponses.get(1).getType().value())))
        .andExpect(jsonPath("$[1].laboratories", is(examResponses.get(1).getLaboratories())));
  }

  @Test
  public void test_get() throws Exception {
    UUID id = e1.getId();

    doReturn(Optional.of(e1)).when(service).findById(id);

    ExamResponse examResponse = ExamResponse.of(e1, true);

    this.mockMvc
        .perform(get("/exams/{id}", id))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(examResponse.getId())))
        .andExpect(jsonPath("$.name", is(examResponse.getName())))
        .andExpect(jsonPath("$.type", is(examResponse.getType().value())))
        .andExpect(jsonPath("$.laboratories", is(examResponse.getLaboratories())));
  }

  @Test
  public void test_create() throws Exception {

    doReturn(e1).when(service).save(Mockito.any(Exam.class));

    ExamResponse examResponse = ExamResponse.of(e1, true);

    ExamForm form = new ExamForm(e1.getName(), e1.getType());

    this.mockMvc
        .perform(
            post("/exams")
                .content(mapper.writeValueAsString(form))
                .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id", is(examResponse.getId())))
        .andExpect(jsonPath("$.name", is(examResponse.getName())))
        .andExpect(jsonPath("$.type", is(examResponse.getType().value())))
        .andExpect(jsonPath("$.laboratories", is(examResponse.getLaboratories())));
  }

  @Test
  public void test_update() throws Exception {
    UUID id = e1.getId();
    doReturn(Optional.of(e1)).when(service).findById(id);

    Exam updated =
        Exam.builder()
            .id(id)
            .name(e1.getName())
            .type(ExamKind.ANALISE_CLINICA)
            .laboratories(new ArrayList<>())
            .build();

    doReturn(updated).when(service).save(Mockito.any(Exam.class));

    ExamResponse examResponse = ExamResponse.of(updated, true);

    ExamForm form = new ExamForm(e1.getName(), ExamKind.ANALISE_CLINICA);

    this.mockMvc
        .perform(
            put("/exams/{id}", id)
                .content(mapper.writeValueAsString(form))
                .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(examResponse.getId())))
        .andExpect(jsonPath("$.name", is(examResponse.getName())))
        .andExpect(jsonPath("$.type", is(examResponse.getType().value())))
        .andExpect(jsonPath("$.laboratories", is(examResponse.getLaboratories())));
  }

  @Test
  public void test_delete() throws Exception {
    UUID id = e1.getId();

    doReturn(Optional.of(e1)).when(service).findById(id);
    doNothing().when(service).delete(e1);

    this.mockMvc
        .perform(delete("/exams/{id}", id))
        .andDo(print())
        .andExpect(status().isNoContent());
  }

  @Test
  public void batch_test_create() throws Exception {
    List<Exam> entities = Arrays.asList(e1, e2, e3);

    List<ExamResponse> responses =
        entities.stream().map(exam -> ExamResponse.of(exam, true)).collect(Collectors.toList());

    List<ExamForm> forms =
        entities
            .stream()
            .map(exam -> new ExamForm(exam.getName(), exam.getType()))
            .collect(Collectors.toList());

    doReturn(entities).when(service).saveAll(Mockito.<Exam>anyList());

    this.mockMvc
        .perform(
            post("/exams/batch-create")
                .content(mapper.writeValueAsString(forms))
                .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$[*]", hasSize(3)))
        .andExpect(jsonPath("$[0].id", is(responses.get(0).getId())))
        .andExpect(jsonPath("$[0].name", is(responses.get(0).getName())))
        .andExpect(jsonPath("$[0].type", is(responses.get(0).getType().value())))
        .andExpect(jsonPath("$[0].laboratories", is(responses.get(0).getLaboratories())))
        .andExpect(jsonPath("$[1].id", is(responses.get(1).getId())))
        .andExpect(jsonPath("$[1].name", is(responses.get(1).getName())))
        .andExpect(jsonPath("$[1].type", is(responses.get(1).getType().value())))
        .andExpect(jsonPath("$[1].laboratories", is(responses.get(1).getLaboratories())))
        .andExpect(jsonPath("$[2].id", is(responses.get(2).getId())))
        .andExpect(jsonPath("$[2].name", is(responses.get(2).getName())))
        .andExpect(jsonPath("$[2].type", is(responses.get(2).getType().value())))
        .andExpect(jsonPath("$[2].laboratories", is(responses.get(2).getLaboratories())));
  }

  @Test
  public void batch_test_update() throws Exception {
    List<Exam> entities = Arrays.asList(e1, e2, e3);

    List<ExamResponse> updateds =
        entities
            .stream()
            .map(
                exam -> {
                  exam.setName(exam.getName() + "_updated");
                  return ExamResponse.of(exam, true);
                })
            .collect(Collectors.toList());

    List<ExamBatchForm> forms =
        entities
            .stream()
            .map(exam -> new ExamBatchForm(exam.getId(), exam.getName(), exam.getType()))
            .collect(Collectors.toList());

    doReturn(entities).when(service).saveAll(Mockito.<Exam>anyList());

    this.mockMvc
        .perform(
            post("/exams/batch-update")
                .content(mapper.writeValueAsString(forms))
                .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[*]", hasSize(3)))
        .andExpect(jsonPath("$[0].id", is(updateds.get(0).getId())))
        .andExpect(jsonPath("$[0].name", is(updateds.get(0).getName())))
        .andExpect(jsonPath("$[0].type", is(updateds.get(0).getType().value())))
        .andExpect(jsonPath("$[0].laboratories", is(updateds.get(0).getLaboratories())))
        .andExpect(jsonPath("$[1].id", is(updateds.get(1).getId())))
        .andExpect(jsonPath("$[1].name", is(updateds.get(1).getName())))
        .andExpect(jsonPath("$[1].type", is(updateds.get(1).getType().value())))
        .andExpect(jsonPath("$[1].laboratories", is(updateds.get(1).getLaboratories())))
        .andExpect(jsonPath("$[2].id", is(updateds.get(2).getId())))
        .andExpect(jsonPath("$[2].name", is(updateds.get(2).getName())))
        .andExpect(jsonPath("$[2].type", is(updateds.get(2).getType().value())))
        .andExpect(jsonPath("$[2].laboratories", is(updateds.get(2).getLaboratories())));
  }

  @Test
  public void batch_test_delete() throws Exception {
    List<UUID> entities = Arrays.asList(e1.getId(), e2.getId(), e3.getId());

    doReturn(Optional.of(e1)).when(service).findById(entities.get(0));
    doReturn(Optional.of(e2)).when(service).findById(entities.get(1));
    doReturn(Optional.of(e3)).when(service).findById(entities.get(2));

    doNothing().when(service).deleteAll(Mockito.<Exam>anyList());

    this.mockMvc
        .perform(
            post("/exams/batch-delete")
                .content(mapper.writeValueAsString(entities))
                .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isNoContent());
  }
}

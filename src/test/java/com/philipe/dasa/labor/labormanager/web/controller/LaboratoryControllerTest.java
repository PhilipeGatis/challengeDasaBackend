package com.philipe.dasa.labor.labormanager.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.philipe.dasa.labor.labormanager.web.entity.Laboratory;
import com.philipe.dasa.labor.labormanager.web.form.LaboratoryBatchForm;
import com.philipe.dasa.labor.labormanager.web.form.LaboratoryForm;
import com.philipe.dasa.labor.labormanager.web.response.LaboratoryResponse;
import com.philipe.dasa.labor.labormanager.web.service.ExamService;
import com.philipe.dasa.labor.labormanager.web.service.LaboratoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.UUID.randomUUID;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class LaboratoryControllerTest {
    @Autowired
    private ObjectMapper mapper;

    @Autowired private MockMvc mockMvc;

    @MockBean
    private LaboratoryService service;

    @MockBean
    private ExamService examService;

    private Laboratory l1;
    private Laboratory l2;
    private Laboratory l3;

    @BeforeEach
    public void set_up() {
        l1 = Laboratory.builder().id(randomUUID()).name("Lab1").address("Street 1").exams(new ArrayList<>()).build();
        l2 = Laboratory.builder().id(randomUUID()).name("Lab2").address("Street 2").exams(new ArrayList<>()).build();
        l3 = Laboratory.builder().id(randomUUID()).name("Lab3").address("Street 3").exams(new ArrayList<>()).build();
    }

    @Test
    public void test_index() throws Exception {
        List<Laboratory> laboratoryEntities = Arrays.asList(l1, l2);
        doReturn(laboratoryEntities).when(service).findAll();

        List<LaboratoryResponse> laboratoryResponses =
                laboratoryEntities.stream().map((laboratory) -> LaboratoryResponse.of(laboratory, true)).collect(Collectors.toList());

        this.mockMvc
                .perform(get("/laboratories"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*]", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(laboratoryResponses.get(0).getId())))
                .andExpect(jsonPath("$[0].name", is(laboratoryResponses.get(0).getName())))
                .andExpect(jsonPath("$[0].exams", is(laboratoryResponses.get(0).getExams())))
                .andExpect(jsonPath("$[1].id", is(laboratoryResponses.get(1).getId())))
                .andExpect(jsonPath("$[1].name", is(laboratoryResponses.get(1).getName())))
                .andExpect(jsonPath("$[1].exams", is(laboratoryResponses.get(1).getExams())));
    }

    @Test
    public void test_get() throws Exception {
        UUID id = l1.getId();

        doReturn(Optional.of(l1)).when(service).findById(id);

        LaboratoryResponse laboratoryResponse = LaboratoryResponse.of(l1, true);

        this.mockMvc
                .perform(get("/laboratories/{id}", id))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(laboratoryResponse.getId())))
                .andExpect(jsonPath("$.name", is(laboratoryResponse.getName())))
                .andExpect(jsonPath("$.exams", is(laboratoryResponse.getExams())));
    }

    @Test
    public void test_create() throws Exception {
        doReturn(l1).when(service).save(Mockito.any(Laboratory.class));

        LaboratoryResponse laboratoryResponse = LaboratoryResponse.of(l1, true);
        LaboratoryForm form = new LaboratoryForm(l1.getName(), l1.getAddress());

        this.mockMvc
                .perform(
                        post("/laboratories")
                                .content(mapper.writeValueAsString(form))
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(laboratoryResponse.getId())))
                .andExpect(jsonPath("$.name", is(laboratoryResponse.getName())))
                .andExpect(jsonPath("$.exams", is(laboratoryResponse.getExams())));
    }

    @Test
    public void test_update() throws Exception {
        UUID id = l1.getId();
        doReturn(Optional.of(l1)).when(service).findById(id);

        Laboratory updated =
                Laboratory.builder()
                        .id(id)
                        .name(l1.getName())
                        .address("Street updated")
                        .exams(new ArrayList<>())
                        .build();

        doReturn(updated).when(service).save(Mockito.any(Laboratory.class));

        LaboratoryResponse laboratoryResponse = LaboratoryResponse.of(updated, true);

        LaboratoryForm form = new LaboratoryForm(l1.getName(), "Street updated");

        this.mockMvc
                .perform(
                        put("/laboratories/{id}", id)
                                .content(mapper.writeValueAsString(form))
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(laboratoryResponse.getId())))
                .andExpect(jsonPath("$.name", is(laboratoryResponse.getName())))
                .andExpect(jsonPath("$.address", is(laboratoryResponse.getAddress())))
                .andExpect(jsonPath("$.exams", is(laboratoryResponse.getExams())));
    }

    @Test
    public void test_delete() throws Exception {
        UUID id = l1.getId();

        doReturn(Optional.of(l1)).when(service).findById(id);
        doNothing().when(service).delete(l1);

        this.mockMvc
                .perform(delete("/laboratories/{id}", id))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    public void batch_test_create() throws Exception {
        List<Laboratory> entities = Arrays.asList(l1, l2, l3);

        List<LaboratoryResponse> responses = entities.stream()
                .map(laboratory -> LaboratoryResponse.of(laboratory, true)).collect(Collectors.toList());

        List<LaboratoryForm> forms = entities.stream()
                .map(laboratory -> new LaboratoryForm(laboratory.getName(), laboratory.getAddress())).collect(Collectors.toList());

        doReturn(entities).when(service).saveAll(Mockito.<Laboratory>anyList());

        this.mockMvc
                .perform(
                        post("/laboratories/batch-create")
                                .content(mapper.writeValueAsString(forms))
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$[*]", hasSize(3)))
                .andExpect(jsonPath("$[0].id", is(responses.get(0).getId())))
                .andExpect(jsonPath("$[0].name", is(responses.get(0).getName())))
                .andExpect(jsonPath("$[0].address", is(responses.get(0).getAddress())))
                .andExpect(jsonPath("$[0].exams", is(responses.get(0).getExams())))
                .andExpect(jsonPath("$[1].id", is(responses.get(1).getId())))
                .andExpect(jsonPath("$[1].name", is(responses.get(1).getName())))
                .andExpect(jsonPath("$[1].address", is(responses.get(1).getAddress())))
                .andExpect(jsonPath("$[1].exams", is(responses.get(1).getExams())))
                .andExpect(jsonPath("$[2].id", is(responses.get(2).getId())))
                .andExpect(jsonPath("$[2].name", is(responses.get(2).getName())))
                .andExpect(jsonPath("$[2].address", is(responses.get(2).getAddress())))
                .andExpect(jsonPath("$[2].exams", is(responses.get(2).getExams())));
    }

    @Test
    public void batch_test_update() throws Exception {
        List<Laboratory> entities = Arrays.asList(l1, l2, l3);

        List<LaboratoryResponse> updateds = entities.stream()
                .map(laboratory -> {
                    laboratory.setName(laboratory.getName() +  "_updated");
                    return LaboratoryResponse.of(laboratory, true);
                }).collect(Collectors.toList());

        List<LaboratoryBatchForm> forms = entities.stream()
                .map(laboratory -> new LaboratoryBatchForm(laboratory.getId(), laboratory.getName(), laboratory.getAddress())).collect(Collectors.toList());

        doReturn(entities).when(service).saveAll(Mockito.<Laboratory>anyList());

        this.mockMvc
                .perform(
                        post("/laboratories/batch-update")
                                .content(mapper.writeValueAsString(forms))
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*]", hasSize(3)))
                .andExpect(jsonPath("$[0].id", is(updateds.get(0).getId())))
                .andExpect(jsonPath("$[0].name", is(updateds.get(0).getName())))
                .andExpect(jsonPath("$[0].address", is(updateds.get(0).getAddress())))
                .andExpect(jsonPath("$[0].exams", is(updateds.get(0).getExams())))
                .andExpect(jsonPath("$[1].id", is(updateds.get(1).getId())))
                .andExpect(jsonPath("$[1].name", is(updateds.get(1).getName())))
                .andExpect(jsonPath("$[1].address", is(updateds.get(1).getAddress())))
                .andExpect(jsonPath("$[1].exams", is(updateds.get(1).getExams())))
                .andExpect(jsonPath("$[2].id", is(updateds.get(2).getId())))
                .andExpect(jsonPath("$[2].name", is(updateds.get(2).getName())))
                .andExpect(jsonPath("$[2].address", is(updateds.get(2).getAddress())))
                .andExpect(jsonPath("$[2].exams", is(updateds.get(2).getExams())));
    }

    @Test
    public void batch_test_delete() throws Exception {
        List<UUID> entities = Arrays.asList(l1.getId(), l2.getId(), l3.getId());

        doReturn(Optional.of(l1)).when(service).findById(entities.get(0));
        doReturn(Optional.of(l2)).when(service).findById(entities.get(1));
        doReturn(Optional.of(l3)).when(service).findById(entities.get(2));

        doNothing().when(service).deleteAll(Mockito.<Laboratory>anyList());

        this.mockMvc
                .perform(post("/laboratories/batch-delete")
                        .content(mapper.writeValueAsString(entities))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());
    }
}

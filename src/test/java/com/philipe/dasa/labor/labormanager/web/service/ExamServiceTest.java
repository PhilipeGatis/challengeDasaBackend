package com.philipe.dasa.labor.labormanager.web.service;

import com.philipe.dasa.labor.labormanager.web.domain.ExamKind;
import com.philipe.dasa.labor.labormanager.web.entity.Exam;
import com.philipe.dasa.labor.labormanager.web.repository.ExamRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.*;

import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ExamServiceTest {

    @Autowired ExamService service;

    @MockBean
    ExamRepository repository;

    @Test
    void test_list() {
        Exam e1 =
                Exam.builder()
                        .id(randomUUID())
                        .name("Exam1")
                        .type(ExamKind.IMAGEM)
                        .build();
        Exam e2 =
                Exam.builder()
                        .id(randomUUID())
                        .name("Exam2")
                        .type(ExamKind.IMAGEM)
                        .build();


        doReturn(Arrays.asList(e1, e2)).when(repository).findAll();

        List<Exam> list = service.findAll();

        assertEquals(2, list.size());
        assertEquals(e1, list.get(0));
        assertEquals(e2, list.get(1));
    }

    @Test
    void test_findById_when_matches() {
        UUID id = randomUUID();
        Exam e1 =
                Exam.builder()
                        .id(randomUUID())
                        .name("Exam1")
                        .type(ExamKind.IMAGEM)
                        .build();

        doReturn(Optional.of(e1)).when(repository).findById(id);

        Optional<Exam> exam = service.findById(id);

        assertEquals(e1, exam.get());
    }

    @Test
    void test_findByNameIgnoreCase_when_matches() {
        String name = "Exam1";
        Exam e1 =
                Exam.builder()
                        .id(randomUUID())
                        .name(name)
                        .type(ExamKind.IMAGEM)
                        .build();

        doReturn(Collections.singletonList(e1)).when(repository).findByNameIgnoreCase(name);

        List<Exam> exams = service.findByNameIgnoreCase(name);

        assertEquals(e1, exams.get(0));
    }

    @Test
    void test_findById_when_does_not_match() {
        UUID id = randomUUID();
        Exam e1 =
                Exam.builder()
                        .id(randomUUID())
                        .name("Exam1")
                        .type(ExamKind.IMAGEM)
                        .build();

        doReturn(Optional.of(e1)).when(repository).findById(id);

        Optional<Exam> exam = service.findById(randomUUID());

        assertTrue(!exam.isPresent());
    }

    @Test
    void test_findByNameIgnoreCase_when_does_not_match() {
        UUID id = randomUUID();
        String name = "Exam1";
        Exam e1 =
                Exam.builder()
                        .id(randomUUID())
                        .name(name)
                        .type(ExamKind.IMAGEM)
                        .build();

        doReturn(Collections.singletonList(e1)).when(repository).findByNameIgnoreCase(name);

        List<Exam> exams = service.findByNameIgnoreCase("Exam2");

        assertTrue(exams.isEmpty());
    }

    @Test
    void test_save() {
        Exam e1 =
                Exam.builder()
                        .id(randomUUID())
                        .name("Exam1")
                        .type(ExamKind.IMAGEM)
                        .build();

        doReturn(e1).when(repository).save(e1);

        Exam saved = service.save(e1);

        assertEquals(e1, saved);
    }

    @Test
    void test_delete() {
        Exam e1 =
                Exam.builder()
                        .id(randomUUID())
                        .name("Exam1")
                        .type(ExamKind.IMAGEM)
                        .build();

        doNothing().when(repository).delete(e1);

        service.delete(e1);

        verify(repository).delete(e1);
    }
}

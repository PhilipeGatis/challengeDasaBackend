package com.philipe.dasa.labor.labormanager.web.service;

import com.philipe.dasa.labor.labormanager.web.entity.Laboratory;
import com.philipe.dasa.labor.labormanager.web.repository.LaboratoryRepository;
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
public class LaboratoryServiceTest {

    @Autowired LaboratoryService service;

    @MockBean
    LaboratoryRepository repository;

    @Test
    void test_list() {
        Laboratory l1 =
                Laboratory.builder()
                        .id(randomUUID())
                        .name("Lab1")
                        .address("Rua 1")
                        .build();
        Laboratory l2 =
                Laboratory.builder()
                        .id(randomUUID())
                        .name("Lab2")
                        .address("Rua 2")
                        .build();


        doReturn(Arrays.asList(l1, l2)).when(repository).findAll();

        List<Laboratory> list = service.findAll();

        assertEquals(2, list.size());
        assertEquals(l1, list.get(0));
        assertEquals(l2, list.get(1));
    }

    @Test
    void test_findById_when_matches() {
        UUID id = randomUUID();
        Laboratory l1 =
                Laboratory.builder()
                        .id(randomUUID())
                        .name("Lab1")
                        .address("Rua 1")
                        .build();

        doReturn(Optional.of(l1)).when(repository).findById(id);

        Optional<Laboratory> exam = service.findById(id);

        assertEquals(l1, exam.get());
    }

    @Test
    void test_findByNameIgnoreCase_when_matches() {
        String name = "Lab1";
        Laboratory l1 =
                Laboratory.builder()
                        .id(randomUUID())
                        .name(name)
                        .address("Rua 1")
                        .build();

        doReturn(Collections.singletonList(l1)).when(repository).findByNameIgnoreCase(name);

        List<Laboratory> labs = service.findByNameIgnoreCase(name);

        assertEquals(l1, labs.get(0));
    }

    @Test
    void test_findById_when_does_not_match() {
        UUID id = randomUUID();
        Laboratory l1 =
                Laboratory.builder()
                        .id(randomUUID())
                        .name("Lab1")
                        .address("Rua 1")
                        .build();

        doReturn(Optional.of(l1)).when(repository).findById(id);

        Optional<Laboratory> exam = service.findById(randomUUID());

        assertTrue(!exam.isPresent());
    }

    @Test
    void test_findByNameIgnoreCase_when_does_not_match() {
        UUID id = randomUUID();
        String name = "Lab1";
        Laboratory l1 =
                Laboratory.builder()
                        .id(randomUUID())
                        .name(name)
                        .address("Rua 1")
                        .build();

        doReturn(Collections.singletonList(l1)).when(repository).findByNameIgnoreCase(name);

        List<Laboratory> labs = service.findByNameIgnoreCase("Laboratory2");

        assertTrue(labs.isEmpty());
    }

    @Test
    void test_save() {
        Laboratory l1 =
                Laboratory.builder()
                        .id(randomUUID())
                        .name("Lab1")
                        .address("Rua 1")
                        .build();

        doReturn(l1).when(repository).save(l1);

        Laboratory saved = service.save(l1);

        assertEquals(l1, saved);
    }

    @Test
    void test_delete() {
        Laboratory l1 =
                Laboratory.builder()
                        .id(randomUUID())
                        .name("Lab1")
                        .address("Rua 1")
                        .build();

        doNothing().when(repository).delete(l1);

        service.delete(l1);

        verify(repository).delete(l1);
    }
}

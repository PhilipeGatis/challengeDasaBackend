package com.philipe.dasa.labor.labormanager.web.repository;

import com.philipe.dasa.labor.labormanager.web.entity.Laboratory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class LaboratoryRepositoryTest {

    @Autowired
    LaboratoryRepository repository;

    @Test
    @Sql("/sqls/laboratory/populate_with_data.sql")
    void test_find_by_name() {
        List<Laboratory> labs = repository.findByNameIgnoreCase("Lab1");
        assertTrue(!labs.isEmpty());
    }

    @Test
    @Sql("/sqls/laboratory/populate_with_data.sql")
    void test_find_by_name_case_insensitive() {
        List<Laboratory> labs = repository.findByNameIgnoreCase("lab1");
        assertTrue(!labs.isEmpty());
    }

    @Test
    @Sql("/sqls/laboratory/populate_with_data.sql")
    void test_soft_delete() {
        List<Laboratory> labs = repository.findByNameIgnoreCase("Lab1");
        repository.delete(labs.get(0));
        List<Laboratory> delLabs = repository.findByNameIgnoreCase("Lab1");
        assertTrue(delLabs.isEmpty());
    }

    @Test
    @Sql("/sqls/laboratory/populate_with_data.sql")
    void test_find_all_soft_delete() {
        List<Laboratory> laboratories = repository.findAll();
        assertTrue(!laboratories.isEmpty());
        assertEquals(2, laboratories.size());
    }

    @Test
    @Sql("/sqls/laboratory/populate_with_data.sql")
    void test_find_all_by_ids() {
        List<Laboratory> laboratories = repository.findAll();
        List<Laboratory> findedLaboratories = repository.findAllByIds(laboratories.stream().map(lab -> lab.getId()).collect(Collectors.toList()));
        assertTrue(!findedLaboratories.isEmpty());
    }
}

package com.philipe.dasa.labor.labormanager.web.repository;

import com.philipe.dasa.labor.labormanager.web.entity.Exam;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ExamRepositoryTest {
    @Autowired
    ExamRepository repository;

    @Test
    @Sql("/sqls/exam/populate_with_data.sql")
    void test_find_by_name() {
        List<Exam> exam = repository.findByNameIgnoreCase("Exam1");
        assertTrue(!exam.isEmpty());
    }

    @Test
    @Sql("/sqls/exam/populate_with_data.sql")
    void test_find_by_name_case_insensitive() {
        List<Exam> exam = repository.findByNameIgnoreCase("exam1");
        assertTrue(!exam.isEmpty());
    }

    @Test
    @Sql("/sqls/exam/populate_with_data.sql")
    void test_soft_delete() {
        List<Exam> exams = repository.findByNameIgnoreCase("Exam1");
        repository.delete(exams.get(0));
        List<Exam> deletedExam = repository.findByNameIgnoreCase("Exam1");
        assertTrue(deletedExam.isEmpty());
    }

    @Test
    @Sql("/sqls/exam/populate_with_data.sql")
    void test_find_all_soft_delete() {
        List<Exam> exams = repository.findAll();
        assertTrue(!exams.isEmpty());
        assertEquals(2, exams.size());
    }

    @Test
    @Sql("/sqls/exam/populate_with_data.sql")
    void test_find_all_by_ids() {
        List<Exam> exams = repository.findAll();
        List<Exam> findedExams = repository.findAllByIds(exams.stream().map(exam -> exam.getId()).collect(Collectors.toList()));
        assertTrue(!findedExams.isEmpty());
    }
}

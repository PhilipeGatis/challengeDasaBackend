package com.philipe.dasa.labor.labormanager.web.service;

import com.philipe.dasa.labor.labormanager.web.entity.Exam;
import com.philipe.dasa.labor.labormanager.web.entity.Laboratory;
import com.philipe.dasa.labor.labormanager.web.repository.ExamRepository;
import com.philipe.dasa.labor.labormanager.web.repository.LaboratoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class LaboratoryService {

    @Autowired
    private LaboratoryRepository repository;

    @Autowired
    private ExamRepository examRepository;

    @Transactional
    public List<Laboratory> findAll() {
        return repository.findAll();
    }

    @Transactional
    public List<Laboratory> findByExamsNameIgnoreCase(String name) {
        return repository.findByExamsNameIgnoreCase(name);
    }

    @Transactional
    public Optional<Laboratory> findById(UUID id) {
        return repository.findById(id);
    }

    @Transactional
    public List<Laboratory> findByIds(List<UUID> ids) {
        return repository.findAllByIds(ids);
    }

    @Transactional
    public List<Laboratory> findByNameIgnoreCase(String name) {
        return repository.findByNameIgnoreCase(name);
    }

    @Transactional
    public Laboratory save(Laboratory laboratory) {
        return repository.save(laboratory);
    }

    @Transactional
    public List<Laboratory> saveAll(List<Laboratory> laboratories) {
        return repository.saveAll(laboratories);
    }

    @Transactional
    public void delete(Laboratory laboratory) {
        repository.delete(laboratory);
    }

    @Transactional
    public void deleteAll(List<Laboratory> laboratories) {
        repository.deleteAll(laboratories);
    }

    @Transactional
    public Optional<Laboratory> connect(UUID id ,List<UUID> examsIds) {
        Optional<Laboratory> laboratory = findById(id);

        if (!laboratory.isPresent()) {
            return laboratory;
        }

        examsIds.removeIf(uuid-> laboratory.get().getExams().stream().parallel().anyMatch(
                exam -> uuid.equals(exam.getId())
        ));

        List<Exam> exams = examRepository.findAllByIds(examsIds);

        laboratory.get().getExams().addAll(exams);

        return Optional.of(save(laboratory.get()));
    }

    @Transactional
    public Optional<Laboratory> disconnect(UUID id ,List<UUID> examsIds) {
        Optional<Laboratory> laboratory = findById(id);

        if (!laboratory.isPresent()) {
            return laboratory;
        }

        laboratory.get().getExams().removeIf(exam-> examsIds.stream().parallel().anyMatch(
                uuid -> uuid.equals(exam.getId())
        ));

        return Optional.of(save(laboratory.get()));
    }
}

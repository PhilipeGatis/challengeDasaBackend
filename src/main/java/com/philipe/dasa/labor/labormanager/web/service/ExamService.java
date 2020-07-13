package com.philipe.dasa.labor.labormanager.web.service;

import com.philipe.dasa.labor.labormanager.web.entity.Exam;
import com.philipe.dasa.labor.labormanager.web.entity.Laboratory;
import com.philipe.dasa.labor.labormanager.web.repository.ExamRepository;
import com.philipe.dasa.labor.labormanager.web.repository.LaboratoryRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExamService {

  @Autowired private ExamRepository repository;
  @Autowired private LaboratoryRepository laboratoryRepository;

  @Transactional
  public List<Exam> findAll() {
    return repository.findAll();
  }

  @Transactional
  public List<Exam> findByLaboratoriesNameIgnoreCase(String name) {
    return repository.findByLaboratoriesNameIgnoreCase(name);
  }

  @Transactional
  public Optional<Exam> findById(UUID id) {
    return repository.findById(id);
  }

  @Transactional
  public List<Exam> findByIds(List<UUID> ids) {
    return repository.findAllByIds(ids);
  }

  @Transactional
  public List<Exam> findByNameIgnoreCase(String name) {
    return repository.findByNameIgnoreCase(name);
  }

  @Transactional
  public Exam save(Exam exam) {
    return repository.save(exam);
  }

  @Transactional
  public List<Exam> saveAll(List<Exam> exams) {
    return repository.saveAll(exams);
  }

  @Transactional
  public void delete(Exam exam) {
    repository.delete(exam);
  }

  @Transactional
  public void deleteAll(List<Exam> exams) {
    repository.deleteAll(exams);
  }

  @Transactional
  public Optional<Exam> connect(UUID id, List<UUID> labsIds) {
    Optional<Exam> exam = findById(id);

    if (!exam.isPresent()) {
      return exam;
    }

    labsIds.removeIf(
        uuid ->
            exam.get()
                .getLaboratories()
                .stream()
                .parallel()
                .anyMatch(lab -> uuid.equals(lab.getId())));

    List<Laboratory> labs = laboratoryRepository.findAllByIds(labsIds);

    exam.get().getLaboratories().addAll(labs);

    return Optional.of(save(exam.get()));
  }

  @Transactional
  public Optional<Exam> disconnect(UUID id, List<UUID> labsIds) {
    Optional<Exam> exam = findById(id);

    if (!exam.isPresent()) {
      return exam;
    }

    exam.get()
        .getLaboratories()
        .removeIf(lab -> labsIds.stream().parallel().anyMatch(uuid -> uuid.equals(lab.getId())));

    return Optional.of(save(exam.get()));
  }
}

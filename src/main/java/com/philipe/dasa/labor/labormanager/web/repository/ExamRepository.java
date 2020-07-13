package com.philipe.dasa.labor.labormanager.web.repository;

import com.philipe.dasa.labor.labormanager.web.entity.Exam;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ExamRepository extends BaseRepository<Exam, UUID> {
  List<Exam> findByNameIgnoreCase(String name);

  @Query("select e from Exam e where e.id in :ids")
  List<Exam> findAllByIds(List<UUID> ids);

  List<Exam> findByLaboratoriesNameIgnoreCase(String name);
}

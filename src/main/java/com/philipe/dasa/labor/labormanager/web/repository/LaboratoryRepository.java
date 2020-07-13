package com.philipe.dasa.labor.labormanager.web.repository;

import com.philipe.dasa.labor.labormanager.web.entity.Laboratory;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface LaboratoryRepository extends BaseRepository<Laboratory, UUID> {
    List<Laboratory> findByNameIgnoreCase(String name);

    @Query( "select l from Laboratory l where l.id in :ids" )
    List<Laboratory> findAllByIds(List<UUID> ids);

    List<Laboratory> findByExamsNameIgnoreCase(String name);
}

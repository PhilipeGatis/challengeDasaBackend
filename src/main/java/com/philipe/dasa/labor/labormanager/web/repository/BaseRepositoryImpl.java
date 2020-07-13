package com.philipe.dasa.labor.labormanager.web.repository;

import java.io.Serializable;
import javax.persistence.EntityManager;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.transaction.annotation.Transactional;

public class BaseRepositoryImpl<T, ID extends Serializable> extends SimpleJpaRepository<T, ID>
    implements BaseRepository<T, ID> {

  private final EntityManager entityManager;

  public BaseRepositoryImpl(
      JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
    super(entityInformation, entityManager);
    this.entityManager = entityManager;
  }

  @Transactional
  @Override
  public <S extends T> S save(S entity) {
    S result = super.save(entity);
    entityManager.flush();
    entityManager.refresh(entity);
    return result;
  }

  @Transactional
  @Override
  public <S extends T> S saveAndFlush(S entity) {
    return save(entity);
  }
}

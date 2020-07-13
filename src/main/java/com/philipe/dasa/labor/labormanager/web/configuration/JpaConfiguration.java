package com.philipe.dasa.labor.labormanager.web.configuration;

import com.philipe.dasa.labor.labormanager.web.repository.BaseRepositoryImpl;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(
        repositoryBaseClass = BaseRepositoryImpl.class,
        basePackages = "com.philipe.dasa.labor.labormanager.web")
public class JpaConfiguration {}

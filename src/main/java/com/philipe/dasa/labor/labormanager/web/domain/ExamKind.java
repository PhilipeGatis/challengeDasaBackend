package com.philipe.dasa.labor.labormanager.web.domain;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public enum ExamKind {
    ANALISE_CLINICA("analise_clinica"),
    IMAGEM("imagem");

    private String description;

    ExamKind(String description) {
        this.description = description;
    }

    @JsonValue
    @JsonDeserialize
    public String value() {
        return description;
    }

    @Override
    public String toString() {
        return this.value();
    }
}

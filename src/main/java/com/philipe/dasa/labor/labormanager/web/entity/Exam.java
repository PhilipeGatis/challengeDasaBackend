package com.philipe.dasa.labor.labormanager.web.entity;

import com.philipe.dasa.labor.labormanager.web.domain.ActiveStatus;
import com.philipe.dasa.labor.labormanager.web.domain.ExamKind;
import lombok.*;
import org.hibernate.annotations.*;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "exam")
@Where(clause="status=0")
@SQLDelete(sql = "UPDATE exam SET status = 1 WHERE id = ?")
@Builder
@AllArgsConstructor
@NoArgsConstructor @Setter @Getter
public class Exam {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter
    private UUID id;

    @Column
    @NotNull
    @Builder.Default
    private int status = ActiveStatus.ACTIVE.value();

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column
    @NotBlank
    @NotNull
    private String name;

    @Column
    @Enumerated(value = EnumType.STRING)
    @NotBlank
    @NotNull
    private ExamKind type;

    @ManyToMany
    @JoinTable(
            name = "laboratory_exam",
            joinColumns = @JoinColumn(name = "exam_id"),
            inverseJoinColumns = @JoinColumn(name = "laboratory_id")
    )
    private List<Laboratory> laboratories;
}

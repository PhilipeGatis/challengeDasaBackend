package com.philipe.dasa.labor.labormanager.web.entity;

import com.philipe.dasa.labor.labormanager.web.domain.ActiveStatus;
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
@Table(name = "laboratory")
@Where(clause="status=0")
@SQLDelete(sql = "UPDATE laboratory SET status = 1 WHERE id = ?")
@Builder
@AllArgsConstructor
@NoArgsConstructor @Setter @Getter
public class Laboratory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter
    private UUID id;

    @Column
    @Builder.Default
    @NotNull
    private int status = ActiveStatus.ACTIVE.value();

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(nullable = false)
    @NotBlank
    private String name;

    @Column(nullable = false)
    @NotBlank
    private String address;

    @ManyToMany
    @JoinTable(
            name = "laboratory_exam",
            joinColumns = @JoinColumn(name = "laboratory_id"),
            inverseJoinColumns = @JoinColumn(name = "exam_id")
    )
    private List<Exam> exams;

    public Laboratory(String name, String address) {
        this.name = name;
        this.address = address;
    }

}

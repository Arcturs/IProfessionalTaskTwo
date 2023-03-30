package com.example.secretSanta.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "participant")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
public class Participant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String wish;
    private Long recipient;
}

package com.example.secretSanta.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
public class CreateParticipantRequest {

    @NotBlank
    private String name;

    private String wish;
}

package com.example.secretSanta.model.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
public class ParticipantWithRecipientResponse {

    private Long id;

    private String name;
    private String wish;

    private ParticipantResponse recipient;
}

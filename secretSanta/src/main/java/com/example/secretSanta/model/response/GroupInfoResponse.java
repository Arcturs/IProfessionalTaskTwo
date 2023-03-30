package com.example.secretSanta.model.response;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
public class GroupInfoResponse {

    private Long id;
    private String name;
    private String description;
    private List<ParticipantWithRecipientResponse> participants;
}

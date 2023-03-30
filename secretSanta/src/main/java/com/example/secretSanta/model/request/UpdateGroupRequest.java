package com.example.secretSanta.model.request;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
public class UpdateGroupRequest {
    private String name;

    private String description;
}

package com.zenika.liquid.democracy.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by pc on 08/03/2017.
 */
public class CollaboratorDto {

    @Getter
    @Setter
    private String collaboratorId;

    @Getter
    @Setter
    private String firstName;

    @Getter
    @Setter
    private String lastName;

    @Getter
    @Setter
    private String email;
}

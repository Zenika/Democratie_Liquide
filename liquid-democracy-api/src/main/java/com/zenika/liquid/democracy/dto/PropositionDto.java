package com.zenika.liquid.democracy.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by pc on 08/03/2017.
 */
public class PropositionDto {

    @Getter
    @Setter
    private String id;

    @Getter
    @Setter
    private String title;

    @Getter
    @Setter
    private String description;

    @Getter
    @Setter
    private int points;
}

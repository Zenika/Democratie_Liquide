package com.zenika.liquid.democracy.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by pc on 08/03/2017.
 */
public class CategoryDto {

    @Getter
    @Setter
    private String uuid;

    @Getter
    @Setter
    private String title;

    @Getter
    @Setter
    private String description;
}

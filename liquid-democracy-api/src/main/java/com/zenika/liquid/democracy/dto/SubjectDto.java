package com.zenika.liquid.democracy.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/**
 * Created by pc on 08/03/2017.
 */
public class SubjectDto {

    @Getter
    @Setter
    private String uuid;

    @Getter
    @Setter
    private String title;

    @Getter
    @Setter
    private String description;

    @Getter
    @Setter
    private int maxPoints;

    @Getter
    @Setter
    private Date deadLine;

    @Getter
    @Setter
    private Date submitDate;

    @Getter
    @Setter
    private List<PropositionDto> propositions;

    @Getter
    @Setter
    private Boolean isClosed;

    @Getter
    @Setter
    private Boolean isVoted;

    @Getter
    @Setter
    private Boolean isMine;

    @Getter
    @Setter
    private int voteCount;

    @Getter
    @Setter
    private String givenDelegation;

    @Getter
    @Setter
    private long receivedDelegations;

    @Getter
    @Setter
    private CategoryDto category;

    @Getter
    @Setter
    private ChannelDto channel;

}

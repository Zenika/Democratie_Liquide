package com.zenika.liquid.democracy.config;

import com.zenika.liquid.democracy.dto.*;
import com.zenika.liquid.democracy.model.*;
import com.zenika.si.core.zenika.model.Collaborator;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.ConfigurableMapper;
import org.springframework.stereotype.Component;

/**
 * Created by pc on 08/03/2017.
 */
@Component
public class MapperConfig extends ConfigurableMapper {

    @Override
    protected void configure(MapperFactory factory) {
        factory.classMap(Subject.class, SubjectDto.class).byDefault().register();
        factory.classMap(Category.class, CategoryDto.class).byDefault().register();
        factory.classMap(Channel.class, ChannelDto.class).byDefault().register();
        factory.classMap(Proposition.class, PropositionDto.class).byDefault().register();
        factory.classMap(Collaborator.class, CollaboratorDto.class).byDefault().register();
    }
}

package com.zenika.liquid.democracy.api.channel.service.impl;

import com.zenika.liquid.democracy.api.channel.exception.ExistingChannelException;
import com.zenika.liquid.democracy.api.channel.exception.UnexistingChannelException;
import com.zenika.liquid.democracy.api.channel.exception.UserNotInChannelException;
import com.zenika.liquid.democracy.api.channel.persistence.ChannelRepository;
import com.zenika.liquid.democracy.api.channel.service.ChannelService;
import com.zenika.liquid.democracy.api.channel.util.ChannelUtil;
import com.zenika.liquid.democracy.authentication.service.CollaboratorService;
import com.zenika.liquid.democracy.config.MapperConfig;
import com.zenika.liquid.democracy.dto.ChannelDto;
import com.zenika.liquid.democracy.model.Channel;
import com.zenika.si.core.zenika.model.Collaborator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ChannelServiceImpl implements ChannelService {

    private final ChannelRepository channelRepository;

    private final CollaboratorService collaboratorService;

    private final MapperConfig mapper;

    @Autowired
    public ChannelServiceImpl(ChannelRepository channelRepository, CollaboratorService collaboratorService, MapperConfig mapper) {
        this.channelRepository = channelRepository;
        this.collaboratorService = collaboratorService;
        this.mapper = mapper;
    }

    public ChannelDto addChannel(Channel newChannel) {
        // check channel not blank
        ChannelUtil.checkChannel(newChannel);

        // trim and lowerCase title
        newChannel.setTitle(newChannel.getTitle().toLowerCase().trim());

        // find duplicates
        Optional<Channel> c = channelRepository.findChannelByTitle(newChannel.getTitle());
        if (c.isPresent()) {
            throw new ExistingChannelException();
        }

        // join the channel
        this.joinChannel(newChannel);
        return prepareChannelForResponse(channelRepository.save(newChannel));
    }

    public List<ChannelDto> getChannels() {
        return channelRepository.findAll().stream()
                .map(this::prepareChannelForResponse)
                .collect(Collectors.toList());
    }

    public Channel getChannelByUuid(String channelUuid) {
        return channelRepository.findChannelByUuid(channelUuid)
                .orElseThrow(UnexistingChannelException::new);
    }

    public ChannelDto getChannelDtoByUuid(String channelUuid) {
        return prepareChannelForResponse(getChannelByUuid(channelUuid));
    }

    public void joinChannel(Channel c) {
        ChannelUtil.checkChannelForJoin(c, collaboratorService.currentUser().getCollaboratorId());
        c.getCollaborators().add(collaboratorService.currentUser());
        channelRepository.save(c);
    }

    public void quitChannel(Channel c) {
        Collaborator collaborator = c.findCollaborator(collaboratorService.currentUser().getCollaboratorId())
                .orElseThrow(UserNotInChannelException::new);
        c.removeCollaborator(collaborator);
        channelRepository.save(c);
    }


    private ChannelDto prepareChannelForResponse(Channel c) {
        return mapper.map(c, ChannelDto.class);
    }

}

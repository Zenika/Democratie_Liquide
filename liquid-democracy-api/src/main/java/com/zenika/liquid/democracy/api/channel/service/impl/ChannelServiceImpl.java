package com.zenika.liquid.democracy.api.channel.service.impl;

import com.zenika.liquid.democracy.api.channel.exception.*;
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

    @Autowired
    private ChannelRepository channelRepository;

    @Autowired
    private CollaboratorService collaboratorService;

    @Autowired
    MapperConfig mapper;

    public ChannelDto addChannel(Channel newChannel) throws MalformedChannelException, UserAlreadyInChannelException, ExistingChannelException {

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
        return channelRepository.findAll().stream().map(c -> prepareChannelForResponse(c)).collect(Collectors.toList());
    }

    public Channel getChannelByUuid(String channelUuid) throws UnexistingChannelException {
        return getChannel(channelUuid);
    }

    public ChannelDto getChannelDtoByUuid(String channelUuid) throws UnexistingChannelException {
        return prepareChannelForResponse(getChannel(channelUuid));
    }

    public void joinChannel(Channel c) throws UserAlreadyInChannelException {

        ChannelUtil.checkChannelForJoin(c, collaboratorService.currentUser().getCollaboratorId());
        c.getCollaborators().add(collaboratorService.currentUser());
        channelRepository.save(c);
    }

    public void quitChannel(Channel c) throws UserNotInChannelException {

        ChannelUtil.checkChannelForQuit(c, collaboratorService.currentUser().getCollaboratorId());
        Optional<Collaborator> collaborator = c.findCollaborator(collaboratorService.currentUser().getCollaboratorId());
        c.removeCollaborator(collaborator.get());
        channelRepository.save(c);
    }


    private ChannelDto prepareChannelForResponse(Channel c) {
        return mapper.map(c, ChannelDto.class);
    }

    private Channel getChannel(String uuid) throws UnexistingChannelException {
        Optional<Channel> c = channelRepository.findChannelByUuid(uuid);

        if (!c.isPresent()) {
            throw new UnexistingChannelException();
        }

        return c.get();
    }

}

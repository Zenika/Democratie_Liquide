package com.zenika.liquid.democracy.api.channel.service;

import com.zenika.liquid.democracy.api.channel.exception.*;
import com.zenika.liquid.democracy.dto.ChannelDto;
import com.zenika.liquid.democracy.model.Channel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ChannelService {

    public ChannelDto addChannel(Channel c) throws MalformedChannelException, UserAlreadyInChannelException, ExistingChannelException;

    public List<ChannelDto> getChannels();

    public Channel getChannelByUuid(String channelUuid) throws UnexistingChannelException;

    public ChannelDto getChannelDtoByUuid(String channelUuid) throws UnexistingChannelException;

    public void joinChannel(Channel c) throws UserAlreadyInChannelException;

    public void quitChannel(Channel c) throws UserNotInChannelException;

}

package com.zenika.liquid.democracy.api.channel.service;

import com.zenika.liquid.democracy.api.channel.exception.*;
import com.zenika.liquid.democracy.dto.ChannelDto;
import com.zenika.liquid.democracy.model.Channel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ChannelService {

    ChannelDto addChannel(Channel c) throws MalformedChannelException, UserAlreadyInChannelException, ExistingChannelException;

    List<ChannelDto> getChannels();

    Channel getChannelByUuid(String channelUuid) throws UnexistingChannelException;

    ChannelDto getChannelDtoByUuid(String channelUuid) throws UnexistingChannelException;

    void joinChannel(Channel c) throws UserAlreadyInChannelException;

    void quitChannel(Channel c) throws UserNotInChannelException;

}

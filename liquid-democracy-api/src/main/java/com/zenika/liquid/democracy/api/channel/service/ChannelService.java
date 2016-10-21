package com.zenika.liquid.democracy.api.channel.service;

import com.zenika.liquid.democracy.api.channel.exception.MalformedChannelException;
import com.zenika.liquid.democracy.api.channel.exception.UnexistingChannelException;
import com.zenika.liquid.democracy.api.channel.exception.UserAlreadyInChannelException;
import com.zenika.liquid.democracy.api.channel.exception.UserNotInChannelException;
import com.zenika.liquid.democracy.model.Channel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ChannelService {

	public Channel addChannel(Channel c) throws MalformedChannelException, UserAlreadyInChannelException;

	public List<Channel> getChannels();

	public Channel getChannelByUuid(String channelUuid) throws UnexistingChannelException;

	public void joinChannel(Channel c) throws UserAlreadyInChannelException;

	public void quitChannel(Channel c) throws UserNotInChannelException;

}

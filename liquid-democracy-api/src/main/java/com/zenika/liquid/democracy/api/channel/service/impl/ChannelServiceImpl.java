package com.zenika.liquid.democracy.api.channel.service.impl;

import com.zenika.liquid.democracy.api.channel.exception.*;
import com.zenika.liquid.democracy.api.channel.persistence.ChannelRepository;
import com.zenika.liquid.democracy.api.channel.service.ChannelService;
import com.zenika.liquid.democracy.api.channel.util.ChannelUtil;
import com.zenika.liquid.democracy.authentication.service.CollaboratorService;
import com.zenika.liquid.democracy.model.Channel;
import com.zenika.si.core.zenika.model.Collaborator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ChannelServiceImpl implements ChannelService {

	@Autowired
	private ChannelRepository channelRepository;

	@Autowired
	private CollaboratorService collaboratorService;

	public Channel addChannel(Channel newChannel) throws MalformedChannelException, UserAlreadyInChannelException, ExistingChannelException {

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
		return channelRepository.save(newChannel);
	}

	public List<Channel> getChannels() {
		return channelRepository.findAll();
	}

	public Channel getChannelByUuid(String channelUuid) throws UnexistingChannelException {
		Optional<Channel> c = channelRepository.findChannelByUuid(channelUuid);

		if (!c.isPresent()) {
			throw new UnexistingChannelException();
		}

		return c.get();
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

}

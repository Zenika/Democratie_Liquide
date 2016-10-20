package com.zenika.liquid.democracy.api.channel.controller;

import com.zenika.liquid.democracy.api.channel.exception.MalformedChannelException;
import com.zenika.liquid.democracy.api.channel.exception.UnexistingChannelException;
import com.zenika.liquid.democracy.api.channel.exception.UserAlreadyInChannelException;
import com.zenika.liquid.democracy.api.channel.exception.UserNotInChannelException;
import com.zenika.liquid.democracy.api.channel.service.ChannelService;
import com.zenika.liquid.democracy.model.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api/channels")
public class ChannelController {

	@Autowired
	private ChannelService channelService;

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Void> addChannel(@Validated @RequestBody Channel c) throws MalformedChannelException {

		Channel out = channelService.addChannel(c);

		return ResponseEntity.created(
		        ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(out.getUuid()).toUri())
		        .build();
	}

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<Channel>> getChannels() {

		List<Channel> out = channelService.getChannels();

		if (out.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(out);
		}

		return ResponseEntity.ok(out);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{channelUuid}/join")
	public ResponseEntity<Channel> joinChannel(@PathVariable String channelUuid)
			throws UnexistingChannelException, UserAlreadyInChannelException {

		Channel c = channelService.getChannelByUuid(channelUuid);
		channelService.joinChannel(c);

		return ResponseEntity.ok().body(c);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{channelUuid}/quit")
	public ResponseEntity<Channel> quitChannel(@PathVariable String channelUuid)
			throws UnexistingChannelException, UserNotInChannelException {

		Channel c = channelService.getChannelByUuid(channelUuid);
		channelService.quitChannel(c);

		return ResponseEntity.ok().body(c);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{channelUuid}")
	public ResponseEntity<Channel> getChannelByUuid(@PathVariable String channelUuid)
	        throws UnexistingChannelException {

		Channel c = channelService.getChannelByUuid(channelUuid);

		return ResponseEntity.ok().body(c);
	}

	@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Le titre est obligatoire")
	@ExceptionHandler(MalformedChannelException.class)
	public void malformedChannelHandler() {
	}

	@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Le channel n'existe pas")
	@ExceptionHandler(UnexistingChannelException.class)
	public void unexistingChannelHandler() {
	}

}

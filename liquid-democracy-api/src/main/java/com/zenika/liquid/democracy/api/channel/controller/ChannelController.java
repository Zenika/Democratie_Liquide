package com.zenika.liquid.democracy.api.channel.controller;

import com.zenika.liquid.democracy.api.channel.exception.*;
import com.zenika.liquid.democracy.api.channel.service.ChannelService;
import com.zenika.liquid.democracy.dto.ChannelDto;
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

    private final ChannelService channelService;

    @Autowired
    public ChannelController(ChannelService channelService) {
        this.channelService = channelService;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> addChannel(@Validated @RequestBody Channel c) {
        ChannelDto out = channelService.addChannel(c);
        return ResponseEntity.created(
                ServletUriComponentsBuilder.fromCurrentRequest()
                        .path("/{id}")
                        .buildAndExpand(out.getUuid())
                        .toUri()
        ).build();
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<ChannelDto>> getChannels() {
        List<ChannelDto> out = channelService.getChannels();
        if (out.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(out);
        }
        return ResponseEntity.ok(out);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{channelUuid}/join")
    public ResponseEntity<Void> joinChannel(@PathVariable String channelUuid) {
        Channel c = channelService.getChannelByUuid(channelUuid);
        channelService.joinChannel(c);

        return ResponseEntity.ok().build();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{channelUuid}/quit")
    public ResponseEntity<Void> quitChannel(@PathVariable String channelUuid) {
        Channel c = channelService.getChannelByUuid(channelUuid);
        channelService.quitChannel(c);

        return ResponseEntity.ok().build();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{channelUuid}")
    public ResponseEntity<ChannelDto> getChannelByUuid(@PathVariable String channelUuid) {
        ChannelDto c = channelService.getChannelDtoByUuid(channelUuid);

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

    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Un channel du même nom existe déjà")
    @ExceptionHandler(ExistingChannelException.class)
    public void existingChannelHandler() {
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Vous appartenez déjà à ce channel")
    @ExceptionHandler(UserAlreadyInChannelException.class)
    public void userAlreadyInChannelHandler() {
    }


    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Aucune trace de vous n'a été trouvée dans ce channel")
    @ExceptionHandler(UserNotInChannelException.class)
    public void userNotInChannelHandler() {
    }
}

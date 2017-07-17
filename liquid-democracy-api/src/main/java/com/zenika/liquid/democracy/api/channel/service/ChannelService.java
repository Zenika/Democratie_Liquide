package com.zenika.liquid.democracy.api.channel.service;

import com.zenika.liquid.democracy.dto.ChannelDto;
import com.zenika.liquid.democracy.model.Channel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ChannelService {

    ChannelDto addChannel(Channel c);

    List<ChannelDto> getChannels();

    Channel getChannelByUuid(String channelUuid);

    ChannelDto getChannelDtoByUuid(String channelUuid);

    void joinChannel(Channel c);

    void quitChannel(Channel c);

}

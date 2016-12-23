package com.zenika.liquid.democracy.api.channel.persistence;

import com.zenika.liquid.democracy.model.Channel;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ChannelRepository extends MongoRepository<Channel, Long> {

	Optional<Channel> findChannelByUuid(String channelUuid);

	Optional<Channel> findChannelByTitle(String title);
}

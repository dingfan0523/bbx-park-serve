package com.cgnpc.bbxpark.device.mapper;

import com.cgnpc.bbxpark.device.domain.ThingModelMessage;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ThingModelMessageRepository  extends ElasticsearchRepository<ThingModelMessage, String> {
}

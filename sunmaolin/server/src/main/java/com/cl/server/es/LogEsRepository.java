package com.cl.server.es;

import com.cl.server.entity.LogEs;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Component;
/**
 * Es api仓库
 *
 * @author: tressures
 * @date: 2024/6/13
 */
@Component
public interface LogEsRepository extends ElasticsearchRepository<LogEs,Long> {
}

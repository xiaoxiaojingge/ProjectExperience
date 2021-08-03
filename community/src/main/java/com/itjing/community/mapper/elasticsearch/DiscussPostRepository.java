package com.itjing.community.mapper.elasticsearch;

import com.itjing.community.entity.DiscussPost;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @author: lijing
 * @Date: 2021年08月02日 12:46
 * @Description:
 */
@Repository
public interface DiscussPostRepository extends ElasticsearchRepository<DiscussPost, Integer> {

}

package com.itjing.community.service;

import com.itjing.community.entity.DiscussPost;
import com.itjing.community.mapper.elasticsearch.DiscussPostRepository;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author: lijing
 * @Date: 2021年08月02日 15:30
 * @Description: 使用es对帖子进行操作
 */
@Service
public class ElasticsearchService {

    @Autowired
    private DiscussPostRepository discussRepository;

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    public void saveDiscussPost(DiscussPost post) {
        discussRepository.save(post);
    }

    public void deleteDiscussPost(int id) {
        discussRepository.deleteById(id);
    }

    public Page<DiscussPost> searchDiscussPost(String keyword, int current, int limit) {

        NativeSearchQuery query = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.multiMatchQuery(keyword, "title", "content"))
                .withSort(SortBuilders.fieldSort("type").order(SortOrder.DESC))
                .withSort(SortBuilders.fieldSort("score").order(SortOrder.DESC))
                .withSort(SortBuilders.fieldSort("createTime").order(SortOrder.DESC))
                .withPageable(PageRequest.of(current, limit))
                .withHighlightFields(
                        new HighlightBuilder.Field("title").preTags("<em>").postTags("</em>"),
                        new HighlightBuilder.Field("content").preTags("<em>").postTags("</em>")
                ).build();

        org.springframework.data.elasticsearch.core.SearchHits<DiscussPost> searchHits = elasticsearchRestTemplate.search(query, DiscussPost.class);

        // 高亮字段映射
        List<DiscussPost> list = new ArrayList<>();
        for (SearchHit<DiscussPost> searchHit : searchHits) {

            DiscussPost discussPostVo = new DiscussPost();

            BeanUtils.copyProperties(searchHit.getContent(), discussPostVo);

            // 处理高亮显示的结果
            Map<String, List<String>> highlightFields = searchHit.getHighlightFields();
            for (String highlightField : highlightFields.keySet()) {

                if (StringUtils.equals(highlightField, "title")) {
                    discussPostVo.setTitle(highlightFields.get(highlightField).get(0));
                } else if (StringUtils.equals(highlightField, "content")) {
                    discussPostVo.setContent(highlightFields.get(highlightField).get(0));
                }

            }
            list.add(discussPostVo);
        }

        // 组装分页对象
        Page<DiscussPost> page = new PageImpl<>(list, PageRequest.of(current, limit), searchHits.getTotalHits());

        return page;
    }

}
package com.itjing.community;

import com.itjing.community.entity.DiscussPost;
import com.itjing.community.mapper.DiscussPostMapper;
import com.itjing.community.mapper.elasticsearch.DiscussPostRepository;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author: lijing
 * @Date: 2021年08月02日 12:51
 * @Description: 测试es相关api
 */
@SpringBootTest
public class ElasticsearchTests {

    @Autowired
    private DiscussPostMapper discussMapper;

    @Autowired
    private DiscussPostRepository discussRepository;

    @Autowired
    ElasticsearchRestTemplate elasticsearchRestTemplate;

    /**
     * 测试插入一条
     */
    @Test
    public void testInsert() {
        discussRepository.save(discussMapper.selectDiscussPostById(241));
        discussRepository.save(discussMapper.selectDiscussPostById(242));
        discussRepository.save(discussMapper.selectDiscussPostById(243));
    }

    /**
     * 测试插入多个，即插入集合
     */
    @Test
    public void testInsertList() {
        discussRepository.saveAll(discussMapper.selectDiscussPosts(101, 0, 100,0));
        discussRepository.saveAll(discussMapper.selectDiscussPosts(102, 0, 100,0));
        discussRepository.saveAll(discussMapper.selectDiscussPosts(103, 0, 100,0));
        discussRepository.saveAll(discussMapper.selectDiscussPosts(111, 0, 100,0));
        discussRepository.saveAll(discussMapper.selectDiscussPosts(112, 0, 100,0));
        discussRepository.saveAll(discussMapper.selectDiscussPosts(131, 0, 100,0));
        discussRepository.saveAll(discussMapper.selectDiscussPosts(132, 0, 100,0));
        discussRepository.saveAll(discussMapper.selectDiscussPosts(133, 0, 100,0));
        discussRepository.saveAll(discussMapper.selectDiscussPosts(134, 0, 100,0));
    }

    /**
     * 测试修改
     */
    @Test
    public void testUpdate() {
        DiscussPost post = discussMapper.selectDiscussPostById(231);
        post.setContent("我是新人,使劲灌水.");
        discussRepository.save(post);
    }

    /**
     * 测试删除
     */
    @Test
    public void testDelete() {
        // discussRepository.deleteById(231); // 删除一条
        discussRepository.deleteAll(); // 全部删除，谨慎使用
    }


    /**
     * 通过 Repository 测试搜索
     */
    @Test
    public void testSearchByRepository() {

        // 构建查询条件
        MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery("互联网寒冬", "title", "content");

        // 构建排序条件
        FieldSortBuilder typeSortBuilder = SortBuilders.fieldSort("type").order(SortOrder.DESC);
        FieldSortBuilder scoreSortBuilder = SortBuilders.fieldSort("score").order(SortOrder.DESC);
        FieldSortBuilder createTimeSortBuilder = SortBuilders.fieldSort("createTime").order(SortOrder.DESC);

        // 构建分页条件
        PageRequest pageRequest = PageRequest.of(0, 10);

        // 构建高亮条件
        HighlightBuilder.Field titleHigh = new HighlightBuilder.Field("title").preTags("<em>").postTags("</em>");
        HighlightBuilder.Field contentHigh = new HighlightBuilder.Field("content").preTags("<em>").postTags("</em>");

        // 组装搜索条件
        NativeSearchQuery query = new NativeSearchQueryBuilder()
                .withQuery(multiMatchQueryBuilder)
                .withSort(typeSortBuilder) // 按照类型降序
                .withSort(scoreSortBuilder) // 类型相同，则按照分数降序
                .withSort(createTimeSortBuilder) // 分数相同则按照创建时间降序
                .withPageable(pageRequest)
                .withHighlightFields(
                        titleHigh, contentHigh
                ).build();

        Page<DiscussPost> page = discussRepository.search(query);

        System.out.println(page.getTotalElements());
        System.out.println(page.getTotalPages());
        System.out.println(page.getNumber());
        System.out.println(page.getSize());
        for (DiscussPost post : page) {
            System.out.println(post);
        }
    }

    /**
     * 通过 RestTemplate 测试搜索
     */
    @Test
    public void testSearchByTemplate() {

        // 构建查询条件
        MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery("互联网寒冬", "title", "content");

        // 构建排序条件
        FieldSortBuilder typeSortBuilder = SortBuilders.fieldSort("type").order(SortOrder.DESC);
        FieldSortBuilder scoreSortBuilder = SortBuilders.fieldSort("score").order(SortOrder.DESC);
        FieldSortBuilder createTimeSortBuilder = SortBuilders.fieldSort("createTime").order(SortOrder.DESC);

        // 构建分页条件
        PageRequest pageRequest = PageRequest.of(0, 10);

        // 构建高亮条件
        HighlightBuilder.Field titleHigh = new HighlightBuilder.Field("title").preTags("<em>").postTags("</em>");
        HighlightBuilder.Field contentHigh = new HighlightBuilder.Field("content").preTags("<em>").postTags("</em>");

        // 组装搜索条件
        NativeSearchQuery query = new NativeSearchQueryBuilder()
                .withQuery(multiMatchQueryBuilder)
                .withSort(typeSortBuilder) // 按照类型降序
                .withSort(scoreSortBuilder) // 类型相同，则按照分数降序
                .withSort(createTimeSortBuilder) // 分数相同则按照创建时间降序
                .withPageable(pageRequest)
                .withHighlightFields(
                        titleHigh, contentHigh
                ).build();

        SearchHits<DiscussPost> searchHits = elasticsearchRestTemplate.search(query, DiscussPost.class);

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
        Page<DiscussPost> page = new PageImpl<>(list, pageRequest, searchHits.getTotalHits());


        System.out.println(page.getTotalElements());
        System.out.println(page.getTotalPages());
        System.out.println(page.getNumber());
        System.out.println(page.getSize());

        for (DiscussPost post : page) {
            System.out.println(post);
        }
    }

}

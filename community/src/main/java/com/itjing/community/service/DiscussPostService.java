package com.itjing.community.service;

import com.itjing.community.entity.DiscussPost;

import java.util.List;

/**
 * @author: lijing
 * @Date: 2021年07月30日 13:54
 * @Description:
 */
public interface DiscussPostService {

    /**
     * 查帖子
     *
     * @param userId
     * @param offset
     * @param limit
     * @return
     */
    List<DiscussPost> findDiscussPosts(int userId, int offset, int limit, int orderMode);

    /**
     * 查帖子数
     *
     * @param userId
     * @return
     */
    int findDiscussPostRows(int userId);

    /**
     * 新增帖子
     *
     * @param post
     * @return
     */
    int addDiscussPost(DiscussPost post);

    /**
     * 查看帖子
     *
     * @param id
     * @return
     */
    DiscussPost findDiscussPostById(int id);

    /**
     * 修改评论数
     *
     * @param id
     * @param commentCount
     * @return
     */
    int updateCommentCount(int id, int commentCount);

    /**
     * 修改帖子类型，0-普通; 1-置顶;
     *
     * @param id
     * @param type
     * @return
     */
    int updateType(int id, int type);

    /**
     * 修改帖子状态，0-正常; 1-精华; 2-拉黑;
     *
     * @param id
     * @param status
     * @return
     */
    int updateStatus(int id, int status);

    /**
     * 修改得分
     *
     * @param id
     * @param score
     * @return
     */
    int updateScore(int id, double score);
}

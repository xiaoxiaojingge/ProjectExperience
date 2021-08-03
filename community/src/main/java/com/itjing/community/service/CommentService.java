package com.itjing.community.service;

import com.itjing.community.entity.Comment;

import java.util.List;

/**
 * @author: lijing
 * @Date: 2021年07月31日 18:48
 * @Description:
 */
public interface CommentService {

    /**
     * 查看评论列表
     *
     * @param entityType
     * @param entityId
     * @param offset
     * @param limit
     * @return
     */
    List<Comment> findCommentsByEntity(int entityType, int entityId, int offset, int limit);

    /**
     * 查看评论数量
     *
     * @param entityType
     * @param entityId
     * @return
     */
    int findCommentCount(int entityType, int entityId);

    /**
     * 新增评论
     *
     * @param comment
     * @return
     */
    int addComment(Comment comment);


    /**
     * 查询评论
     *
     * @param id
     * @return
     */
    Comment findCommentById(int id);

    /**
     * 查询用户回帖数
     * @param userId
     * @param entityTypePost
     * @return
     */
    int findCommentRows(int userId, int entityTypePost);

    /**
     * 查询回复的帖子
     * @param userId
     * @param offset
     * @param limit
     * @return
     */
    List<Comment> findComments(int userId, int offset, int limit,int entityType);
}

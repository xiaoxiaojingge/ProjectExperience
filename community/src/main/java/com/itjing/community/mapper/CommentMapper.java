package com.itjing.community.mapper;

import com.itjing.community.entity.Comment;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author: lijing
 * @Date: 2021年07月31日 18:46
 * @Description:
 */
@Repository
public interface CommentMapper {

    /**
     * 查看评论列表
     *
     * @param entityType
     * @param entityId
     * @param offset
     * @param limit
     * @return
     */
    List<Comment> selectCommentsByEntity(int entityType, int entityId, int offset, int limit);

    /**
     * 查看评论数量
     *
     * @param entityType
     * @param entityId
     * @return
     */
    int selectCountByEntity(int entityType, int entityId);

    /**
     * 新增评论
     *
     * @param comment
     * @return
     */
    int insertComment(Comment comment);

    /**
     * 根据id查询评论
     *
     * @param id
     * @return
     */
    Comment selectCommentById(int id);

    /**
     * 根据用户id和实体类型查询
     *
     * @param userId
     * @param entityType
     * @return
     */
    int selectCommentRows(int userId, int entityType);

    /**
     * 分页查询用户评论
     *
     * @param userId
     * @param offset
     * @param limit
     * @return
     */
    List<Comment> selectComments(int userId, int offset, int limit, int entityType);
}

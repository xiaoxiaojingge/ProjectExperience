package com.itjing.community.service.impl;

import com.itjing.community.constant.CommunityConstant;
import com.itjing.community.entity.Comment;
import com.itjing.community.mapper.CommentMapper;
import com.itjing.community.mapper.DiscussPostMapper;
import com.itjing.community.service.CommentService;
import com.itjing.community.utils.SensitiveFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * @author: lijing
 * @Date: 2021年07月31日 18:50
 * @Description:
 */
@Service
public class CommentServiceImpl implements CommentService, CommunityConstant {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private SensitiveFilter sensitiveFilter;

    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Override
    public List<Comment> findCommentsByEntity(int entityType, int entityId, int offset, int limit) {
        return commentMapper.selectCommentsByEntity(entityType, entityId, offset, limit);
    }

    @Override
    public int findCommentCount(int entityType, int entityId) {
        return commentMapper.selectCountByEntity(entityType, entityId);
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public int addComment(Comment comment) {

        if (comment == null) {
            throw new IllegalArgumentException("参数不能为空!");
        }

        // 添加评论
        comment.setContent(HtmlUtils.htmlEscape(comment.getContent()));
        comment.setContent(sensitiveFilter.filter(comment.getContent()));

        int rows = commentMapper.insertComment(comment);

        // 更新帖子评论数量
        if (comment.getEntityType() == ENTITY_TYPE_POST) {
            int count = commentMapper.selectCountByEntity(comment.getEntityType(), comment.getEntityId());
            discussPostMapper.updateCommentCount(comment.getEntityId(), count);
        }

        return rows;
    }


    @Override
    public Comment findCommentById(int id) {
        return commentMapper.selectCommentById(id);
    }

    @Override
    public int findCommentRows(int userId, int entityType) {
        return commentMapper.selectCommentRows(userId, entityType);
    }

    @Override
    public List<Comment> findComments(int userId, int offset, int limit,int entityType) {
        return commentMapper.selectComments(userId, offset, limit,entityType);
    }
}

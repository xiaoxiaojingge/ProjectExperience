package com.itjing.community.mapper;

import com.itjing.community.entity.DiscussPost;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author: lijing
 * @Date: 2021年07月30日 12:28
 * @Description:
 */
@Repository
public interface DiscussPostMapper {
    /**
     * 查询所有
     * @param userId
     * @param offset 哪一页
     * @param limit 每页多少条
     * @param orderMode 排序模式
     * @return
     */
    List<DiscussPost> selectDiscussPosts(int userId, int offset, int limit,int orderMode);

    /**
     * @Param注解用于给参数取别名,
     * 如果只有一个参数,并且在<if>里使用,则必须加别名.
     * @param userId
     * @return 帖子的数量
     */
    int selectDiscussPostRows(@Param("userId") int userId);

    /**
     * 新增帖子
     * @param discussPost
     * @return
     */
    int insertDiscussPost(DiscussPost discussPost);

    /**
     * 查询帖子
     * @param id
     * @return
     */
    DiscussPost selectDiscussPostById(int id);

    /**
     * 修改评论数量
     * @param id
     * @param commentCount
     * @return
     */
    int updateCommentCount(int id, int commentCount);

    /**
     * 修改类型
     * @param id
     * @param type
     * @return
     */
    int updateType(int id, int type);

    /**
     * 修改状态
     * @param id
     * @param status
     * @return
     */
    int updateStatus(int id, int status);

    /**
     * 修改分数
     * @param id
     * @param score
     * @return
     */
    int updateScore(int id, double score);
}

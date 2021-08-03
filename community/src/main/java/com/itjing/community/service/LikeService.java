package com.itjing.community.service;

/**
 * @author: lijing
 * @Date: 2021年08月01日 15:22
 * @Description: 点赞业务接口
 */
public interface LikeService {

    /**
     * 点赞
     * @param userId
     * @param entityType
     * @param entityId
     * @param entityUserId
     */
    void like(int userId, int entityType, int entityId, int entityUserId);

    /**
     * 查询某实体点赞的数量
     * @param entityType
     * @param entityId
     * @return
     */
    long findEntityLikeCount(int entityType, int entityId);

    /**
     *  查询某人对某实体的点赞状态
     * @param userId
     * @param entityType
     * @param entityId
     * @return
     */
    int findEntityLikeStatus(int userId, int entityType, int entityId);

    /**
     * 查询某个用户获得的赞
     * @param userId
     * @return
     */
    int findUserLikeCount(int userId);
}

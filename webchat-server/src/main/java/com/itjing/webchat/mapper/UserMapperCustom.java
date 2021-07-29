package com.itjing.webchat.mapper;

import com.itjing.webchat.vo.FriendsRequestVO;
import com.itjing.webchat.vo.MyFriendsVO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author: lijing
 * @Date: 2021年07月27日 18:19
 * @Description:
 */
@Repository
public interface UserMapperCustom {

    List<FriendsRequestVO> queryFriendRequestList(String acceptUserId);

    List<MyFriendsVO> queryMyFriends(String userId);

    void batchUpdateMsgSigned(List<String> msgIdList);

}

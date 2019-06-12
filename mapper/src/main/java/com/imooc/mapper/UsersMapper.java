package com.imooc.mapper;

import com.imooc.pojo.Users;
import com.imooc.utils.MyMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersMapper extends MyMapper<Users> {
    /**
     * 用户受喜欢数累加
     * @param userId
     */
    void addReceiveLikeCounts(@Param("userId") String userId);

    /**
     * 用户受喜欢数累减
     * @param userId
     */
    void reduceReceiveLikeCounts(@Param("userId") String userId);

    /**
     * 增加粉丝数量
     * @param userId
     */
    void addFansCount(@Param("userId") String userId);

    /**
     * 减少粉丝数量
     * @param userId
     */
    void reduceFansCount(@Param("userId") String userId);

    /**
     * 增加关注数量
     * @param userId
     */
    void addFllowCount(@Param("userId") String userId);

    /**
     * 减少关注数量
     * @param userId
     */
    void reduceFllowCount(@Param("userId") String userId);

}
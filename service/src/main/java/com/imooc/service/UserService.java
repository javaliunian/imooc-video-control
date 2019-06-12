package com.imooc.service;

import com.imooc.pojo.Users;
import com.imooc.pojo.UsersReport;

/**
 * @author daile.sun
 * @date 2018/9/26
 */
public interface UserService {
    /**
     *
     * @param userName 判断该注册用户是否已存在
     * @return   false:不存在   true：以存在
     */
    boolean isExistUser(String  userName);

    /**
     * 保存用户
     * @param user
     * @return
     */
    void saveUsers(Users user);

    /**
     *
     * @param userName 判断该用户是否可以登录
     * @return   false:不可以   true：可以
     */
    Users isExistUserForLogin(String  userName,String password);

    /**
     * 用户修改信息
     * @param user
     */
    void updateUserInfo(Users user);

    /**
     * 查询用户信息
     * @param userId
     * @return
     */
    Users queryUserInfo(String userId);

    /**
     * 查询用户是否喜欢该视频
     * @param userId
     * @param videoId
     * @return
     */
    boolean isUserLikeVideo(String userId,String videoId);

    /**
     * 增加用户和粉丝的关系
     * @param userId
     * @param fanId
     */
    void saveUserFanRelation(String userId,String fanId);

    /**
     * 删除用户和粉丝的关系
     * @param userId
     * @param fanId
     */
    void deleteUserFanRelation(String userId,String fanId);


    /**
     * 查询用户是否关注
     * @param userId
     * @param fanId
     * @return
     */
    boolean queryIfFollow(String userId,String fanId);

    /**
     * 保存举报用户的信息
     * @param usersReport
     */
    void saveReportUser(UsersReport usersReport,String userId);
}

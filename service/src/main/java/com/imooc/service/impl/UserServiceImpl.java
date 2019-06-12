package com.imooc.service.impl;

import com.imooc.mapper.UsersFansMapper;
import com.imooc.mapper.UsersLikeVideosMapper;
import com.imooc.mapper.UsersMapper;
import com.imooc.mapper.UsersReportMapper;
import com.imooc.pojo.Users;
import com.imooc.pojo.UsersFans;
import com.imooc.pojo.UsersLikeVideos;
import com.imooc.pojo.UsersReport;
import com.imooc.service.UserService;
import com.imooc.utils.IMoocJSONResult;
import org.apache.commons.lang3.StringUtils;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @author daile.sun
 * @date 2018/9/26
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private UsersLikeVideosMapper usersLikeVideosMapper;

    @Autowired
    private Sid sid;

    @Autowired
    private UsersFansMapper usersFansMapper;

    @Autowired
    private UsersReportMapper usersReportMapper;

    @Transactional(propagation = Propagation.SUPPORTS,rollbackFor = Exception.class)
    @Override
    public boolean isExistUser(String userName) {
        Users user = new Users();
        user.setUsername(userName);
        Users result = usersMapper.selectOne(user);

        return result == null ? false : true;
    }

    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    @Override
    public void saveUsers(Users user) {
        String userId=sid.nextShort();
        user.setId(userId);
        usersMapper.insert(user);
    }

    @Transactional(propagation = Propagation.SUPPORTS,rollbackFor = Exception.class)
    @Override
    public Users isExistUserForLogin(String userName,String password) {
        Users user = new Users();
        user.setUsername(userName);
        user.setPassword(password);
        Users result = usersMapper.selectOne(user);
        if(result !=null){
            return result;
        }else{
            return null;
        }
    }

    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    @Override
    public void updateUserInfo(Users user) {
        usersMapper.updateByPrimaryKeySelective(user);
    }

    @Transactional(propagation = Propagation.SUPPORTS,rollbackFor = Exception.class)
    @Override
    public Users queryUserInfo(String userId) {
        Users user=new Users();
        user.setId(userId);
        return usersMapper.selectByPrimaryKey(user);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS,rollbackFor = Exception.class)
    public boolean isUserLikeVideo(String userId, String videoId) {
        if(StringUtils.isBlank(videoId) || StringUtils.isBlank(userId)){
            return false;
        }
        UsersLikeVideos usersLikeVideos = new UsersLikeVideos();
        usersLikeVideos.setUserId(userId);
        usersLikeVideos.setVideoId(videoId);
        if(!usersLikeVideosMapper.select(usersLikeVideos).isEmpty()){
            return true;
        }
        return false;

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void saveUserFanRelation(String userId, String fanId) {
        UsersFans usersFans = new UsersFans();
        usersFans.setId(sid.nextShort());
        usersFans.setFanId(fanId);
        usersFans.setUserId(userId);
        usersFansMapper.insert(usersFans);

        usersMapper.addFansCount(userId);
        usersMapper.addFllowCount(fanId);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void deleteUserFanRelation(String userId, String fanId) {
        UsersFans usersFans = new UsersFans();
        usersFans.setFanId(fanId);
        usersFans.setUserId(userId);
        usersFansMapper.delete(usersFans);

        usersMapper.reduceFansCount(userId);
        usersMapper.reduceFllowCount(fanId);

    }

    @Override
    public boolean queryIfFollow(String userId, String fanId) {
        UsersFans usersFans = new UsersFans();
        usersFans.setFanId(fanId);
        usersFans.setUserId(userId);
        if(usersFansMapper.selectOne(usersFans)!=null){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public void saveReportUser(UsersReport usersReport,String userId) {
        usersReport.setId(sid.nextShort());
        usersReport.setUserid(userId);
        usersReport.setCreateDate(new Date());
        usersReportMapper.insert(usersReport);
    }
}

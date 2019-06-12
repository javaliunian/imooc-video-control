package com.imooc.controller;

import com.imooc.pojo.Users;
import com.imooc.service.UserService;
import com.imooc.utils.IMoocJSONResult;
import com.imooc.utils.MD5Utils;
import com.imooc.vo.UsersVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.util.StringUtil;

import java.util.UUID;

/**
 * @author daile.sun
 * @date 2018/9/26
 */
@RestController
@Api(value="用户登录注册的接口",tags = {"注册和登录的controller"})
public class RegistLoginController extends BasicController{

    @Autowired
    private UserService userService;

    @PostMapping(value = "/regist")
    @ApiOperation(value="用户注册",notes = "用户注册的接口")
    public IMoocJSONResult regist(@RequestBody Users user) throws Exception {
        //1.判断用户名和密码是否为空
        if(StringUtil.isEmpty(user.getUsername())||StringUtil.isEmpty(user.getPassword())){
            return IMoocJSONResult.errorMsg("用户名和密码不能为空！");
        }

        //2.判断用户名是否已存在
        if(userService.isExistUser(user.getUsername())){
            return IMoocJSONResult.errorMsg("用户名已存在");
        }else{
            //3.插入数据
            user.setNickname(user.getUsername());
            user.setPassword(MD5Utils.getMD5Str(user.getPassword()));
            user.setFansCounts(0);
            user.setFollowCounts(0);
            user.setReceiveLikeCounts(0);
            userService.saveUsers(user);

        }
        user.setPassword("");

        UsersVO usersVO = serUserRedisSessionToken(user);

        return  IMoocJSONResult.ok(usersVO);
    }

    private UsersVO serUserRedisSessionToken(Users user) {
        String uniqueToken= UUID.randomUUID().toString();
        //redis中缓存用户的token30分钟过期,经检验，单位是秒
        redis.set(USER_REDIS_SESSION+":"+user.getId(),uniqueToken,60*30);

        UsersVO usersVO = new UsersVO();
        BeanUtils.copyProperties(user,usersVO);
        usersVO.setUserToken(uniqueToken);
        return usersVO;
    }

    @RequestMapping(value = "/login",method = RequestMethod.POST)
    @ApiOperation(value="用户登录",notes = "用户登录的接口")
    public IMoocJSONResult login(@RequestBody Users user) throws Exception {
        //1.判断用户名和密码是否为空
        if(StringUtil.isEmpty(user.getUsername())||StringUtil.isEmpty(user.getPassword())){
            return IMoocJSONResult.errorMsg("用户名和密码不能为空！");
        }
        //2.对密码进行加密
        user.setPassword(MD5Utils.getMD5Str(user.getPassword()));

        //3.判断该用户是否可以登录
        Users userResult=userService.isExistUserForLogin(user.getUsername(),user.getPassword());
        if(userResult!=null){
            userResult.setPassword("");
            UsersVO usersVO=serUserRedisSessionToken(userResult);
            return IMoocJSONResult.ok(usersVO);
        }else{
            return  IMoocJSONResult.errorMsg("用户名或密码错误");
        }

    }

    @RequestMapping(value = "/logout",method = RequestMethod.POST)
    @ApiImplicitParam(name = "userId",value = "用户id",required = true,dataType = "String",paramType = "query")
    @ApiOperation(value="用户注销",notes = "用户注销的接口")
    public IMoocJSONResult logout(String userId) throws Exception {
        redis.del(USER_REDIS_SESSION+":"+userId);
        return IMoocJSONResult.ok();

    }
}

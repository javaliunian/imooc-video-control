package com.imooc.controller;

import com.imooc.pojo.Users;
import com.imooc.pojo.UsersReport;
import com.imooc.service.UserService;
import com.imooc.utils.IMoocJSONResult;
import com.imooc.vo.PublisherVideo;
import com.imooc.vo.UsersVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.util.StringUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author daile.sun
 * @date 2018/9/26
 */
@RestController
@Api(value="用户相关业务的接口",tags = {"用户相关业务controller"})
@RequestMapping("/user")
public class UserController extends BasicController{

    @Autowired
    private UserService userService;


    @RequestMapping(value = "/uploadFace",method = RequestMethod.POST)
    @ApiImplicitParam(name = "userId",value = "用户id",required = true,dataType = "String",paramType = "query")
    @ApiOperation(value="用户上传头像",notes = "用户上传头像的接口")
    public IMoocJSONResult uploadFace(String userId, @RequestParam("file") MultipartFile[] files) throws Exception {

        if(StringUtils.isBlank(userId)){
            return IMoocJSONResult.errorMsg("用户id不能为空...");
        }

        FileOutputStream fileOutputStream=null;
        InputStream inputStream=null;
        //文件保存的命名空间
        String fileSpace="C:/imooc_video_dev";
        //保存到数据库中的相对路径
        String uploadPathDB="/"+userId+"/face";

        try {

            if(files!=null&&files.length>0){


                String fileName = files[0].getOriginalFilename();
                if(StringUtil.isNotEmpty(fileName)){
                    //文件上传后最终的保存路径
                    String finalPath=fileSpace+uploadPathDB+"/"+fileName;
                    //设置数据库的保存路径
                    uploadPathDB+=("/"+fileName);
                    File outFile=new File(finalPath);
                    if(outFile.getParentFile()!=null || !outFile.getParentFile().isDirectory()){
                        //创建文件夹
                        outFile.getParentFile().mkdirs();
                    }

                    fileOutputStream=new FileOutputStream(outFile);
                    inputStream=files[0].getInputStream();
                    IOUtils.copy(inputStream,fileOutputStream);
                }
            }else{
                return IMoocJSONResult.errorMsg("图片不能为空...");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return IMoocJSONResult.errorMsg("上传出错...");
        } finally {
            if(fileOutputStream!=null){
                fileOutputStream.flush();
                fileOutputStream.close();
            }
            if(inputStream!=null){
                inputStream.close();
            }
        }

        Users users = new Users();
        users.setId(userId);
        users.setFaceImage(uploadPathDB);
        userService.updateUserInfo(users);

        return IMoocJSONResult.ok(uploadPathDB);

    }

    @RequestMapping(value = "/query",method = RequestMethod.POST)
    @ApiImplicitParam(name = "userId",value = "用户id",required = true,dataType = "String",paramType = "query")
    @ApiOperation(value="查询用户信息",notes = "查询用户信息的接口")
    public IMoocJSONResult query(String userId,String fanId) throws Exception {
        if(StringUtils.isBlank(userId)){
            return IMoocJSONResult.errorMsg("用户id不能为空...");
        }
        Users users = userService.queryUserInfo(userId);
        UsersVO usersVO=new UsersVO();
        BeanUtils.copyProperties(users,usersVO);
        usersVO.setFollow(userService.queryIfFollow(userId,fanId));
        return IMoocJSONResult.ok(usersVO);

    }


    @RequestMapping(value = "/queryPublisher",method = RequestMethod.POST)
    @ApiImplicitParam(name = "userId",value = "用户id",required = true,dataType = "String",paramType = "query")
    @ApiOperation(value="查询发布者信息",notes = "查询发布者信息的接口")
    public IMoocJSONResult queryPublisher(String loginUserId,String videoId,String publishUserId) throws Exception {
        if(StringUtils.isBlank(videoId) ){
            return IMoocJSONResult.errorMsg("");
        }
        // 1.查询视频发布者信息
        Users users = userService.queryUserInfo(publishUserId);
        UsersVO publisher=new UsersVO();
        BeanUtils.copyProperties(users,publisher);

        // 2.查询当前登录者是否对该视频点赞

        boolean userLikeVideo=userService.isUserLikeVideo(loginUserId,videoId);

        PublisherVideo publisherVideo = new PublisherVideo();
        publisherVideo.setUserLikeVideo(userLikeVideo);
        publisherVideo.setPublisher(publisher);

        return IMoocJSONResult.ok(publisherVideo);

    }

    @RequestMapping(value = "/concern",method = RequestMethod.POST)
    @ApiImplicitParam(name = "userId",value = "用户id",required = true,dataType = "String",paramType = "query")
    @ApiOperation(value="关注",notes = "关注的接口")
    public IMoocJSONResult concern(String userId,String fansId) throws Exception {

        if(StringUtils.isBlank(userId)||StringUtils.isBlank(fansId)){
            return IMoocJSONResult.errorMsg("");
        }

        userService.saveUserFanRelation(userId,fansId);
        return IMoocJSONResult.ok("关注成功~");

    }

    @RequestMapping(value = "/cancelConcern",method = RequestMethod.POST)
    @ApiImplicitParam(name = "userId",value = "用户id",required = true,dataType = "String",paramType = "query")
    @ApiOperation(value="取消关注",notes = "取消关注的接口")
    public IMoocJSONResult cancelConcern(String userId,String fansId) throws Exception {

        if(StringUtils.isBlank(userId)||StringUtils.isBlank(fansId)){
            return IMoocJSONResult.errorMsg("");
        }

        userService.deleteUserFanRelation(userId,fansId);
        return IMoocJSONResult.ok("取消关注成功~");

    }

    @RequestMapping(value = "/reportUser",method = RequestMethod.POST)
    @ApiImplicitParam(name = "userId",value = "用户id",required = true,dataType = "String",paramType = "query")
    @ApiOperation(value="举报用户",notes = "举报用户的接口")
    public IMoocJSONResult reportUser(@RequestBody UsersReport usersReport, String userId) throws Exception {

        if(StringUtils.isBlank(userId)){
            return IMoocJSONResult.errorMsg("");
        }

        userService.saveReportUser(usersReport,userId);
        return IMoocJSONResult.ok("举报成功~");

    }
}

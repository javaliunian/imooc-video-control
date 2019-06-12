package com.imooc.controller;

import com.imooc.enums.VideoStatusEnum;
import com.imooc.pojo.Bgm;
import com.imooc.pojo.Comments;
import com.imooc.pojo.Videos;
import com.imooc.service.BgmService;
import com.imooc.service.VideoService;
import com.imooc.utils.FetchVideoCover;
import com.imooc.utils.IMoocJSONResult;
import com.imooc.utils.MergeVideoMp3;
import com.imooc.utils.PagedResult;
import io.swagger.annotations.*;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.util.StringUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.UUID;

/**
 * @author daile.sun
 * @date 2018/10/9
 */
@RestController
@Api(value = "视频的接口",tags = {"视频的controller"})
    @RequestMapping("/video")
public class VideoController extends BasicController{

    @Autowired
    private BgmService bgmService;

    @Autowired
    private VideoService videoService;


    @RequestMapping(value = "/upload",method = RequestMethod.POST,headers = "content-type=multipart/form-data")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId",value = "用户id",required = true,dataType = "String",paramType = "form"),
            @ApiImplicitParam(name = "bgmId",value = "背景音乐id",dataType = "String",paramType = "form"),
            @ApiImplicitParam(name = "videoSeconds",value = "视频长度",required = true,dataType = "double",paramType = "form"),
            @ApiImplicitParam(name = "videoWidth",value = "视频宽度",required = true,dataType = "int",paramType = "form"),
            @ApiImplicitParam(name = "videoHeight",value = "视频高度",required = true,dataType = "int",paramType = "form"),
            @ApiImplicitParam(name = "desc",value = "描述",dataType = "String",paramType = "form")
    })
    @ApiOperation(value="上传视频",notes = "上传视频的接口")
    public IMoocJSONResult upload(String userId,
                                  String bgmId,
                                  double videoSeconds, int videoWidth, int videoHeight, String desc,
                                  @ApiParam(value = "短视频",required = true) MultipartFile file) throws Exception {

        if(StringUtils.isBlank(userId)){
            return IMoocJSONResult.errorMsg("用户id不能为空...");
        }

        FileOutputStream fileOutputStream=null;
        InputStream inputStream=null;
        //文件保存的命名空间
//        String fileSpace="C:/imooc_video_dev";
        //保存到数据库中的相对路径
        String uploadPathDB="/"+userId+"/video";
        String coverPath="/"+userId+"/video";
        StringBuffer finalVideoPath=new StringBuffer();
        try {

            if(file!=null){


                String fileName = file.getOriginalFilename();

                String fileNamePrefix=fileName.split("\\.")[0];
                if(StringUtil.isNotEmpty(fileName)){
                    //文件上传后最终的保存路径
                    finalVideoPath=finalVideoPath.append(FILESPACE+uploadPathDB+"/"+fileName);
                    //设置数据库的保存路径
                    uploadPathDB+=("/"+fileName);
                    coverPath=coverPath+"/"+fileNamePrefix+".jpg";

                    File outFile=new File(finalVideoPath.toString());
                    if(outFile.getParentFile()!=null || !outFile.getParentFile().isDirectory()){
                        //创建文件夹
                        outFile.getParentFile().mkdirs();
                    }

                    fileOutputStream=new FileOutputStream(outFile);
                    inputStream=file.getInputStream();
                    IOUtils.copy(inputStream,fileOutputStream);
                }
            }else{
                return IMoocJSONResult.errorMsg("视频不能为空...");
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

        //判断bgmId是否为空，如果不为空，就查询bgm的信息，并和视频合并

        if(StringUtils.isNotBlank(bgmId)){
            Bgm bgm = bgmService.queryById(bgmId);
            String mp3InputPath=FILESPACE+bgm.getPath();

            MergeVideoMp3 tool = new MergeVideoMp3(FFMPEGEXE);
            String videoInputPath=finalVideoPath.toString();
            String outputName= UUID.randomUUID().toString()+".mp4";
            uploadPathDB="/"+userId+"/video/"+outputName;
            finalVideoPath=new StringBuffer(FILESPACE+uploadPathDB);

            tool.convertor(videoInputPath,mp3InputPath,videoSeconds,finalVideoPath.toString());
            System.out.println("uploadPathDB"+uploadPathDB);
            System.out.println("finalVideoPath"+finalVideoPath);

        }

        //对视频进行截图
        FetchVideoCover fetchVideoCover=new FetchVideoCover(FFMPEGEXE);
        fetchVideoCover.convertor(finalVideoPath.toString(),FILESPACE+coverPath);

        //保存视频信息到数据库
        Videos video = new Videos();
        video.setAudioId(bgmId);
        video.setUserId(userId);
        video.setVideoSeconds((float)videoSeconds);
        video.setVideoHeight(videoHeight);
        video.setVideoWidth(videoWidth);
        video.setVideoDesc(desc);
        video.setVideoPath(uploadPathDB);
        video.setCoverPath(coverPath);
        video.setStatus(VideoStatusEnum.SUCCESS.value);
        video.setCreateTime(new Date());

        String videoId=videoService.saveVideo(video);


        return IMoocJSONResult.ok(videoId);

    }


    @RequestMapping(value = "/uploadCover",method = RequestMethod.POST,headers = "content-type=multipart/form-data")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId",value = "用户id",required = true,dataType = "String",paramType = "form"),
            @ApiImplicitParam(name = "videoId",value = "视频id",required = true,dataType = "String",paramType = "form")
    })
    @ApiOperation(value="上传封面",notes = "上传封面的接口")
    public IMoocJSONResult uploadCover(String videoId,String userId,
                                  @ApiParam(value = "封面",required = true) MultipartFile file) throws Exception {

        if(StringUtils.isBlank(videoId)){
            return IMoocJSONResult.errorMsg("视频id不能为空...");
        }

        FileOutputStream fileOutputStream=null;
        InputStream inputStream=null;
        //文件保存的命名空间
//        String fileSpace="C:/imooc_video_dev";
        //保存到数据库中的相对路径
        String uploadPathDB="/"+userId+"/video";
        String finalVideoPath="";
        try {

            if(file!=null){


                String fileName = file.getOriginalFilename();
                if(StringUtil.isNotEmpty(fileName)){
                    //文件上传后最终的保存路径
                    finalVideoPath=FILESPACE+uploadPathDB+"/"+fileName;
                    //设置数据库的保存路径
                    uploadPathDB+=("/"+fileName);
                    File outFile=new File(finalVideoPath);
                    if(outFile.getParentFile()!=null || !outFile.getParentFile().isDirectory()){
                        //创建文件夹
                        outFile.getParentFile().mkdirs();
                    }

                    fileOutputStream=new FileOutputStream(outFile);
                    inputStream=file.getInputStream();
                    IOUtils.copy(inputStream,fileOutputStream);
                }
            }else{
                return IMoocJSONResult.errorMsg("视频不能为空...");
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

        videoService.updateVideo(videoId, uploadPathDB);

        return IMoocJSONResult.ok();

    }

    @RequestMapping(value = "/showAll",method = RequestMethod.POST)
    @ApiOperation(value="首页查询视频",notes = "首页查询视频的接口")
    public IMoocJSONResult showAll(@RequestParam(defaultValue = PAGE) Integer page,@RequestParam(defaultValue = PAGESIZE) Integer pageSize,
                                    @RequestBody Videos video,Integer isSaveRecord){

        PagedResult pagedResult = videoService.getAllVideos(video,isSaveRecord,page, pageSize);
        return IMoocJSONResult.ok(pagedResult);

    }

    @RequestMapping(value = "/hot",method = RequestMethod.POST)
    @ApiOperation(value="获取热搜词",notes = "获取热搜词的接口")
    public IMoocJSONResult hot(){

        return IMoocJSONResult.ok(videoService.getHotWords());

    }
    @RequestMapping(value = "/userLike",method = RequestMethod.POST)
    @ApiOperation(value="用户喜欢视频",notes = "用户喜欢视频的接口")
    public IMoocJSONResult userLike(String userId,String videoId,String videoCreaterId){
        videoService.userLikeVideo(userId,videoId,videoCreaterId);
        return IMoocJSONResult.ok();

    }
    @RequestMapping(value = "/userUnLike",method = RequestMethod.POST)
    @ApiOperation(value="用户不喜欢视频",notes = "用户不喜欢视频的接口")
    public IMoocJSONResult userUnLike(String userId,String videoId,String videoCreaterId){
        videoService.userUnLikeVideo(userId,videoId,videoCreaterId);
        return IMoocJSONResult.ok();

    }

    @RequestMapping(value = "/showMyLike",method = RequestMethod.POST)
    @ApiOperation(value="显示我收藏的视频",notes = "显示我收藏的视频")
    public IMoocJSONResult showMyLike(String userId,@RequestParam(defaultValue = PAGE) Integer page,@RequestParam(defaultValue = PAGESIZE) Integer pageSize){
        if(StringUtils.isBlank(userId)){
            return IMoocJSONResult.errorMsg("请登录...");
        }

        PagedResult pagedResult = videoService.showMyLike(userId,page,pageSize);
        return IMoocJSONResult.ok(pagedResult);

    }

    @RequestMapping(value = "/showMyFollow",method = RequestMethod.POST)
    @ApiOperation(value="显示我关注的人的视频",notes = "显示我关注的人的视频")
    public IMoocJSONResult showMyFollow(String userId,@RequestParam(defaultValue = PAGE) Integer page,@RequestParam(defaultValue = PAGESIZE) Integer pageSize){
        if(StringUtils.isBlank(userId)){
            return IMoocJSONResult.errorMsg("请登录...");
        }

        PagedResult pagedResult = videoService.showMyFollow(userId,page,pageSize);
        return IMoocJSONResult.ok(pagedResult);

    }

    @RequestMapping(value = "/saveComment",method = RequestMethod.POST)
    @ApiOperation(value="保存留言",notes = "保存留言")
    public IMoocJSONResult saveComment( @RequestBody Comments comments,String fatherCommentId,String toUserId){

        comments.setFatherCommentId(fatherCommentId);
        comments.setToUserId(toUserId);
        videoService.saveComments(comments);
        return IMoocJSONResult.ok();

    }

    @RequestMapping(value = "/getComment",method = RequestMethod.POST)
    @ApiOperation(value="查询留言",notes = "查询留言")
    public IMoocJSONResult getComment(String videoId,@RequestParam(defaultValue = PAGE) Integer page,@RequestParam(defaultValue = PAGESIZE) Integer pageSize){

        return IMoocJSONResult.ok(videoService.getComment(videoId,page,pageSize));

    }
}

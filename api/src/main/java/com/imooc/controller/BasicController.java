package com.imooc.controller;

import com.imooc.utils.RedisOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author daile.sun
 * @date 2018/10/8
 */
@RestController
public class BasicController {

    @Autowired
    public RedisOperator redis;

    public static final String USER_REDIS_SESSION="user-redis-session";

    //文件保存的命名空间
    public static final String FILESPACE="C:/imooc_video_dev";

    //ffmpeg所在目录
    public static final String FFMPEGEXE="D:/小程序/ffmpeg-4.0.2-win64-static/bin/ffmpeg.exe";

    //分页页数默认10
    public static final String PAGESIZE="5";

    //默认第一页
    public static final String PAGE="1";
}

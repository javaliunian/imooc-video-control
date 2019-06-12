package com.imooc.mapper;

import com.imooc.pojo.Videos;
import com.imooc.utils.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface VideosMapper extends MyMapper<Videos> {

    List<Videos> queryAllVideos(@Param("desc") String desc, @Param("userId") String userId);

    /**
     * 对视频的喜欢的数量累加
     * @param videoId
     */
    void addVideoLikeCounts(@Param("videoId") String videoId);

    /**
     * 对视频的喜欢的数量累减
     * @param videoId
     */
    void reduceVideoLikeCounts(@Param("videoId") String videoId);

    List<Videos> showMyLike(@Param("userId")String userId);

    List<Videos> showMyFollow(@Param("userId")String userId);
}
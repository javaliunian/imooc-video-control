package com.imooc.service;

import com.imooc.pojo.Comments;
import com.imooc.pojo.Videos;
import com.imooc.utils.PagedResult;

import java.util.List;

/**
 * @author daile.sun
 * @date 2018/10/9
 */
public interface VideoService {

    /**
     * 保存视频信息
     * @param video
     */
    String saveVideo(Videos video);

    /**
     * 修改视频的封面
     * @param videoId
     * @param coverPath
     */
    void updateVideo(String videoId,String coverPath);

    /**
     *  分页查询
     * @return
     */
    PagedResult getAllVideos(Videos video,Integer isSaveRecord,Integer page,Integer pageSize);


    /**
     * 获取热搜词列表
     * @return
     */
    List<String> getHotWords();

    /**
     * 用户喜欢视频
     * @param userId
     * @param videoId
     * @param videoCreaterId
     */
    void userLikeVideo(String userId,String videoId,String videoCreaterId);

    /**
     * 用户不喜欢视频/取消点赞
     * @param userId
     * @param videoId
     * @param videoCreaterId
     */
    void userUnLikeVideo(String userId,String videoId,String videoCreaterId);

    /**
     * 查询我收藏的视频列表
     * @param userId
     * @param page
     * @param pageSize
     * @return
     */
    PagedResult showMyLike(String userId, Integer page, Integer pageSize);

    /**
     * 查询我关注的人发布的视频
     * @param userId
     * @param page
     * @param pageSize
     * @return
     */
    PagedResult showMyFollow(String userId, Integer page, Integer pageSize);

    /**
     * 保存留言
     * @param comments
     */
    void saveComments(Comments comments);

    PagedResult getComment(String videoId, Integer page, Integer pageSize);
}

package com.imooc.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.imooc.mapper.*;
import com.imooc.pojo.Comments;
import com.imooc.pojo.SearchRecords;
import com.imooc.pojo.UsersLikeVideos;
import com.imooc.pojo.Videos;
import com.imooc.service.VideoService;
import com.imooc.utils.PagedResult;
import com.imooc.utils.TimeAgoUtils;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author daile.sun
 * @date 2018/10/9
 */
@Service
public class VideoServiceImpl implements VideoService {

    @Autowired
    private VideosMapper videosMapper;
    @Autowired
    private Sid sid;

    @Autowired
    private SearchRecordsMapper searchRecordsMapper;

    @Autowired
    private UsersLikeVideosMapper usersLikeVideosMapper;

    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private CommentsMapper commentsMapper;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public String saveVideo(Videos video) {

        video.setId(sid.nextShort());
        videosMapper.insertSelective(video);
        return video.getId();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateVideo(String videoId, String coverPath) {
        Videos videos = new Videos();
        videos.setId(videoId);
        videos.setCoverPath(coverPath);
        videosMapper.updateByPrimaryKeySelective(videos);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public PagedResult getAllVideos(Videos video,Integer isSaveRecord,Integer page, Integer pageSize) {
        //保存热搜词
        String desc=video.getVideoDesc();
        String userId=video.getUserId();
        if(isSaveRecord!=null && isSaveRecord==1){
            SearchRecords searchRecords=new SearchRecords();
            searchRecords.setId(sid.nextShort());
            searchRecords.setContent(desc);
            searchRecordsMapper.insert(searchRecords);
        }

        PageHelper.startPage(page,pageSize);
        List<Videos> list=videosMapper.queryAllVideos(desc,userId);
        PageInfo<Videos> pageList=new PageInfo<>(list);

        PagedResult pagedResult = new PagedResult();
        pagedResult.setPage(page);
        pagedResult.setTotal(pageList.getPages());

        pagedResult.setRecords(pageList.getTotal());
        pagedResult.setRows(list);

        return pagedResult;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<String> getHotWords() {

        return searchRecordsMapper.getHotWords();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void userLikeVideo(String userId, String videoId, String videoCreaterId) {
        //1.保存用户和视频的点赞喜欢关联关系表
        String likeId=sid.nextShort();

        UsersLikeVideos usersLikeVideos = new UsersLikeVideos();
        usersLikeVideos.setId(likeId);
        usersLikeVideos.setUserId(userId);
        usersLikeVideos.setVideoId(videoId);
        usersLikeVideosMapper.insert(usersLikeVideos);

        //2.视频喜欢数量累加
        videosMapper.addVideoLikeCounts(videoId );

        //3.用户受喜欢数量累加
        usersMapper.addReceiveLikeCounts(userId);
    }
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void userUnLikeVideo(String userId, String videoId, String videoCreaterId) {
        //1.删除用户和视频的点赞喜欢关联关系表
        UsersLikeVideos usersLikeVideos = new UsersLikeVideos();
        usersLikeVideos.setVideoId(videoId);
        usersLikeVideos.setUserId(userId);
        usersLikeVideosMapper.delete(usersLikeVideos);

        //2.视频喜欢数量累减
        videosMapper.reduceVideoLikeCounts(videoId );

        //3.用户受喜欢数量累减
        usersMapper.reduceReceiveLikeCounts(userId);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public PagedResult showMyLike(String userId, Integer page, Integer pageSize) {
        PageHelper.startPage(page,pageSize);
        List<Videos> list=videosMapper.showMyLike(userId);

        PageInfo<Videos> pageList=new PageInfo<>(list);

        PagedResult pagedResult = new PagedResult();
        pagedResult.setPage(page);
        pagedResult.setTotal(pageList.getPages());

        pagedResult.setRecords(pageList.getTotal());
        pagedResult.setRows(list);
        return pagedResult;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public PagedResult showMyFollow(String userId, Integer page, Integer pageSize) {
        PageHelper.startPage(page,pageSize);
        List<Videos> list=videosMapper.showMyFollow(userId);
        PageInfo<Videos> pageList=new PageInfo<>(list);

        PagedResult pagedResult = new PagedResult();
        pagedResult.setPage(page);
        pagedResult.setTotal(pageList.getPages());

        pagedResult.setRecords(pageList.getTotal());
        pagedResult.setRows(list);
        return pagedResult;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void saveComments(Comments comments) {
        comments.setId(sid.nextShort());
        comments.setCreateTime(new Date());
        commentsMapper.insert(comments);
    }

    @Override
    public PagedResult getComment(String videoId, Integer page, Integer pageSize) {
        PageHelper.startPage(page,pageSize);
        List<Comments> comments=commentsMapper.getComment(videoId);
        for(Comments comment:comments){
            comment.setTimeAgo(TimeAgoUtils.format(comment.getCreateTime()));
        }
        PageInfo<Comments> pageList=new PageInfo<>(comments);

        PagedResult pagedResult = new PagedResult();
        pagedResult.setPage(page);
        pagedResult.setTotal(pageList.getPages());

        pagedResult.setRecords(pageList.getTotal());
        pagedResult.setRows(comments);
        return pagedResult;

    }
}

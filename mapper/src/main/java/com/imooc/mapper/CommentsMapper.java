package com.imooc.mapper;

import com.imooc.pojo.Comments;
import com.imooc.utils.MyMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface CommentsMapper extends MyMapper<Comments> {
    List<Comments> getComment(String videoId);
}
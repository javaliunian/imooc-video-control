package com.imooc.mapper;

import com.imooc.pojo.SearchRecords;
import com.imooc.utils.MyMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface SearchRecordsMapper extends MyMapper<SearchRecords> {

    List<String> getHotWords();
}
package com.imooc.service;

import com.imooc.pojo.Bgm;

import java.util.List;

/**
 * @author daile.sun
 * @date 2018/10/9
 */
public interface BgmService {

    /**
     * 查询背景音乐列表
     * @return
     */
    List<Bgm> queryBgmList();

    /**
     * 通过id查询bgm信息
     * @param bgmId
     * @return
     */
    Bgm queryById(String bgmId);
}

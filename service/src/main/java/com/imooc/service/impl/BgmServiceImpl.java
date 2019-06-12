package com.imooc.service.impl;

import com.imooc.mapper.BgmMapper;
import com.imooc.pojo.Bgm;
import com.imooc.service.BgmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author daile.sun
 * @date 2018/10/9
 */
@Service
public class BgmServiceImpl implements BgmService {

    @Autowired
    private BgmMapper bgmMapper;

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<Bgm> queryBgmList() {
        return bgmMapper.selectAll();
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS,rollbackFor = Exception.class)
    public Bgm queryById(String bgmId) {
        return bgmMapper.selectByPrimaryKey(bgmId);
    }
}

package com.imooc.controller;

import com.imooc.pojo.Bgm;
import com.imooc.service.BgmService;
import com.imooc.utils.IMoocJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author daile.sun
 * @date 2018/10/9
 */
@RestController
@Api(value = "背景音乐的接口",tags = {"背景音乐的controller"})
@RequestMapping("/bgm")
public class BgmController {

    @Autowired
    private BgmService bgmService;

    @ApiOperation(value = "获取背景音乐列表",notes = "获取背景音乐列表的接口")
    @RequestMapping(value = "/list",method = RequestMethod.POST)
    public IMoocJSONResult list(){
        return IMoocJSONResult.ok(bgmService.queryBgmList());
    }


}

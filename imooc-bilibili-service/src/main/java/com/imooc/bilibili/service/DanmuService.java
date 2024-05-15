package com.imooc.bilibili.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.imooc.bilibili.domain.video.Danmu;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface DanmuService extends IService<Danmu> {
    void addDanmu(Danmu danmu);
    void asyncAddDanmu(Danmu danmu);

    List<Danmu> getDanmus(Map<String, Date> params);

    void addDanmuToRedis(Danmu danmu);
}

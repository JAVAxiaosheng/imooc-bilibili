package com.imooc.bilibili.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.imooc.bilibili.domain.video.CollectionGroup;

import java.util.List;

public interface CollectionGroupService extends IService<CollectionGroup> {
    void addCollectionGroup(CollectionGroup collectionGroup);

    void addDefaultCollectionGroup(Long userId);

    void deleteCollectionGroup(Long groupId);

    List<CollectionGroup> getCollectionGroupsByUserId(Long userId);
}

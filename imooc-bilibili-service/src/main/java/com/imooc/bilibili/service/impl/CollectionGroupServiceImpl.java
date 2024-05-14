package com.imooc.bilibili.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.imooc.bilibili.constant.VideoConstant;
import com.imooc.bilibili.domain.video.CollectionGroup;
import com.imooc.bilibili.exception.ConditionException;
import com.imooc.bilibili.mapper.CollectionGroupMapper;
import com.imooc.bilibili.service.CollectionGroupService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class CollectionGroupServiceImpl extends ServiceImpl<CollectionGroupMapper, CollectionGroup> implements CollectionGroupService {
    @Resource
    private CollectionGroupMapper collectionGroupMapper;

    @Override
    public void addCollectionGroup(CollectionGroup collectionGroup) {
        collectionGroupMapper.insert(collectionGroup);
    }

    @Override
    public void addDefaultCollectionGroup(Long userId) {
        CollectionGroup collectionGroup = new CollectionGroup();
        collectionGroup.setUserId(userId);
        collectionGroup.setName("默认分组");
        collectionGroup.setType("0");
        collectionGroup.setCreateTime(new Date());
        collectionGroup.setUpdateTime(new Date());
        collectionGroupMapper.insert(collectionGroup);
    }

    @Override
    public void deleteCollectionGroup(Long groupId) {
        CollectionGroup collectionGroup = collectionGroupMapper.selectById(groupId);
        if (ObjectUtil.isNull(collectionGroup)) {
            throw new ConditionException("没有找到该分组,groupId=" + groupId);
        }
        if (VideoConstant.COLLECTION_DEFAULT_GROUP_TYPE.equals(collectionGroup.getType())) {
            throw new ConditionException("不允许删除默认分组,groupId=" + groupId);
        }
        collectionGroupMapper.deleteById(groupId);
    }

    @Override
    public List<CollectionGroup> getCollectionGroupsByUserId(Long userId) {
        LambdaQueryWrapper<CollectionGroup> queryWrapper = new LambdaQueryWrapper<CollectionGroup>()
                .eq(CollectionGroup::getUserId, userId);
        return collectionGroupMapper.selectList(queryWrapper);
    }
}

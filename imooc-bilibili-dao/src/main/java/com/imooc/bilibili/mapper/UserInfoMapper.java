package com.imooc.bilibili.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.imooc.bilibili.domain.UserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

@Mapper
public interface UserInfoMapper extends BaseMapper<UserInfo> {
    Integer getOneInfoById(@Param("id") Integer id);

    List<UserInfo> batchGetUserInfoByUserIds(Set<Long> userIdList);
}

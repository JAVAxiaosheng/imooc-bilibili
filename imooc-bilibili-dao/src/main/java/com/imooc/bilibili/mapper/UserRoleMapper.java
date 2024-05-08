package com.imooc.bilibili.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.imooc.bilibili.domain.auth.UserRole;
import org.mapstruct.Mapper;

@Mapper
public interface UserRoleMapper extends BaseMapper<UserRole> {
}

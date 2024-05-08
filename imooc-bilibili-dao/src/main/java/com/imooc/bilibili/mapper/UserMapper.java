package com.imooc.bilibili.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.imooc.bilibili.domain.User;
import com.imooc.bilibili.domain.UserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    User getUserByPhone(String phone);

    User getUserByPhoneOrEmail(@Param("phoneOrEmail") String phoneOrEmail);

    Integer getCountUserInfosOnPage(Map<String, Object> params);

    List<UserInfo> getUserInfosOnPage(Map<String, Object> params);
}

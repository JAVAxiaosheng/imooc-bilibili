package com.imooc.bilibili.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.imooc.bilibili.constant.UserConstant;
import com.imooc.bilibili.domain.PageResult;
import com.imooc.bilibili.domain.RefreshToken;
import com.imooc.bilibili.domain.User;
import com.imooc.bilibili.domain.UserInfo;
import com.imooc.bilibili.exception.ConditionException;
import com.imooc.bilibili.mapper.UserInfoMapper;
import com.imooc.bilibili.mapper.UserMapper;
import com.imooc.bilibili.service.RefreshTokenService;
import com.imooc.bilibili.service.UserAuthService;
import com.imooc.bilibili.service.UserService;
import com.imooc.bilibili.util.MD5Util;
import com.imooc.bilibili.util.RSAUtil;
import com.imooc.bilibili.util.TokenUtil;
import com.mysql.cj.util.StringUtils;
import jdk.nashorn.internal.ir.CallNode;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;


@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Resource
    private UserMapper userMapper;

    @Resource
    private UserInfoMapper userInfoMapper;

    @Resource
    private UserAuthService userAuthService;

    @Resource
    private RefreshTokenService refreshTokenService;

    @Override
    public void addUser(User user) {
        String phone = user.getPhone();
        if (StringUtils.isNullOrEmpty(phone)) {
            throw new ConditionException("手机号不能为空!");
        }
        User dbUser = userMapper.getUserByPhone(phone);
        if (dbUser != null) {
            throw new ConditionException("该手机号已经被注册!");
        }
        Date now = new Date();
        String salt = String.valueOf(now.getTime());
        String password = user.getPassword();
        String rawPassword;
        try {
            rawPassword = RSAUtil.decrypt(password);
        } catch (Exception e) {
            throw new ConditionException("密码解密失败!");
        }
        String md5Password = MD5Util.sign(rawPassword, salt, "UTF-8");
        user.setSalt(salt);
        user.setPassword(md5Password);
        user.setCreateTime(now);
        user.setUpdateTime(now);
        userMapper.insert(user);
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(user.getId());
        userInfo.setNick(UserConstant.DEFAULT_NICK);
        userInfo.setBirth(UserConstant.DEFAULT_BIRTH);
        userInfo.setGender(UserConstant.GENDER_MALE);
        userInfo.setCreateTime(now);
        userInfo.setUpdateTime(now);
        userInfoMapper.insert(userInfo);
        // 添加默认权限角色
        userAuthService.addUserDefaultRole(user.getId());
    }

    @Override
    public String login(User user) throws Exception {
        String phone = user.getPhone() == null ? "" : user.getPhone();
        String email = user.getEmail() == null ? "" : user.getEmail();
        if (StringUtils.isNullOrEmpty(phone) && StringUtils.isNullOrEmpty(email)) {
            throw new ConditionException("参数异常!");
        }
        String phoneOrEmail = phone + email;
        User dbUser = userMapper.getUserByPhoneOrEmail(phoneOrEmail);
        if (dbUser == null) {
            throw new ConditionException("当前用户不存在!");
        }
        String rawPassword;
        String password = user.getPassword();
        try {
            rawPassword = RSAUtil.decrypt(password);
        } catch (Exception e) {
            throw new ConditionException("密码解密失败!");
        }
        String salt = dbUser.getSalt();
        String md5Password = MD5Util.sign(rawPassword, salt, "UTF-8");
        if (!md5Password.equals(dbUser.getPassword())) {
            throw new ConditionException("密码错误!");
        }
        return TokenUtil.generateToken(dbUser.getId());
    }

    @Override
    public User getInfoById(Long id) {
        User user = userMapper.selectById(id);
        LambdaQueryWrapper<UserInfo> queryWrapper = new LambdaQueryWrapper<UserInfo>()
                .eq(UserInfo::getUserId, id);
        UserInfo userInfo = userInfoMapper.selectOne(queryWrapper);
        user.setUserInfo(userInfo);
        return user;
    }

    @Override
    public void updateUserInfo(User user) throws Exception {
        Long userId = user.getId();
        User dbUser = userMapper.selectById(userId);
        if (dbUser == null) {
            throw new ConditionException("该用户不存在!");
        }
        String password = user.getPassword();
        if (!StringUtils.isNullOrEmpty(password)) {
            String rawPassword = RSAUtil.decrypt(password);
            String md5Password = MD5Util.sign(rawPassword, dbUser.getSalt(), "UTF-8");
            user.setPassword(md5Password);
        }
        user.setUpdateTime(new Date());
        LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<User>()
                .set(!StringUtils.isNullOrEmpty(user.getPhone()), User::getPhone, user.getPhone())
                .set(!StringUtils.isNullOrEmpty(user.getEmail()), User::getEmail, user.getEmail())
                .set(!StringUtils.isNullOrEmpty(user.getPassword()), User::getPassword, user.getPassword())
                .set(User::getUpdateTime, user.getUpdateTime())
                .eq(User::getId, userId);
        userMapper.update(user, updateWrapper);
    }

    @Override
    public void updateUserInfos(UserInfo userInfo) {
        userInfo.setUpdateTime(new Date());
        LambdaUpdateWrapper<UserInfo> updateWrapper = new LambdaUpdateWrapper<UserInfo>()
                .set(!StringUtils.isNullOrEmpty(userInfo.getNick()), UserInfo::getNick, userInfo.getNick())
                .set(!StringUtils.isNullOrEmpty(userInfo.getAvatar()), UserInfo::getAvatar, userInfo.getAvatar())
                .set(!StringUtils.isNullOrEmpty(userInfo.getBirth()), UserInfo::getBirth, userInfo.getBirth())
                .set(!StringUtils.isNullOrEmpty(userInfo.getSign()), UserInfo::getSign, userInfo.getSign())
                .set(!StringUtils.isNullOrEmpty(userInfo.getGender()), UserInfo::getGender, userInfo.getGender())
                .set(UserInfo::getUpdateTime, userInfo.getUpdateTime())
                .eq(UserInfo::getUserId, userInfo.getUserId());
        userInfoMapper.update(userInfo, updateWrapper);
    }

    @Override
    public PageResult<UserInfo> getUserInfosOnPage(JSONObject params) {
        Integer pageNum = params.getInteger("pageNum");
        Integer pageSize = params.getInteger("pageSize");
        params.put("start", (pageNum - 1) * pageSize);
        params.put("limit", pageSize);
        Integer total = userMapper.getCountUserInfosOnPage(params);
        List<UserInfo> userInfoList = new ArrayList<>();
        if (total > 0) {
            userInfoList = userMapper.getUserInfosOnPage(params);
        }
        return new PageResult<>(total, userInfoList);
    }

    @Override
    public Map<String, Object> loginForDts(User user) throws Exception {
        String phone = user.getPhone() == null ? "" : user.getPhone();
        String email = user.getEmail() == null ? "" : user.getEmail();
        if (StringUtils.isNullOrEmpty(phone) && StringUtils.isNullOrEmpty(email)) {
            throw new ConditionException("参数异常!");
        }
        String phoneOrEmail = phone + email;
        User dbUser = userMapper.getUserByPhoneOrEmail(phoneOrEmail);
        if (dbUser == null) {
            throw new ConditionException("当前用户不存在!");
        }
        String rawPassword;
        String password = user.getPassword();
        try {
            rawPassword = RSAUtil.decrypt(password);
        } catch (Exception e) {
            throw new ConditionException("密码解密失败!");
        }
        String salt = dbUser.getSalt();
        String md5Password = MD5Util.sign(rawPassword, salt, "UTF-8");
        if (!md5Password.equals(dbUser.getPassword())) {
            throw new ConditionException("密码错误!");
        }
        Long userId = dbUser.getId();
        String accessToken = TokenUtil.generateToken(userId);
        String refreshToken = TokenUtil.generateRefreshToken(userId);
        // 保存refreshToken到数据库
        Integer deleteId = refreshTokenService.deleteRefreshToken(refreshToken, userId);
        Integer addId = refreshTokenService.addRefreshToken(refreshToken, userId);
        Map<String, Object> result = new HashMap<>();
        result.put("accessToken", accessToken);
        result.put("refreshToken", refreshToken);
        return result;
    }

    @Override
    public void logout(Long userId, String refreshToken) {
        refreshTokenService.deleteRefreshToken(refreshToken, userId);
    }

    @Override
    public String refreshAccessToken(String refreshToken) throws Exception {
        RefreshToken rt = refreshTokenService.getRefreshTokenDetail(refreshToken);
        if (rt == null) {
            throw new ConditionException("555", "Token过期!");
        }
        Long userId = rt.getUserId();
        return TokenUtil.generateToken(userId);
    }
}

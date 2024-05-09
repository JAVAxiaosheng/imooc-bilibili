package com.imooc.bilibili.aspect;

import com.imooc.bilibili.constant.AuthRoleConstant;
import com.imooc.bilibili.domain.UserMoments;
import com.imooc.bilibili.domain.auth.UserRole;
import com.imooc.bilibili.exception.ConditionException;
import com.imooc.bilibili.service.UserRoleService;
import com.imooc.bilibili.support.UserSupport;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Order(1)
@Component
@Aspect
public class DataLimitedAspect {
    @Resource
    private UserSupport userSupport;

    @Resource
    private UserRoleService userRoleService;

    @Pointcut("@annotation(com.imooc.bilibili.annotation.DataLimited)")
    public void check() {
    }

    @Before("check()")
    public void before(JoinPoint joinPoint) {
        Long userId = userSupport.getCurrentUserId();
        List<UserRole> userRoleList = userRoleService.getUserRolesByUserId(userId);
        Set<String> roleCodeSet = userRoleList.stream().map(UserRole::getRoleCode).collect(Collectors.toSet());
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg instanceof UserMoments) {
                UserMoments userMoments = (UserMoments) arg;
                String type = userMoments.getType();
                if (roleCodeSet.contains(AuthRoleConstant.ROLE_CODE_LV0) && !"0".equals(type)) {
                    throw new ConditionException("参数异常!");
                }
            }
        }
    }
}

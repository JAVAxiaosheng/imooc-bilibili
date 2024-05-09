package com.imooc.bilibili.aspect;

import com.imooc.bilibili.annotation.ApiLimitedRole;
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
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Order(1)
@Component
@Aspect
public class ApiLimitedRoleAspect {
    @Resource
    private UserSupport userSupport;

    @Resource
    private UserRoleService userRoleService;

    @Pointcut("@annotation(com.imooc.bilibili.annotation.ApiLimitedRole)")
    public void check() {
    }

    @Before("check() && @annotation(apiLimitedRole)")
    public void doBefore(JoinPoint joinPoint, ApiLimitedRole apiLimitedRole) {
        Long userId = userSupport.getCurrentUserId();
        List<UserRole> userRoleList = userRoleService.getUserRolesByUserId(userId);
        String[] limitedRoleCodeList = apiLimitedRole.limitedRoleCodeList();
        Set<String> limitedRoleCodeSet = Arrays.stream(limitedRoleCodeList).collect(Collectors.toSet());
        Set<String> roleCodeSet = userRoleList.stream().map(UserRole::getRoleCode).collect(Collectors.toSet());
        roleCodeSet.retainAll(limitedRoleCodeSet);
        if (!roleCodeSet.isEmpty()) {
            throw new ConditionException("权限不足!");
        }
    }
}

package com.imooc.bilibili.controller;

import com.alibaba.fastjson.JSONObject;
import com.imooc.bilibili.domain.JsonResponse;
import com.imooc.bilibili.domain.PageResult;
import com.imooc.bilibili.domain.User;
import com.imooc.bilibili.domain.UserInfo;
import com.imooc.bilibili.service.UserFollowingService;
import com.imooc.bilibili.service.UserService;
import com.imooc.bilibili.support.UserSupport;
import com.imooc.bilibili.util.RSAUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Api(tags = "用户信息", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {
    @Resource
    private UserService userService;

    @Resource
    private UserFollowingService userFollowingService;

    @Resource
    private UserSupport userSupport;

    @GetMapping("/rsa-pks")
    @ApiOperation(value = "获取公钥", httpMethod = "GET")
    public JsonResponse<String> getRsaPublicKey() {
        String pk = RSAUtil.getPublicKeyStr();
        return new JsonResponse<>(pk);
    }

    @PostMapping("/add")
    @ApiOperation(value = "注册", httpMethod = "POST")
    public JsonResponse<String> addUser(@RequestBody User user) {
        userService.addUser(user);
        return JsonResponse.success();
    }

    @PostMapping("/tokens")
    @ApiOperation(value = "登录", httpMethod = "POST")
    public JsonResponse<String> login(@RequestBody User user) throws Exception {
        String token = userService.login(user);
        return new JsonResponse<>(token);
    }

    @GetMapping("/info")
    @ApiOperation(value = "获取用户信息", httpMethod = "GET")
    public JsonResponse<User> getUserInfo() {
        Long userId = userSupport.getCurrentUserId();
        User user = userService.getInfoById(userId);
        return new JsonResponse<>(user);
    }

    @PutMapping("/update")
    @ApiOperation(value = "更新用户信息", httpMethod = "PUT")
    public JsonResponse<String> updateUserInfo(@RequestBody User user) throws Exception {
        Long userId = userSupport.getCurrentUserId();
        user.setId(userId);
        userService.updateUserInfo(user);
        return JsonResponse.success();
    }

    @PutMapping("/update/info")
    @ApiOperation(value = "更新用户信息2", httpMethod = "PUT")
    public JsonResponse<String> updateUserInfos(@RequestBody UserInfo userInfo) throws Exception {
        Long userId = userSupport.getCurrentUserId();
        userInfo.setUserId(userId);
        userService.updateUserInfos(userInfo);
        return JsonResponse.success();
    }

    @GetMapping("/page/infos")
    @ApiOperation(value = "分页获取用户信息", httpMethod = "GET")
    public JsonResponse<PageResult<UserInfo>> getUserInfosOnPage(
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(value = "nick", required = false) String nick
    ) {
        Long userId = userSupport.getCurrentUserId();
        JSONObject params = new JSONObject();
        params.put("pageNum", pageNum);
        params.put("pageSize", pageSize);
        params.put("nick", nick);
        params.put("userId", userId);
        PageResult<UserInfo> result = userService.getUserInfosOnPage(params);
        if (result.getTotal() > 0) {
            // 检查用户的关注状态
            List<UserInfo> userInfoList = userFollowingService.checkFollowingStatus(result.getList(), userId);
            result.setList(userInfoList);
        }
        return new JsonResponse<>(result);
    }

    @PostMapping("/double/tokens")
    @ApiOperation(value = "双token登录验证", httpMethod = "POST")
    public JsonResponse<Map<String, Object>> loginForDts(@RequestBody User user) throws Exception {
        Map<String, Object> map = userService.loginForDts(user);
        return new JsonResponse<>(map);
    }

    @DeleteMapping("delete/double/tokens")
    @ApiOperation(value = "删除刷新token,退出登录", httpMethod = "DELETE")
    public JsonResponse<String> logout(HttpServletRequest request) {
        String refreshToken = request.getHeader("refreshToken");
        Long userId = userSupport.getCurrentUserId();
        userService.logout(userId, refreshToken);
        return JsonResponse.success();
    }

    @PostMapping("/access/tokens")
    @ApiOperation(value = "刷新accessToken", httpMethod = "POST")
    public JsonResponse<String> refreshAccessToken(HttpServletRequest request) throws Exception {
        String refreshToken = request.getHeader("refreshToken");
        String accessToken = userService.refreshAccessToken(refreshToken);
        return new JsonResponse<>(accessToken);
    }


}

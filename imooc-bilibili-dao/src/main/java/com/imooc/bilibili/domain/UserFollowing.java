package com.imooc.bilibili.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@TableName("t_user_following")
@ApiModel(value = "UserFollowing对象", description = "用户关注表")
public class UserFollowing {
    @TableId(type = IdType.AUTO)
    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("userId")
    private Long userId;

    @ApiModelProperty("关注用户Id")
    private Long followingId;

    @ApiModelProperty("关注分组Id")
    private Long groupId;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @TableField(exist = false)
    private UserInfo userInfo;
}

package com.imooc.bilibili.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@TableName("t_user_moments")
@ApiModel(value = "UserMoments对象", description = "用户动态信息表")
public class UserMoments {
    @TableId(type = IdType.AUTO)
    @ApiModelProperty("id")
    private Long id;
    @ApiModelProperty("userId")
    private Long userId;
    @ApiModelProperty("动态类型")
    private String type;
    @ApiModelProperty("内容详情Id")
    private Long contentId;
    @ApiModelProperty("创建时间")
    private Date createTime;
    @ApiModelProperty("更新时间")
    private Date updateTime;
}

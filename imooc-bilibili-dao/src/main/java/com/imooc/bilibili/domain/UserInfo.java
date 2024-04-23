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
@TableName("t_user_info")
@ApiModel(value = "UserInfo对象", description = "用户基本信息表")
public class UserInfo {
    @TableId(type = IdType.AUTO)
    @ApiModelProperty("id")
    private Long id;
    @ApiModelProperty("userId")
    private Long userId;
    @ApiModelProperty("昵称")
    private String nick;
    @ApiModelProperty("头像")
    private String avatar;
    @ApiModelProperty("签名")
    private String sign;
    @ApiModelProperty("性别")
    private String gender;
    @ApiModelProperty("生日")
    private String birth;
    @ApiModelProperty("创建时间")
    private Date createTime;
    @ApiModelProperty("更新时间")
    private Date updateTime;

    @TableField(exist = false)
    @ApiModelProperty("关注状态")
    private Boolean followed;
}

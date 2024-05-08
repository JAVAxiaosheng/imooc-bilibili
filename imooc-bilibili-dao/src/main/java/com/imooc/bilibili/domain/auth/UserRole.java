package com.imooc.bilibili.domain.auth;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@TableName("t_user_role")
@ApiModel(value = "UserRole对象", description = "用户角色关联表")
public class UserRole {
    @TableId(type = IdType.AUTO)
    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("userId")
    private Long userId;

    @ApiModelProperty("roleId")
    private Long roleId;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @TableField(exist = false)
    @ApiModelProperty("角色名称")
    private String roleName;

    @TableField(exist = false)
    @ApiModelProperty("角色唯一编码")
    private String roleCode;

}

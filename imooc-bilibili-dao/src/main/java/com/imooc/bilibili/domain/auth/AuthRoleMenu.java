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
@TableName("t_auth_role_menu")
@ApiModel(value = "AuthRoleMenu对象", description = "角色与页面菜单关联表")
public class AuthRoleMenu {
    @TableId(type = IdType.AUTO)
    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("roleId")
    private Long roleId;

    @ApiModelProperty("menuId")
    private Long menuId;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @TableField(exist = false)
    private AuthMenu authMenu;

}

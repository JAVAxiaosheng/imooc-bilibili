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
@TableName("t_auth_role_element_operation")
@ApiModel(value = "AuthRoleElementOperation对象", description = "角色与元素操作关联表")
public class AuthRoleElementOperation {
    @TableId(type = IdType.AUTO)
    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("roleId")
    private Long roleId;

    @ApiModelProperty("elementOperationId")
    private Long elementOperationId;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @TableField(exist = false)
    private AuthElementOperation authElementOperation;
}

package com.imooc.bilibili.domain.auth;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@TableName("t_auth_element_operation")
@ApiModel(value = "AuthElementOperation对象", description = "页面元素操作表")
public class AuthElementOperation {
    @TableId(type = IdType.AUTO)
    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("页面元素名称")
    private String elementName;

    @ApiModelProperty("页面元素唯一编码")
    private String elementCode;

    @ApiModelProperty("页面元素类型")
    private String elementType;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;
}

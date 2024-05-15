package com.imooc.bilibili.domain.video;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@TableName("t_danmu")
@ApiModel(value = "Danmu对象", description = "弹幕")
public class Danmu {
    @TableId(type = IdType.AUTO)
    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("userId")
    private Long userId;

    @ApiModelProperty("videoId")
    private Long videoId;

    @ApiModelProperty("弹幕内容")
    private String content;

    @ApiModelProperty("弹幕出现时间")
    private String danmuTime;

    @ApiModelProperty("创建时间")
    private Date createTime;
}

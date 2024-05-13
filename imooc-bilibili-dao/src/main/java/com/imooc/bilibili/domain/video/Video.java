package com.imooc.bilibili.domain.video;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@TableName("t_video")
@ApiModel(value = "Video对象", description = "视频信息")
public class Video {
    @TableId(type = IdType.AUTO)
    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("userId")
    private Long userId;

    @ApiModelProperty("视频链接")
    private String url;

    @ApiModelProperty("封面链接")
    private String thumbnail;

    @ApiModelProperty("视频标题")
    private String title;

    @ApiModelProperty("视频类型")
    private String type;

    @ApiModelProperty("视频时长")
    private String duration;

    @ApiModelProperty("所在分区")
    private String area;

    @ApiModelProperty("视频简介")
    private String description;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

    @TableField(exist = false)
    @ApiModelProperty("视频标签列表")
    private List<VideoTag> videoTagList;
}

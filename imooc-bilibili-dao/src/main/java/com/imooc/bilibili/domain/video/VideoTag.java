package com.imooc.bilibili.domain.video;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@TableName("t_video_tag")
@ApiModel(value = "VideoTag对象", description = "视频和标签关联")
public class VideoTag {
    @TableId(type = IdType.AUTO)
    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("videoId")
    private Long videoId;

    @ApiModelProperty("tagId")
    private Long tagId;

    @ApiModelProperty("创建时间")
    private Date createTime;
}

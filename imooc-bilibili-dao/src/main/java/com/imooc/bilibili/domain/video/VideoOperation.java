package com.imooc.bilibili.domain.video;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@TableName("t_video_operation")
@ApiModel(value = "VideoOperation对象", description = "视频操作")
public class VideoOperation {
    @TableId(type = IdType.AUTO)
    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("userId")
    private Long userId;

    @ApiModelProperty("videoId")
    private Long videoId;

    @ApiModelProperty("操作类型")
    private String operationType;

    @ApiModelProperty("创建时间")
    private Date createTime;
}

package com.imooc.bilibili.domain.video;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@TableName("t_video_view")
@ApiModel(value = "VideoView对象", description = "视频观看记录")
public class VideoView {
    @TableId(type = IdType.AUTO)
    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("userId")
    private Long userId;

    @ApiModelProperty("videoId")
    private Long videoId;

    @ApiModelProperty("客户端Id")
    private String clientId;

    @ApiModelProperty("ip")
    private String ip;

    @ApiModelProperty("创建时间")
    private Date createTime;
}

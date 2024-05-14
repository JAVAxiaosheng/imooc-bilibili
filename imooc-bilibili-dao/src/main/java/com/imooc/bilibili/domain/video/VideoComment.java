package com.imooc.bilibili.domain.video;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.imooc.bilibili.domain.UserInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@TableName("t_video_comment")
@ApiModel(value = "VideoComment对象", description = "视频评论")
public class VideoComment {
    @TableId(type = IdType.AUTO)
    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("userId")
    private Long userId;

    @ApiModelProperty("videoId")
    private Long videoId;

    @ApiModelProperty("评论")
    private String comment;

    @ApiModelProperty("回复用户Id")
    private Long replyUserId;

    @ApiModelProperty("根节点评论Id")
    private Long rootId;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

    @TableField(exist = false)
    @ApiModelProperty("二级评论")
    private List<VideoComment> childList;

    @TableField(exist = false)
    @ApiModelProperty("关联用户信息")
    private UserInfo userInfo;

    @TableField(exist = false)
    @ApiModelProperty("二级评论回复的用户")
    private UserInfo replyUserInfo;
}

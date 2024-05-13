package com.imooc.bilibili.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@TableName("t_file_metadata")
@ApiModel(value = "FileMetadata对象", description = "文件元数据")
public class FileMetadata {
    @TableId(type = IdType.AUTO)
    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("dfs文件存储路径")
    private String dfsUrl;

    @ApiModelProperty("文件全名")
    private String fileOriginName;

    @ApiModelProperty("文件类型")
    private String fileType;

    @ApiModelProperty("文件md5值")
    private String fileMd5;

    @ApiModelProperty("创建时间")
    private Date createTime;
}

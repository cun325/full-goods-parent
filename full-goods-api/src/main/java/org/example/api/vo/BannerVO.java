package org.example.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 轮播图VO
 */
@Data
@ApiModel(description = "轮播图信息")
public class BannerVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "轮播图ID")
    private Long id;

    @ApiModelProperty(value = "图片URL")
    private String imageUrl;

    @ApiModelProperty(value = "链接URL")
    private String linkUrl;
}
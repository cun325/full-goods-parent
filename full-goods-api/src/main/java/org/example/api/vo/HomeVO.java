package org.example.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 首页数据VO
 */
@Data
@ApiModel(description = "首页数据")
public class HomeVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "轮播图列表")
    private List<BannerVO> banners;

    @ApiModelProperty(value = "分类列表")
    private List<CategoryVO> categories;

    @ApiModelProperty(value = "推荐水果列表")
    private List<FruitVO> recommendFruits;

    @ApiModelProperty(value = "新品上架列表")
    private List<FruitVO> newFruits;
}
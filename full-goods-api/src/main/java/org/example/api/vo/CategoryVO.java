package org.example.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 分类VO
 */
@Data
@ApiModel(description = "分类信息")
public class CategoryVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "分类ID")
    private Long id;

    @ApiModelProperty(value = "分类名称")
    private String name;

    @ApiModelProperty(value = "图标名称")
    private String iconName;

    @ApiModelProperty(value = "图标URL")
    private String iconUrl;
    
    @ApiModelProperty(value = "水果数量")
    private Integer fruitCount;
}
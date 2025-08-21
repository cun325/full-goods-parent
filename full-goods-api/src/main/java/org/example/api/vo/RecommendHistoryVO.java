package org.example.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 推荐历史记录VO
 */
@Data
@ApiModel(description = "推荐历史记录")
public class RecommendHistoryVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "历史记录ID")
    private Long id;

    @ApiModelProperty(value = "推荐条件")
    private String condition;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "时间描述，如：10分钟前")
    private String timeDesc;
}
package org.example.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 水果推荐请求DTO
 */
@Data
@ApiModel(description = "水果推荐请求参数")
public class FruitRecommendDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户token", example = "1234567890")
    private String token;

    @ApiModelProperty(value = "推荐条件", required = true, example = "适合孕妇的水果")
    @NotBlank(message = "推荐条件不能为空")
    private String condition;

    @ApiModelProperty(value = "最大推荐数量", example = "5")
    private Integer limit = 5;
}
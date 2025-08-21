package org.example.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * Token DTO
 */
@Data
@ApiModel(description = "Token请求参数")
public class TokenDTO {

    @NotBlank(message = "token不能为空")
    @ApiModelProperty(value = "用户token", required = true, example = "1234567890")
    private String token;
}
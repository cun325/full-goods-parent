package org.example.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 修改密码DTO
 */
@Data
@ApiModel(description = "修改密码请求参数")
public class ChangePasswordDTO {

    @NotBlank(message = "token不能为空")
    @ApiModelProperty(value = "用户token", required = true, example = "1234567890")
    private String token;

    @NotBlank(message = "旧密码不能为空")
    @ApiModelProperty(value = "旧密码", required = true, example = "123456")
    private String oldPassword;

    @NotBlank(message = "新密码不能为空")
    @ApiModelProperty(value = "新密码", required = true, example = "654321")
    private String newPassword;

    @NotBlank(message = "确认密码不能为空")
    @ApiModelProperty(value = "确认密码", required = true, example = "654321")
    private String confirmPassword;
}
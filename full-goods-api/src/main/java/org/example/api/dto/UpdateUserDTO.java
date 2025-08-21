package org.example.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 更新用户信息DTO
 */
@Data
@ApiModel(description = "更新用户信息请求参数")
public class UpdateUserDTO {

    @NotBlank(message = "token不能为空")
    @ApiModelProperty(value = "用户token", required = true, example = "1234567890")
    private String token;

    @ApiModelProperty(value = "用户名", example = "user123")
    private String username;

    @ApiModelProperty(value = "昵称", example = "鲜果达人")
    private String nickname;

    @ApiModelProperty(value = "头像URL", example = "https://example.com/avatar.jpg")
    private String avatar;

    @ApiModelProperty(value = "手机号", example = "13800138000")
    private String mobile;

    @ApiModelProperty(value = "性别", example = "男")
    private String gender;

    @ApiModelProperty(value = "生日", example = "1990-01-01")
    private String birthday;

    @ApiModelProperty(value = "邮箱", example = "user@example.com")
    private String email;

    @ApiModelProperty(value = "地址", example = "北京市朝阳区")
    private String address;

    @ApiModelProperty(value = "个人简介", example = "热爱生活，喜欢水果")
    private String bio;
}
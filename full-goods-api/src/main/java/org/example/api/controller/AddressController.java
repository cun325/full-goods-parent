package org.example.api.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.example.api.service.AddressService;
import org.example.api.service.UserService;
import org.example.api.vo.AddressVO;
import org.example.common.entity.Address;
import org.example.common.entity.User;
import org.example.common.response.Result;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 收货地址控制器
 */
@Slf4j
@RestController
@RequestMapping("/address")
@Api(tags = "收货地址接口")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @Autowired
    private UserService userService;

    @PostMapping("/list")
    @ApiOperation("获取用户收货地址列表")
    public Result<List<AddressVO>> getAddressList(@RequestParam String token) {
        try {
            // 验证token获取用户信息
            if (!StringUtils.hasText(token)) {
                return Result.failed("用户未登录");
            }

            User user = userService.getByToken(token);
            if (user == null) {
                return Result.failed("用户不存在或token已过期");
            }

            // 获取地址列表
            List<Address> addressList = addressService.getAddressList(user.getId());
            
            // 转换为VO列表
            List<AddressVO> addressVOList = addressList.stream().map(address -> {
                AddressVO addressVO = new AddressVO();
                BeanUtils.copyProperties(address, addressVO);
                return addressVO;
            }).collect(Collectors.toList());
            
            return Result.success(addressVOList);
        } catch (Exception e) {
            log.error("获取收货地址列表失败", e);
            return Result.failed("获取收货地址列表失败");
        }
    }

    @PostMapping("/default")
    @ApiOperation("获取用户默认收货地址")
    public Result<AddressVO> getDefaultAddress(@RequestParam String token) {
        try {
            // 验证token获取用户信息
            if (!StringUtils.hasText(token)) {
                return Result.failed("用户未登录");
            }

            User user = userService.getByToken(token);
            if (user == null) {
                return Result.failed("用户不存在或token已过期");
            }

            // 获取默认地址
            Address address = addressService.getDefaultAddress(user.getId());
            if (address == null) {
                return Result.failed("暂无默认收货地址");
            }
            
            // 转换为VO
            AddressVO addressVO = new AddressVO();
            BeanUtils.copyProperties(address, addressVO);
            
            return Result.success(addressVO);
        } catch (Exception e) {
            log.error("获取默认收货地址失败", e);
            return Result.failed("获取默认收货地址失败");
        }
    }

    @GetMapping("/detail/{addressId}")
    @ApiOperation("获取收货地址详情")
    public Result<AddressVO> getAddressDetail(@PathVariable Long addressId, @RequestParam String token) {
        try {
            // 验证token获取用户信息
            if (!StringUtils.hasText(token)) {
                return Result.failed("用户未登录");
            }

            User user = userService.getByToken(token);
            if (user == null) {
                return Result.failed("用户不存在或token已过期");
            }

            // 获取地址详情
            Address address = addressService.getAddressDetail(addressId);
            if (address == null) {
                return Result.failed("收货地址不存在");
            }

            // 验证地址是否属于当前用户
            if (!address.getUserId().equals(user.getId())) {
                return Result.failed("无权访问该收货地址");
            }
            
            // 转换为VO
            AddressVO addressVO = new AddressVO();
            BeanUtils.copyProperties(address, addressVO);
            
            return Result.success(addressVO);
        } catch (Exception e) {
            log.error("获取收货地址详情失败", e);
            return Result.failed("获取收货地址详情失败");
        }
    }

    @PostMapping("/add")
    @ApiOperation("添加收货地址")
    public Result<Boolean> addAddress(@Valid @RequestBody AddressVO addressVO, @RequestParam String token) {
        try {
            // 验证token获取用户信息
            if (!StringUtils.hasText(token)) {
                return Result.failed("用户未登录");
            }

            User user = userService.getByToken(token);
            if (user == null) {
                return Result.failed("用户不存在或token已过期");
            }

            // 转换为实体
            Address address = new Address();
            BeanUtils.copyProperties(addressVO, address);
            address.setUserId(user.getId());

            // 添加地址
            boolean success = addressService.addAddress(address);
            
            return success ? Result.success(true) : Result.failed("添加收货地址失败");
        } catch (Exception e) {
            log.error("添加收货地址失败", e);
            return Result.failed("添加收货地址失败");
        }
    }

    @PostMapping("/update")
    @ApiOperation("修改收货地址")
    public Result<Boolean> updateAddress(@Valid @RequestBody AddressVO addressVO, @RequestParam String token) {
        try {
            // 验证token获取用户信息
            if (!StringUtils.hasText(token)) {
                return Result.failed("用户未登录");
            }

            User user = userService.getByToken(token);
            if (user == null) {
                return Result.failed("用户不存在或token已过期");
            }

            // 转换为实体
            Address address = new Address();
            BeanUtils.copyProperties(addressVO, address);
            address.setUserId(user.getId());

            // 修改地址
            boolean success = addressService.updateAddress(address);
            
            return success ? Result.success(true) : Result.failed("修改收货地址失败");
        } catch (Exception e) {
            log.error("修改收货地址失败", e);
            return Result.failed("修改收货地址失败");
        }
    }

    @PostMapping("/delete/{addressId}")
    @ApiOperation("删除收货地址")
    public Result<Boolean> deleteAddress(@PathVariable Long addressId, @RequestParam String token) {
        try {
            // 验证token获取用户信息
            if (!StringUtils.hasText(token)) {
                return Result.failed("用户未登录");
            }

            User user = userService.getByToken(token);
            if (user == null) {
                return Result.failed("用户不存在或token已过期");
            }

            // 删除地址
            boolean success = addressService.deleteAddress(addressId, user.getId());
            
            return success ? Result.success(true) : Result.failed("删除收货地址失败");
        } catch (Exception e) {
            log.error("删除收货地址失败", e);
            return Result.failed("删除收货地址失败");
        }
    }

    @PostMapping("/setDefault/{addressId}")
    @ApiOperation("设置默认收货地址")
    public Result<Boolean> setDefaultAddress(@PathVariable Long addressId, @RequestParam String token) {
        try {
            // 验证token获取用户信息
            if (!StringUtils.hasText(token)) {
                return Result.failed("用户未登录");
            }

            User user = userService.getByToken(token);
            if (user == null) {
                return Result.failed("用户不存在或token已过期");
            }

            // 设置默认地址
            boolean success = addressService.setDefaultAddress(addressId, user.getId());
            
            return success ? Result.success(true) : Result.failed("设置默认收货地址失败");
        } catch (Exception e) {
            log.error("设置默认收货地址失败", e);
            return Result.failed("设置默认收货地址失败");
        }
    }
}
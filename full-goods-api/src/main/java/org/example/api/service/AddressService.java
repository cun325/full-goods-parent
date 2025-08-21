package org.example.api.service;

import org.example.common.entity.Address;

import java.util.List;

/**
 * 收货地址服务接口
 */
public interface AddressService {

    /**
     * 获取用户收货地址列表
     *
     * @param userId 用户ID
     * @return 收货地址列表
     */
    List<Address> getAddressList(Long userId);

    /**
     * 获取用户默认收货地址
     *
     * @param userId 用户ID
     * @return 默认收货地址
     */
    Address getDefaultAddress(Long userId);

    /**
     * 获取收货地址详情
     *
     * @param addressId 收货地址ID
     * @return 收货地址信息
     */
    Address getAddressDetail(Long addressId);

    /**
     * 添加收货地址
     *
     * @param address 收货地址信息
     * @return 是否成功
     */
    boolean addAddress(Address address);

    /**
     * 修改收货地址
     *
     * @param address 收货地址信息
     * @return 是否成功
     */
    boolean updateAddress(Address address);

    /**
     * 删除收货地址
     *
     * @param addressId 收货地址ID
     * @param userId    用户ID
     * @return 是否成功
     */
    boolean deleteAddress(Long addressId, Long userId);

    /**
     * 设置默认收货地址
     *
     * @param addressId 收货地址ID
     * @param userId    用户ID
     * @return 是否成功
     */
    boolean setDefaultAddress(Long addressId, Long userId);
}
package org.example.api.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.example.common.entity.Address;

import java.util.List;

/**
 * 收货地址Mapper接口
 */
@Mapper
public interface AddressMapper {

    /**
     * 根据用户ID查询收货地址列表
     *
     * @param userId 用户ID
     * @return 收货地址列表
     */
    List<Address> selectByUserId(Long userId);

    /**
     * 根据用户ID查询默认收货地址
     *
     * @param userId 用户ID
     * @return 默认收货地址
     */
    Address selectDefaultByUserId(Long userId);

    /**
     * 根据ID查询收货地址
     *
     * @param id 收货地址ID
     * @return 收货地址信息
     */
    Address selectById(Long id);

    /**
     * 新增收货地址
     *
     * @param address 收货地址信息
     * @return 影响行数
     */
    int insert(Address address);

    /**
     * 修改收货地址
     *
     * @param address 收货地址信息
     * @return 影响行数
     */
    int update(Address address);

    /**
     * 删除收货地址
     *
     * @param id 收货地址ID
     * @return 影响行数
     */
    int deleteById(Long id);

    /**
     * 清除用户默认收货地址
     *
     * @param userId 用户ID
     * @return 影响行数
     */
    int clearDefaultByUserId(Long userId);
}
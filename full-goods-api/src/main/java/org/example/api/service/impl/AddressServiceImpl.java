package org.example.api.service.impl;

import org.example.api.service.AddressService;
import org.example.common.entity.Address;
import org.example.api.mapper.AddressMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 收货地址服务实现类
 */
@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressMapper addressMapper;

    @Override
    public List<Address> getAddressList(Long userId) {
        return addressMapper.selectByUserId(userId);
    }

    @Override
    public Address getDefaultAddress(Long userId) {
        return addressMapper.selectDefaultByUserId(userId);
    }

    @Override
    public Address getAddressDetail(Long addressId) {
        return addressMapper.selectById(addressId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addAddress(Address address) {
        if (address.getCreateTime() == null) {
            address.setCreateTime(new Date());
        }
        if (address.getUpdateTime() == null) {
            address.setUpdateTime(new Date());
        }
        if (address.getIsDefault() != null && address.getIsDefault() == 1) {
            // 如果设置为默认地址，先将该用户的所有地址设置为非默认
            addressMapper.clearDefaultByUserId(address.getUserId());
        }
        return addressMapper.insert(address) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateAddress(Address address) {
        Address existAddress = addressMapper.selectById(address.getId());
        if (existAddress == null || !existAddress.getUserId().equals(address.getUserId())) {
            return false;
        }

        address.setUpdateTime(new Date());
        if (address.getIsDefault() != null && address.getIsDefault() == 1) {
            // 如果设置为默认地址，先将该用户的所有地址设置为非默认
            addressMapper.clearDefaultByUserId(address.getUserId());
        }
        return addressMapper.update(address) > 0;
    }

    @Override
    public boolean deleteAddress(Long addressId, Long userId) {
        Address address = addressMapper.selectById(addressId);
        if (address == null || !address.getUserId().equals(userId)) {
            return false;
        }
        return addressMapper.deleteById(addressId) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean setDefaultAddress(Long addressId, Long userId) {
        Address address = addressMapper.selectById(addressId);
        if (address == null || !address.getUserId().equals(userId)) {
            return false;
        }

        // 先将该用户的所有地址设置为非默认
        addressMapper.clearDefaultByUserId(userId);

        // 将指定地址设置为默认
        address.setIsDefault(1);
        address.setUpdateTime(new Date());
        return addressMapper.update(address) > 0;
    }
}
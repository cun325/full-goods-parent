package org.example.api.service;

import org.example.api.vo.FlashSaleVO;
import org.example.common.entity.FlashSale;

import java.util.List;

/**
 * 限时特惠服务接口
 */
public interface FlashSaleService {

    /**
     * 获取所有有效的限时特惠商品
     *
     * @return 限时特惠列表
     */
    List<FlashSaleVO> getActiveFlashSales();

    /**
     * 根据ID获取限时特惠
     *
     * @param id 限时特惠ID
     * @return 限时特惠信息
     */
    FlashSale getById(Long id);

    /**
     * 新增限时特惠
     *
     * @param flashSale 限时特惠信息
     * @return 是否成功
     */
    boolean save(FlashSale flashSale);

    /**
     * 修改限时特惠
     *
     * @param flashSale 限时特惠信息
     * @return 是否成功
     */
    boolean update(FlashSale flashSale);

    /**
     * 删除限时特惠
     *
     * @param id 限时特惠ID
     * @return 是否成功
     */
    boolean deleteById(Long id);

    /**
     * 更新已售数量
     *
     * @param id 限时特惠ID
     * @param quantity 购买数量
     * @return 是否成功
     */
    boolean updateSoldCount(Long id, Integer quantity);
}
package org.example.api.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.example.common.entity.FlashSale;
import org.example.api.vo.FlashSaleVO;

import java.util.List;

/**
 * 限时特惠Mapper接口
 */
@Mapper
public interface FlashSaleMapper {

    /**
     * 查询所有有效的限时特惠商品
     *
     * @return 限时特惠列表
     */
    List<FlashSaleVO> selectActiveFlashSales();

    /**
     * 根据ID查询限时特惠
     *
     * @param id 限时特惠ID
     * @return 限时特惠信息
     */
    FlashSale selectById(Long id);

    /**
     * 新增限时特惠
     *
     * @param flashSale 限时特惠信息
     * @return 影响行数
     */
    int insert(FlashSale flashSale);

    /**
     * 修改限时特惠
     *
     * @param flashSale 限时特惠信息
     * @return 影响行数
     */
    int update(FlashSale flashSale);

    /**
     * 删除限时特惠
     *
     * @param id 限时特惠ID
     * @return 影响行数
     */
    int deleteById(Long id);

    /**
     * 更新已售数量
     *
     * @param id 限时特惠ID
     * @param quantity 购买数量
     * @return 影响行数
     */
    int updateSoldCount(Long id, Integer quantity);
}
package org.example.api.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.example.common.entity.OrderItem;

import java.util.List;

/**
 * 订单项Mapper接口
 */
@Mapper
public interface OrderItemMapper {

    /**
     * 根据订单ID查询订单项列表
     *
     * @param orderId 订单ID
     * @return 订单项列表
     */
    List<OrderItem> selectByOrderId(Long orderId);

    /**
     * 根据订单编号查询订单项列表
     *
     * @param orderNo 订单编号
     * @return 订单项列表
     */
    List<OrderItem> selectByOrderNo(String orderNo);

    /**
     * 根据ID查询订单项
     *
     * @param id 订单项ID
     * @return 订单项信息
     */
    OrderItem selectById(Long id);

    /**
     * 批量新增订单项
     *
     * @param orderItems 订单项列表
     * @return 影响行数
     */
    int batchInsert(List<OrderItem> orderItems);

    /**
     * 新增订单项
     *
     * @param orderItem 订单项信息
     * @return 影响行数
     */
    int insert(OrderItem orderItem);

    /**
     * 修改订单项
     *
     * @param orderItem 订单项信息
     * @return 影响行数
     */
    int update(OrderItem orderItem);

    /**
     * 删除订单项
     *
     * @param id 订单项ID
     * @return 影响行数
     */
    int deleteById(Long id);

    /**
     * 根据订单ID删除订单项
     *
     * @param orderId 订单ID
     * @return 影响行数
     */
    int deleteByOrderId(Long orderId);

    /**
     * 根据订单编号删除订单项
     *
     * @param orderNo 订单编号
     * @return 影响行数
     */
    int deleteByOrderNo(String orderNo);
}
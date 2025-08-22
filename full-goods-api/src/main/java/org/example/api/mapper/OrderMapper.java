package org.example.api.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.common.entity.Order;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 订单Mapper接口
 */
@Mapper
public interface OrderMapper {

    /**
     * 分页查询所有订单
     *
     * @return 订单列表
     */
    List<Order> selectAllWithPage();

    /**
     * 根据条件分页查询订单列表
     *
     * @param search 搜索关键词（订单号、用户名等）
     * @param status 订单状态
     * @return 订单列表
     */
    List<Order> selectByConditionsWithPage(@Param("search") String search, @Param("status") Integer status);

    /**
     * 根据用户ID查询订单列表
     *
     * @param userId 用户ID
     * @return 订单列表
     */
    List<Order> selectByUserId(Long userId);

    /**
     * 根据用户ID和状态查询订单列表
     *
     * @param userId 用户ID
     * @param status 订单状态
     * @return 订单列表
     */
    List<Order> selectByUserIdAndStatus(Long userId, Integer status);

    /**
     * 根据订单编号查询订单
     *
     * @param orderNo 订单编号
     * @return 订单信息
     */
    Order selectByOrderNo(String orderNo);

    /**
     * 根据ID查询订单
     *
     * @param id 订单ID
     * @return 订单信息
     */
    Order selectById(Long id);

    /**
     * 新增订单
     *
     * @param order 订单信息
     * @return 影响行数
     */
    int insert(Order order);

    /**
     * 修改订单
     *
     * @param order 订单信息
     * @return 影响行数
     */
    int update(Order order);

    /**
     * 删除订单
     *
     * @param id 订单ID
     * @return 影响行数
     */
    int deleteById(Long id);

    /**
     * 根据日期范围查询订单数量
     *
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 订单数量
     */
    Integer countByDateRange(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    /**
     * 根据日期范围查询销售总额
     *
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 销售总额
     */
    BigDecimal sumSalesByDateRange(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    /**
     * 查询订单总数
     *
     * @return 订单总数
     */
    Long countTotal();

    /**
     * 查询今日订单数
     *
     * @return 今日订单数
     */
    Long countTodayOrders();

    /**
     * 查询今日销售额
     *
     * @return 今日销售额
     */
    BigDecimal sumTodayRevenue();

    /**
     * 根据状态查询订单数
     *
     * @param status 订单状态
     * @return 订单数
     */
    Long countByStatus(Integer status);

    /**
     * 计算月增长率
     *
     * @return 月增长率
     */
    Double calculateMonthlyGrowth();

    // ==================== Admin相关方法 ====================

    /**
     * 根据主键查询订单
     *
     * @param id 订单ID
     * @return 订单信息
     */
    Order selectByPrimaryKey(@Param("id") Long id);

    /**
     * 根据主键更新订单
     *
     * @param order 订单信息
     * @return 影响行数
     */
    int updateByPrimaryKey(Order order);

    /**
     * 根据主键删除订单
     *
     * @param id 订单ID
     * @return 影响行数
     */
    int deleteByPrimaryKey(@Param("id") Long id);

    /**
     * 查询指定时间之前创建的未付款订单
     *
     * @param beforeTime 时间点
     * @return 未付款订单列表
     */
    List<Order> findUnpaidOrdersBeforeTime(@Param("beforeTime") Date beforeTime);
}
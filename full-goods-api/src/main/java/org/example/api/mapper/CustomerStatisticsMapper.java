package org.example.api.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.common.entity.CustomerStatistics;

import java.util.Date;
import java.util.List;

/**
 * 客服统计Mapper接口
 */
@Mapper
public interface CustomerStatisticsMapper {

    /**
     * 分页查询所有统计记录
     *
     * @return 统计记录列表
     */
    List<CustomerStatistics> selectAllWithPage();

    /**
     * 根据条件分页查询统计记录
     *
     * @param search 搜索关键词（客服名称等）
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 统计记录列表
     */
    List<CustomerStatistics> selectByConditionsWithPage(@Param("search") String search, 
                                                        @Param("startDate") Date startDate, 
                                                        @Param("endDate") Date endDate);

    /**
     * 根据客服ID查询统计记录
     *
     * @param serviceId 客服ID
     * @return 统计记录列表
     */
    List<CustomerStatistics> selectByServiceId(Long serviceId);

    /**
     * 根据日期查询统计记录
     *
     * @param statisticsDate 统计日期
     * @return 统计记录列表
     */
    List<CustomerStatistics> selectByDate(Date statisticsDate);

    /**
     * 根据客服ID和日期查询统计记录
     *
     * @param serviceId 客服ID
     * @param statisticsDate 统计日期
     * @return 统计记录
     */
    CustomerStatistics selectByServiceIdAndDate(@Param("serviceId") Long serviceId, 
                                               @Param("statisticsDate") Date statisticsDate);

    /**
     * 根据ID查询统计记录
     *
     * @param id 统计记录ID
     * @return 统计记录信息
     */
    CustomerStatistics selectById(Long id);

    /**
     * 新增统计记录
     *
     * @param statistics 统计记录信息
     * @return 影响行数
     */
    int insert(CustomerStatistics statistics);

    /**
     * 修改统计记录
     *
     * @param statistics 统计记录信息
     * @return 影响行数
     */
    int update(CustomerStatistics statistics);

    /**
     * 删除统计记录
     *
     * @param id 统计记录ID
     * @return 影响行数
     */
    int deleteById(Long id);

    /**
     * 根据日期范围删除统计记录
     *
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 影响行数
     */
    int deleteByDateRange(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    /**
     * 统计客服工作量排行
     *
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param limit 限制数量
     * @return 客服工作量排行
     */
    List<CustomerStatistics> selectWorkloadRanking(@Param("startDate") Date startDate, 
                                                   @Param("endDate") Date endDate, 
                                                   @Param("limit") Integer limit);

    /**
     * 统计客服满意度排行
     *
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param limit 限制数量
     * @return 客服满意度排行
     */
    List<CustomerStatistics> selectSatisfactionRanking(@Param("startDate") Date startDate, 
                                                       @Param("endDate") Date endDate, 
                                                       @Param("limit") Integer limit);

    /**
     * 计算总体统计数据
     *
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 总体统计数据
     */
    CustomerStatistics selectOverallStatistics(@Param("startDate") Date startDate, 
                                               @Param("endDate") Date endDate);
}
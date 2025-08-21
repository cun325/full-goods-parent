package org.example.api.service;

import org.example.common.entity.CustomerStatistics;
import com.github.pagehelper.PageInfo;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 客服统计服务接口
 */
public interface CustomerStatisticsService {

    /**
     * 分页查询所有统计数据
     *
     * @param page 页码
     * @param size 每页大小
     * @return 分页结果
     */
    PageInfo<CustomerStatistics> getAllStatistics(int page, int size);

    /**
     * 根据条件分页查询统计数据
     *
     * @param page 页码
     * @param size 每页大小
     * @param search 搜索关键词（客服名称）
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 分页结果
     */
    PageInfo<CustomerStatistics> getStatisticsByConditions(int page, int size, String search, Date startDate, Date endDate);

    /**
     * 根据客服ID获取统计数据
     *
     * @param serviceId 客服ID
     * @return 统计数据列表
     */
    List<CustomerStatistics> getStatisticsByServiceId(Long serviceId);

    /**
     * 根据日期获取统计数据
     *
     * @param statisticsDate 统计日期
     * @return 统计数据列表
     */
    List<CustomerStatistics> getStatisticsByDate(Date statisticsDate);

    /**
     * 根据客服ID和日期获取统计数据
     *
     * @param serviceId 客服ID
     * @param statisticsDate 统计日期
     * @return 统计数据
     */
    CustomerStatistics getStatisticsByServiceIdAndDate(Long serviceId, Date statisticsDate);

    /**
     * 根据ID获取统计数据详情
     *
     * @param id 统计数据ID
     * @return 统计数据信息
     */
    CustomerStatistics getStatisticsById(Long id);

    /**
     * 创建统计数据
     *
     * @param statistics 统计数据信息
     * @return 创建的统计数据
     */
    CustomerStatistics createStatistics(CustomerStatistics statistics);

    /**
     * 更新统计数据
     *
     * @param statistics 统计数据信息
     * @return 更新后的统计数据
     */
    CustomerStatistics updateStatistics(CustomerStatistics statistics);

    /**
     * 删除统计数据
     *
     * @param id 统计数据ID
     * @return 是否删除成功
     */
    boolean deleteStatistics(Long id);

    /**
     * 根据日期范围删除统计数据
     *
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 删除的数量
     */
    int deleteByDateRange(Date startDate, Date endDate);

    /**
     * 获取客服工作量排行
     *
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param limit 限制数量
     * @return 工作量排行列表
     */
    List<CustomerStatistics> getWorkloadRanking(Date startDate, Date endDate, Integer limit);

    /**
     * 获取客服满意度排行
     *
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param limit 限制数量
     * @return 满意度排行列表
     */
    List<CustomerStatistics> getSatisfactionRanking(Date startDate, Date endDate, Integer limit);

    /**
     * 获取总体统计数据
     *
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 总体统计数据
     */
    CustomerStatistics getOverallStatistics(Date startDate, Date endDate);

    /**
     * 生成日统计数据
     *
     * @param statisticsDate 统计日期
     * @return 生成的统计数据数量
     */
    int generateDailyStatistics(Date statisticsDate);

    /**
     * 生成指定客服的日统计数据
     *
     * @param serviceId 客服ID
     * @param statisticsDate 统计日期
     * @return 生成的统计数据
     */
    CustomerStatistics generateServiceDailyStatistics(Long serviceId, Date statisticsDate);

    /**
     * 获取客服效率分析
     *
     * @param serviceId 客服ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 效率分析数据
     */
    Map<String, Object> getEfficiencyAnalysis(Long serviceId, Date startDate, Date endDate);

    /**
     * 获取客服趋势分析
     *
     * @param serviceId 客服ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 趋势分析数据
     */
    Map<String, Object> getTrendAnalysis(Long serviceId, Date startDate, Date endDate);

    /**
     * 获取团队统计概览
     *
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 团队统计概览
     */
    Map<String, Object> getTeamOverview(Date startDate, Date endDate);

    /**
     * 导出统计数据
     *
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param serviceIds 客服ID列表（可选）
     * @return 导出文件路径
     */
    String exportStatistics(Date startDate, Date endDate, List<Long> serviceIds);
}
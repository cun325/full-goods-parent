package org.example.api.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.example.api.mapper.CustomerStatisticsMapper;
import org.example.api.service.CustomerStatisticsService;
import org.example.common.entity.CustomerStatistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 客服统计Service实现类
 */
@Slf4j
@Service
public class CustomerStatisticsServiceImpl implements CustomerStatisticsService {

    @Autowired
    private CustomerStatisticsMapper customerStatisticsMapper;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public PageInfo<CustomerStatistics> getAllStatistics(int page, int size) {
        PageHelper.startPage(page, size);
        List<CustomerStatistics> statistics = customerStatisticsMapper.selectAllWithPage();
        return new PageInfo<>(statistics);
    }

    @Override
    public PageInfo<CustomerStatistics> getStatisticsByConditions(int page, int size, String search, Date startDate, Date endDate) {
        PageHelper.startPage(page, size);
        List<CustomerStatistics> statistics = customerStatisticsMapper.selectByConditionsWithPage(search, startDate, endDate);
        return new PageInfo<>(statistics);
    }

    @Override
    public List<CustomerStatistics> getStatisticsByServiceId(Long serviceId) {
        try {
            return customerStatisticsMapper.selectByServiceId(serviceId);
        } catch (Exception e) {
            log.error("根据客服ID查询统计失败，客服ID: {}", serviceId, e);
            return null;
        }
    }

    @Override
    public List<CustomerStatistics> getStatisticsByDate(Date statisticsDate) {
        try {
            return customerStatisticsMapper.selectByDate(statisticsDate);
        } catch (Exception e) {
            log.error("根据日期查询统计失败，日期: {}", statisticsDate, e);
            return null;
        }
    }

    @Override
    public CustomerStatistics getStatisticsByServiceIdAndDate(Long serviceId, Date statisticsDate) {
        try {
            return customerStatisticsMapper.selectByServiceIdAndDate(serviceId, statisticsDate);
        } catch (Exception e) {
            log.error("根据客服ID和日期查询统计失败，客服ID: {}, 日期: {}", serviceId, statisticsDate, e);
            return null;
        }
    }

    @Override
    public CustomerStatistics getStatisticsById(Long id) {
        try {
            return customerStatisticsMapper.selectById(id);
        } catch (Exception e) {
            log.error("根据ID查询统计失败，ID: {}", id, e);
            return null;
        }
    }

    @Override
    @Transactional
    public CustomerStatistics createStatistics(CustomerStatistics statistics) {
        try {
            if (statistics.getCreateTime() == null) {
                statistics.setCreateTime(new Date());
            }
            if (statistics.getUpdateTime() == null) {
                statistics.setUpdateTime(new Date());
            }
            if (customerStatisticsMapper.insert(statistics) > 0) {
                return statistics;
            }
            return null;
        } catch (Exception e) {
            log.error("创建统计失败", e);
            return null;
        }
    }

    @Override
    @Transactional
    public CustomerStatistics updateStatistics(CustomerStatistics statistics) {
        try {
            statistics.setUpdateTime(new Date());
            if (customerStatisticsMapper.update(statistics) > 0) {
                return statistics;
            }
            return null;
        } catch (Exception e) {
            log.error("更新统计失败，ID: {}", statistics.getId(), e);
            return null;
        }
    }

    @Override
    @Transactional
    public boolean deleteStatistics(Long id) {
        try {
            return customerStatisticsMapper.deleteById(id) > 0;
        } catch (Exception e) {
            log.error("删除统计失败，ID: {}", id, e);
            return false;
        }
    }

    @Override
    @Transactional
    public int deleteByDateRange(Date startDate, Date endDate) {
        try {
            return customerStatisticsMapper.deleteByDateRange(startDate, endDate);
        } catch (Exception e) {
            log.error("根据日期范围删除统计失败，开始日期: {}, 结束日期: {}", startDate, endDate, e);
            return 0;
        }
    }

    @Override
    public List<CustomerStatistics> getWorkloadRanking(Date startDate, Date endDate, Integer limit) {
        try {
            return customerStatisticsMapper.selectWorkloadRanking(startDate, endDate, limit);
        } catch (Exception e) {
            log.error("获取工作量排行失败，开始日期: {}, 结束日期: {}, 限制: {}", startDate, endDate, limit, e);
            return null;
        }
    }

    @Override
    public List<CustomerStatistics> getSatisfactionRanking(Date startDate, Date endDate, Integer limit) {
        try {
            return customerStatisticsMapper.selectSatisfactionRanking(startDate, endDate, limit);
        } catch (Exception e) {
            log.error("获取满意度排行失败，开始日期: {}, 结束日期: {}, 限制: {}", startDate, endDate, limit, e);
            return null;
        }
    }

    @Override
    public CustomerStatistics getOverallStatistics(Date startDate, Date endDate) {
        try {
            return customerStatisticsMapper.selectOverallStatistics(startDate, endDate);
        } catch (Exception e) {
            log.error("获取总体统计失败，开始日期: {}, 结束日期: {}", startDate, endDate, e);
            return null;
        }
    }

    @Override
    @Transactional
    public int generateDailyStatistics(Date statisticsDate) {
        try {
            String dateStr = dateFormat.format(statisticsDate);
            log.info("开始生成日期 {} 的统计数据", dateStr);
            
            int generatedCount = 0;
            // 示例逻辑：为每个客服生成统计数据
            for (int serviceId = 1; serviceId <= 10; serviceId++) {
                CustomerStatistics statistics = new CustomerStatistics();
                statistics.setStatisticsDate(statisticsDate);
                statistics.setServiceId((long) serviceId);
                statistics.setServiceName("客服" + serviceId);
                statistics.setDialogCount((int) (Math.random() * 50) + 10);
                statistics.setMessageCount((int) (Math.random() * 200) + 50);
                statistics.setAvgResponseTime((int) (Math.random() * 300) + 30);
                statistics.setAvgSatisfaction(Math.random() * 2 + 3);
                statistics.setResolvedCount((int) (Math.random() * 40) + 5);
                statistics.setOnlineMinutes((int) (Math.random() * 480) + 240);
                statistics.setEfficiencyScore(Math.random() * 20 + 80);
                
                if (createStatistics(statistics) != null) {
                    generatedCount++;
                }
            }
            
            log.info("完成生成日期 {} 的统计数据，共生成 {} 条", dateStr, generatedCount);
            return generatedCount;
        } catch (Exception e) {
            log.error("生成日统计数据失败，日期: {}", statisticsDate, e);
            return 0;
        }
    }

    @Override
    @Transactional
    public CustomerStatistics generateServiceDailyStatistics(Long serviceId, Date statisticsDate) {
        try {
            String dateStr = dateFormat.format(statisticsDate);
            log.info("开始生成客服 {} 在日期 {} 的统计数据", serviceId, dateStr);
            
            CustomerStatistics statistics = new CustomerStatistics();
            statistics.setStatisticsDate(statisticsDate);
            statistics.setServiceId(serviceId);
            statistics.setServiceName("客服" + serviceId);
            statistics.setDialogCount((int) (Math.random() * 50) + 10);
            statistics.setMessageCount((int) (Math.random() * 200) + 50);
            statistics.setAvgResponseTime((int) (Math.random() * 300) + 30);
            statistics.setAvgSatisfaction(Math.random() * 2 + 3);
            statistics.setResolvedCount((int) (Math.random() * 40) + 5);
            statistics.setOnlineMinutes((int) (Math.random() * 480) + 240);
            statistics.setEfficiencyScore(Math.random() * 20 + 80);
            
            return createStatistics(statistics);
        } catch (Exception e) {
            log.error("生成客服日统计数据失败，客服ID: {}, 日期: {}", serviceId, statisticsDate, e);
            return null;
        }
    }

    @Override
    public Map<String, Object> getEfficiencyAnalysis(Long serviceId, Date startDate, Date endDate) {
        try {
            Map<String, Object> analysis = new HashMap<>();
            List<CustomerStatistics> statistics = getStatisticsByServiceId(serviceId);
            
            if (statistics != null && !statistics.isEmpty()) {
                double avgEfficiency = statistics.stream()
                    .mapToDouble(CustomerStatistics::getEfficiencyScore)
                    .average()
                    .orElse(0.0);
                
                double avgResponseTime = statistics.stream()
                    .mapToInt(CustomerStatistics::getAvgResponseTime)
                    .average()
                    .orElse(0.0);
                
                int totalDialogs = statistics.stream()
                    .mapToInt(CustomerStatistics::getDialogCount)
                    .sum();
                
                analysis.put("avgEfficiency", avgEfficiency);
                analysis.put("avgResponseTime", avgResponseTime);
                analysis.put("totalDialogs", totalDialogs);
                analysis.put("period", dateFormat.format(startDate) + " 至 " + dateFormat.format(endDate));
            }
            
            return analysis;
        } catch (Exception e) {
            log.error("获取效率分析失败，客服ID: {}, 开始日期: {}, 结束日期: {}", serviceId, startDate, endDate, e);
            return new HashMap<>();
        }
    }

    @Override
    public Map<String, Object> getTrendAnalysis(Long serviceId, Date startDate, Date endDate) {
        try {
            Map<String, Object> trend = new HashMap<>();
            log.info("获取客服 {} 的趋势分析，开始日期: {}, 结束日期: {}", serviceId, startDate, endDate);
            
            // 这里应该实现具体的趋势分析逻辑
            trend.put("serviceId", serviceId);
            trend.put("startDate", startDate);
            trend.put("endDate", endDate);
            trend.put("trendData", "暂未实现");
            
            return trend;
        } catch (Exception e) {
            log.error("获取趋势分析失败，客服ID: {}, 开始日期: {}, 结束日期: {}", serviceId, startDate, endDate, e);
            return new HashMap<>();
        }
    }

    @Override
    public Map<String, Object> getTeamOverview(Date startDate, Date endDate) {
        try {
            Map<String, Object> overview = new HashMap<>();
            
            // 获取总体统计
            CustomerStatistics overallStats = getOverallStatistics(startDate, endDate);
            if (overallStats != null) {
                overview.put("totalDialogs", overallStats.getDialogCount());
                overview.put("totalMessages", overallStats.getMessageCount());
                overview.put("avgSatisfaction", overallStats.getAvgSatisfaction());
                overview.put("avgEfficiency", overallStats.getEfficiencyScore());
            }
            
            // 获取工作量排行前5
            List<CustomerStatistics> workloadRanking = getWorkloadRanking(startDate, endDate, 5);
            overview.put("topPerformers", workloadRanking);
            
            // 获取满意度排行前5
            List<CustomerStatistics> satisfactionRanking = getSatisfactionRanking(startDate, endDate, 5);
            overview.put("topSatisfaction", satisfactionRanking);
            
            return overview;
        } catch (Exception e) {
            log.error("获取团队概览失败，开始日期: {}, 结束日期: {}", startDate, endDate, e);
            return new HashMap<>();
        }
    }

    @Override
    public String exportStatistics(Date startDate, Date endDate, List<Long> serviceIds) {
        try {
            String startDateStr = dateFormat.format(startDate);
            String endDateStr = dateFormat.format(endDate);
            log.info("导出统计数据，开始日期: {}, 结束日期: {}, 客服IDs: {}", startDateStr, endDateStr, serviceIds);
            
            // 这里应该实现导出逻辑，生成Excel或CSV文件
            // 返回文件路径或下载链接
            
            return "/exports/statistics_" + startDateStr + "_" + endDateStr + ".xlsx";
        } catch (Exception e) {
            log.error("导出统计数据失败，开始日期: {}, 结束日期: {}, 客服IDs: {}", startDate, endDate, serviceIds, e);
            return null;
        }
    }
}
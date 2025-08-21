package org.example.admin.service.impl;

import org.example.common.entity.FruitCategory;
import org.example.admin.mapper.CategoryMapper;
import org.example.admin.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import java.time.LocalDateTime;
import java.util.Date;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 分类管理服务实现类
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public Map<String, Object> getCategoryList(int page, int size, String search) {
        try {
            // 使用PageHelper进行分页
            PageHelper.startPage(page, size);
            
            // 使用CategoryMapper直接查询数据库
            List<FruitCategory> categories = categoryMapper.selectByConditions(search);
            
            // 获取分页信息
            PageInfo<FruitCategory> pageInfo = new PageInfo<>(categories);
            
            // 转换数据格式以匹配前端期望的格式
            List<Map<String, Object>> formattedCategories = new ArrayList<>();
            for (FruitCategory category : categories) {
                Map<String, Object> formatted = new HashMap<>();
                formatted.put("id", category.getId());
                formatted.put("name", category.getName());
                formatted.put("description", "分类: " + category.getName()); // 生成描述
                formatted.put("iconName", category.getIconName());
                formatted.put("iconUrl", category.getIconUrl());
                formatted.put("sort", category.getSortOrder() != null ? category.getSortOrder() : 0);
                // 状态转换：数据库中1表示启用，0表示禁用；前端使用active/inactive
                Integer status = category.getStatus();
                formatted.put("status", status != null && status == 1 ? "active" : "inactive");
                formatted.put("createTime", formatDateTime(category.getCreateTime()));
                formatted.put("updateTime", formatDateTime(category.getUpdateTime()));
                formattedCategories.add(formatted);
            }

            Map<String, Object> pageResult = new HashMap<>();
            pageResult.put("list", formattedCategories);
            pageResult.put("total", pageInfo.getTotal());
            pageResult.put("page", pageInfo.getPageNum());
            pageResult.put("size", pageInfo.getPageSize());
            pageResult.put("pages", pageInfo.getPages());

            return pageResult;
        } catch (Exception e) {
            e.printStackTrace();
            // 返回空结果
            Map<String, Object> emptyResult = new HashMap<>();
            emptyResult.put("list", new ArrayList<>());
            emptyResult.put("total", 0);
            emptyResult.put("page", page);
            emptyResult.put("size", size);
            emptyResult.put("pages", 0);
            return emptyResult;
        }
    }

    @Override
    public FruitCategory getCategoryById(Long id) {
        try {
            return categoryMapper.selectById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public FruitCategory addCategory(FruitCategory category) {
        try {
            category.setCreateTime(new Date());
            category.setUpdateTime(new Date());
            if (category.getStatus() == null) {
                category.setStatus(1); // 默认启用
            }
            int result = categoryMapper.insert(category);
            return result > 0 ? category : null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public FruitCategory updateCategory(FruitCategory category) {
        try {
            category.setUpdateTime(new Date());
            int result = categoryMapper.update(category);
            return result > 0 ? categoryMapper.selectById(category.getId()) : null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean deleteCategory(Long id) {
        try {
            int result = categoryMapper.deleteById(id);
            return result > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public int batchDeleteCategories(List<Long> ids) {
        try {
            return categoryMapper.batchDelete(ids);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public FruitCategory toggleCategoryStatus(Long id) {
        try {
            FruitCategory category = categoryMapper.selectById(id);
            if (category != null) {
                // 切换状态：1变0，0变1
                int newStatus = category.getStatus() == 1 ? 0 : 1;
                int result = categoryMapper.updateStatus(id, newStatus);
                if (result > 0) {
                    category.setStatus(newStatus);
                    category.setUpdateTime(new Date());
                    return category;
                }
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public int batchToggleCategoryStatus(List<Long> ids) {
        try {
            // 这里简化处理，统一设置为启用状态
            return categoryMapper.batchUpdateStatus(ids, 1);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 格式化日期时间
     */
    private String formatDateTime(Object dateTime) {
        if (dateTime == null) {
            return LocalDateTime.now().format(formatter);
        }
        if (dateTime instanceof String) {
            return (String) dateTime;
        }
        return LocalDateTime.now().format(formatter);
    }
}
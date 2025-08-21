package org.example.admin.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.admin.service.AdminProductService;
import org.example.common.entity.Fruit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.*;

/**
 * 商品管理服务实现类
 */
@Slf4j
@Service
public class AdminProductServiceImpl implements AdminProductService {

    @Autowired
    private RestTemplate restTemplate;

    private static final String API_BASE_URL = "http://localhost:8080/api";

    @Override
    public Map<String, Object> getProductList(int page, int size, String search, String category, String status) {
        try {
            // 构建查询参数
            StringBuilder url = new StringBuilder(API_BASE_URL + "/fruit/admin/list?page=" + page + "&size=" + size);
            if (search != null && !search.trim().isEmpty()) {
                url.append("&search=").append(search);
            }
            if (category != null && !category.trim().isEmpty()) {
                url.append("&category=").append(category);
            }
            if (status != null && !status.trim().isEmpty()) {
                url.append("&status=").append(status);
            }

            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                url.toString(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Map<String, Object>>() {}
            );

            Map<String, Object> result = response.getBody();
            if (result != null && result.get("code").equals(200)) {
                // 提取data字段中的PageInfo数据
                Map<String, Object> data = (Map<String, Object>) result.get("data");
                Map<String, Object> adminResult = new HashMap<>();
                adminResult.put("list", data.get("list"));
                adminResult.put("total", data.get("total"));
                adminResult.put("page", data.get("pageNum"));
                adminResult.put("size", data.get("pageSize"));
                return adminResult;
            } else {
                throw new RuntimeException("API调用失败");
            }
        } catch (Exception e) {
            throw new RuntimeException("获取商品列表失败: " + e.getMessage());
        }
    }

    @Override
    public Fruit getProductById(Long id) {
        try {
            ResponseEntity<Fruit> response = restTemplate.getForEntity(
                API_BASE_URL + "/fruit/" + id,
                Fruit.class
            );
            return response.getBody();
        } catch (Exception e) {
            throw new RuntimeException("获取商品详情失败: " + e.getMessage());
        }
    }

    @Override
    public void addProduct(Fruit fruit) {
        try {
            restTemplate.postForObject(API_BASE_URL + "/fruit/admin/add", fruit, String.class);
            log.info("添加商品成功: " + fruit.getName());
        } catch (Exception e) {
            log.error("添加商品失败: " + e.getMessage());
            throw new RuntimeException("添加商品失败: " + e.getMessage());
        }
    }

    @Override
    public void updateProduct(Fruit fruit) {
        try {
            restTemplate.put(API_BASE_URL + "/fruit/admin/update/" + fruit.getId(), fruit);
            log.info("更新商品成功: " + fruit.getName());
        } catch (Exception e) {
            log.error("更新商品失败: " + e.getMessage());
            throw new RuntimeException("更新商品失败: " + e.getMessage());
        }
    }

    @Override
    public void deleteProduct(Long id) {
        try {
            restTemplate.delete(API_BASE_URL + "/fruit/admin/delete/" + id);
            log.info("删除商品成功，ID: " + id);
        } catch (Exception e) {
            log.error("删除商品失败: " + e.getMessage());
            throw new RuntimeException("删除商品失败: " + e.getMessage());
        }
    }

    @Override
    public void batchDeleteProducts(List<Long> ids) {
        for (Long id : ids) {
            deleteProduct(id);
        }
    }

    @Override
    public void toggleProductStatus(Long id) {
        try {
            restTemplate.put(API_BASE_URL + "/fruit/admin/status/" + id, null);
            log.info("切换商品状态成功，ID: " + id);
        } catch (Exception e) {
            log.error("切换商品状态失败: " + e.getMessage());
            throw new RuntimeException("切换商品状态失败: " + e.getMessage());
        }
    }

    @Override
    public void batchToggleStatus(List<Long> ids) {
        for (Long id : ids) {
            toggleProductStatus(id);
        }
    }

    @Override
    public List<String> getCategories() {
        try {
            ResponseEntity<List<String>> response = restTemplate.exchange(
                API_BASE_URL + "/fruit/categories",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<String>>() {}
            );
            return response.getBody();
        } catch (Exception e) {
            // 返回模拟分类数据
            return Arrays.asList("热带水果", "温带水果", "亚热带水果", "进口水果", "有机水果");
        }
    }

    @Override
    public Map<String, Object> getProductStatistics() {
        try {
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                API_BASE_URL + "/fruit/statistics",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Map<String, Object>>() {}
            );
            
            Map<String, Object> result = response.getBody();
            if (result != null && result.get("code").equals(200)) {
                return (Map<String, Object>) result.get("data");
            } else {
                throw new RuntimeException("API调用失败");
            }
        } catch (Exception e) {
            throw new RuntimeException("获取商品统计失败: " + e.getMessage());
        }
    }

    @Override
    public void setRecommended(Long id, Boolean recommended) {
        try {
            Map<String, Object> request = new HashMap<>();
            request.put("recommended", recommended);
            restTemplate.put(API_BASE_URL + "/fruit/admin/recommend/" + id, request);
            log.info("设置商品推荐状态成功，ID: " + id + ", 推荐状态: " + recommended);
        } catch (Exception e) {
            log.error("设置商品推荐状态失败: " + e.getMessage());
            throw new RuntimeException("设置商品推荐状态失败: " + e.getMessage());
        }
    }

    @Override
    public void batchSetRecommended(List<Long> ids, Boolean recommended) {
        try {
            Map<String, Object> request = new HashMap<>();
            request.put("ids", ids);
            request.put("recommended", recommended);
            restTemplate.put(API_BASE_URL + "/fruit/admin/recommend/batch", request);
            log.info("批量设置商品推荐状态成功，商品数量: " + ids.size() + ", 推荐状态: " + recommended);
        } catch (Exception e) {
            log.error("批量设置商品推荐状态失败: " + e.getMessage());
            throw new RuntimeException("批量设置商品推荐状态失败: " + e.getMessage());
        }
    }

    @Override
    public void setFlashSale(Long id, Map<String, Object> flashSaleData) {
        try {
            restTemplate.postForObject(API_BASE_URL + "/fruit/admin/flash-sale/" + id, flashSaleData, String.class);
            log.info("设置商品限时特惠成功，ID: " + id);
        } catch (Exception e) {
            log.error("设置商品限时特惠失败: " + e.getMessage());
            throw new RuntimeException("设置商品限时特惠失败: " + e.getMessage());
        }
    }

    @Override
    public void cancelFlashSale(Long id) {
        try {
            restTemplate.delete(API_BASE_URL + "/fruit/admin/flash-sale/" + id);
            log.info("取消商品限时特惠成功，ID: " + id);
        } catch (Exception e) {
            log.error("取消商品限时特惠失败: " + e.getMessage());
            throw new RuntimeException("取消商品限时特惠失败: " + e.getMessage());
        }
    }

    /**
     * 获取模拟商品统计数据
     */
    private Map<String, Object> getMockProductStatistics() {
        Map<String, Object> statistics = new HashMap<>();
        statistics.put("totalProducts", 156);
        statistics.put("onlineProducts", 142);
        statistics.put("offlineProducts", 14);
        statistics.put("lowStockProducts", 8);
        statistics.put("outOfStockProducts", 3);
        statistics.put("todayAddedProducts", 5);
        return statistics;
    }

    /**
     * 获取模拟商品列表数据
     */
    private Map<String, Object> getMockProductList(int page, int size, String search, String category, String status) {
        List<Map<String, Object>> allProducts = new ArrayList<>();
        
        // 创建包含上架和下架商品的模拟数据（生成更多数据用于过滤）
        for (int i = 1; i <= 100; i++) {
            Map<String, Object> product = new HashMap<>();
            product.put("id", (long) i);
            product.put("name", "商品" + i);
            product.put("category", i % 4 == 0 ? "进口水果" : "热带水果");
            product.put("unit", "500g");
            product.put("origin", "产地" + i);
            product.put("price", 10.0 + i);
            product.put("stock", 100 + i);
            // 修复状态格式：使用数字格式（1表示上架，0表示下架）
            product.put("status", i % 3 == 0 ? 0 : 1); // 每3个商品中有1个下架
            product.put("imageUrl", "https://via.placeholder.com/100");
            allProducts.add(product);
        }
        
        // 应用过滤条件
        List<Map<String, Object>> filteredProducts = allProducts.stream()
            .filter(product -> {
                // 搜索过滤
                if (search != null && !search.trim().isEmpty()) {
                    String productName = (String) product.get("name");
                    if (!productName.contains(search)) {
                        return false;
                    }
                }
                
                // 分类过滤
                if (category != null && !category.trim().isEmpty()) {
                    String productCategory = (String) product.get("category");
                    if (!category.equals(productCategory)) {
                        return false;
                    }
                }
                
                // 状态过滤
                if (status != null && !status.trim().isEmpty()) {
                    Integer productStatus = (Integer) product.get("status");
                    Integer filterStatus = Integer.parseInt(status);
                    if (!productStatus.equals(filterStatus)) {
                        return false;
                    }
                }
                
                return true;
            })
            .collect(ArrayList::new, (list, item) -> list.add(item), ArrayList::addAll);
        
        // 分页处理
        int total = filteredProducts.size();
        int startIndex = (page - 1) * size;
        int endIndex = Math.min(startIndex + size, total);
        
        List<Map<String, Object>> pagedProducts = new ArrayList<>();
        if (startIndex < total) {
            pagedProducts = filteredProducts.subList(startIndex, endIndex);
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("list", pagedProducts);
        result.put("total", (long) total);
        result.put("page", page);
        result.put("size", size);
        
        return result;
    }

    /**
     * 获取模拟商品数据
     */
    private Fruit getMockFruit(Long id) {
        Fruit fruit = new Fruit();
        fruit.setId(id);
        fruit.setName("商品" + id);
        fruit.setCategory("热带水果");
        fruit.setUnit("500g/份");
        fruit.setOrigin("海南");
        fruit.setPrice(BigDecimal.valueOf(10.0 + id));
        fruit.setStock(100);
        fruit.setDescription("这是商品" + id + "的描述");
        fruit.setImageUrl("https://via.placeholder.com/300");
        return fruit;
    }
}
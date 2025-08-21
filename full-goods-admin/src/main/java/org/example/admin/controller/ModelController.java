package org.example.admin.controller;

import org.example.admin.service.AdminModelService;
import org.example.common.response.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * AI模型管理控制器
 * 提供模型的CRUD操作、训练数据管理、推荐策略配置等功能
 */
@RestController
@RequestMapping("/admin/model")
@CrossOrigin(origins = "*")
public class ModelController {

    @Autowired
    private AdminModelService adminModelService;

    @GetMapping("/list")
    public Result<Map<String, Object>> getModelList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String status) {
        return adminModelService.getModelList(page, size, search, status);
    }

    @GetMapping("/{id}")
    public Result<Map<String, Object>> getModelDetail(@PathVariable Long id) {
        return adminModelService.getModelDetail(id);
    }

    @PostMapping
    public Result<Boolean> createModel(@RequestBody Map<String, Object> request) {
        return adminModelService.createModel(request);
    }

    @PostMapping("/upload")
    public Result<Boolean> uploadModel(
            @RequestParam("file") MultipartFile file,
            @RequestParam("name") String name,
            @RequestParam("version") String version,
            @RequestParam("description") String description) {
        return adminModelService.uploadModel(file, name, version, description);
    }

    @PostMapping("/{id}/test")
    public Result<Map<String, Object>> testModel(@PathVariable Long id) {
        return adminModelService.testModel(id);
    }

    @PutMapping("/{id}/status")
    public Result<Boolean> updateModelStatus(@PathVariable Long id, @RequestBody Map<String, Object> request) {
        String status = (String) request.get("status");
        if ("enabled".equals(status)) {
            return adminModelService.activateModel(id);
        } else {
            return adminModelService.deactivateModel(id);
        }
    }

    @PutMapping("/{id}")
    public Result<Boolean> updateModel(@PathVariable Long id, @RequestBody Map<String, Object> request) {
        return adminModelService.updateModel(id, request);
    }

    @PutMapping("/activate/{id}")
    public Result<Boolean> activateModel(@PathVariable Long id) {
        return adminModelService.activateModel(id);
    }

    @PutMapping("/deactivate/{id}")
    public Result<Boolean> deactivateModel(@PathVariable Long id) {
        return adminModelService.deactivateModel(id);
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> deleteModel(@PathVariable Long id) {
        return adminModelService.deleteModel(id);
    }

    @PostMapping("/training-data")
    public Result<Boolean> uploadTrainingData(
            @RequestParam("file") MultipartFile file,
            @RequestParam("modelId") Long modelId,
            @RequestParam("name") String name) {
        return adminModelService.uploadTrainingData(file, modelId, name);
    }

    @PostMapping("/strategy")
    public Result<Boolean> createStrategy(@RequestBody Map<String, Object> request) {
        return adminModelService.createStrategy(request);
    }

    @GetMapping("/statistics")
    public Result<Map<String, Object>> getModelStatistics() {
        return adminModelService.getModelStatistics();
    }
}
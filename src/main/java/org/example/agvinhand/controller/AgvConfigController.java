package org.example.agvinhand.controller;

import org.example.agvinhand.common.AjaxResult;
import org.example.agvinhand.entity.AgvConfig;
import org.example.agvinhand.service.AgvConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/agvConfig")
public class AgvConfigController {
    @Autowired
    private AgvConfigService agvConfigService;

    @GetMapping("/{id}")
    public AjaxResult<AgvConfig> getById(@PathVariable Long id) {
        return AjaxResult.success(agvConfigService.getById(id));
    }

    @GetMapping("/list")
    public AjaxResult<List<AgvConfig>> listAll() {
        return AjaxResult.success(agvConfigService.listAll());
    }

    @PostMapping
    public AjaxResult<Boolean> save(@RequestBody AgvConfig config) {
        return agvConfigService.save(config) ? AjaxResult.success(true) : AjaxResult.error("新增失败");
    }

    @PutMapping
    public AjaxResult<Boolean> update(@RequestBody AgvConfig config) {
        return agvConfigService.updateById(config) ? AjaxResult.success(true) : AjaxResult.error("更新失败");
    }

    @DeleteMapping("/{id}")
    public AjaxResult<Boolean> delete(@PathVariable Long id) {
        return agvConfigService.removeById(id) ? AjaxResult.success(true) : AjaxResult.error("删除失败");
    }
}
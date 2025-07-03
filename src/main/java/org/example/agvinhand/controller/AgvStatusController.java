package org.example.agvinhand.controller;

import org.example.agvinhand.common.AjaxResult;
import org.example.agvinhand.entity.AgvStatus;
import org.example.agvinhand.service.AgvStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/agvStatus")
public class AgvStatusController {
    @Autowired
    private AgvStatusService agvStatusService;

    @GetMapping("/{id}")
    public AjaxResult<AgvStatus> getById(@PathVariable Long id) {
        return AjaxResult.success(agvStatusService.getById(id));
    }

    @GetMapping("/list")
    public AjaxResult<List<AgvStatus>> listAll() {
        return AjaxResult.success(agvStatusService.listAll());
    }

    @PostMapping
    public AjaxResult<Boolean> save(@RequestBody AgvStatus status) {
        return agvStatusService.save(status) ? AjaxResult.success(true) : AjaxResult.error("新增失败");
    }

    @PutMapping
    public AjaxResult<Boolean> update(@RequestBody AgvStatus status) {
        return agvStatusService.updateById(status) ? AjaxResult.success(true) : AjaxResult.error("更新失败");
    }

    @DeleteMapping("/{id}")
    public AjaxResult<Boolean> delete(@PathVariable Long id) {
        return agvStatusService.removeById(id) ? AjaxResult.success(true) : AjaxResult.error("删除失败");
    }
}
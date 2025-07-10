package org.example.agvinhand.controller;

import org.example.agvinhand.common.AjaxResult;
import org.example.agvinhand.entity.AgvTask;
import org.example.agvinhand.service.AgvTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/agvTask")
@CrossOrigin(origins = "*")
public class AgvTaskController {
    @Autowired
    private AgvTaskService agvTaskService;

    @GetMapping("/{id}")
    public AjaxResult<AgvTask> getById(@PathVariable Long id) {
        return AjaxResult.success(agvTaskService.getById(id));
    }

    @GetMapping("/list")
    public AjaxResult<List<AgvTask>> listAll() {
        return AjaxResult.success(agvTaskService.listAll());
    }

    @PostMapping
    public AjaxResult<Boolean> save(@RequestBody AgvTask task) {
        return agvTaskService.save(task) ? AjaxResult.success(true) : AjaxResult.error("新增失败");
    }

    @PutMapping
    public AjaxResult<Boolean> update(@RequestBody AgvTask task) {
        return agvTaskService.updateById(task) ? AjaxResult.success(true) : AjaxResult.error("更新失败");
    }

    @DeleteMapping("/{id}")
    public AjaxResult<Boolean> delete(@PathVariable Long id) {
        return agvTaskService.removeById(id) ? AjaxResult.success(true) : AjaxResult.error("删除失败");
    }

    /**
     * 同步任务数据接口
     * 接收前端从远端后端获取的任务列表，进行对比和存储
     */
    @PostMapping("/sync")
    public AjaxResult<Boolean> syncTasks(@RequestBody List<AgvTask> remoteTasks) {
        try {
            boolean result = agvTaskService.syncTasks(remoteTasks);
            return result ? AjaxResult.success(true) : AjaxResult.error("同步失败");
        } catch (Exception e) {
            return AjaxResult.error("同步异常: " + e.getMessage());
        }
    }

    /**
     * 根据cloudTaskId查询任务
     */
    @GetMapping("/cloud/{cloudTaskId}")
    public AjaxResult<AgvTask> getByCloudTaskId(@PathVariable Long cloudTaskId) {
        return AjaxResult.success(agvTaskService.getByCloudTaskId(cloudTaskId));
    }
}
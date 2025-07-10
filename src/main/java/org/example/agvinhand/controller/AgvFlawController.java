package org.example.agvinhand.controller;

import org.example.agvinhand.common.AjaxResult;
import org.example.agvinhand.entity.AgvFlaw;
import org.example.agvinhand.service.AgvFlawService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/agvFlaw")
@CrossOrigin(origins = "*")
public class AgvFlawController {
    @Autowired
    private AgvFlawService agvFlawService;

    @GetMapping("/{id}")
    public AjaxResult<AgvFlaw> getById(@PathVariable Long id) {
        return AjaxResult.success(agvFlawService.getById(id));
    }

    @GetMapping("/list")
    public AjaxResult<List<AgvFlaw>> listAll() {
        return AjaxResult.success(agvFlawService.listAll());
    }

    @PostMapping
    public AjaxResult<Boolean> save(@RequestBody AgvFlaw flaw) {
        return agvFlawService.save(flaw) ? AjaxResult.success(true) : AjaxResult.error("新增失败");
    }

    @PutMapping
    public AjaxResult<Boolean> update(@RequestBody AgvFlaw flaw) {
        return agvFlawService.updateById(flaw) ? AjaxResult.success(true) : AjaxResult.error("更新失败");
    }

    @DeleteMapping("/{id}")
    public AjaxResult<Boolean> delete(@PathVariable Long id) {
        return agvFlawService.removeById(id) ? AjaxResult.success(true) : AjaxResult.error("删除失败");
    }

    /**
     * 同步缺陷数据接口
     * 接收前端从远端后端获取的缺陷列表，进行对比和存储
     */
    @PostMapping("/sync")
    public AjaxResult<Boolean> syncFlaws(@RequestBody List<AgvFlaw> remoteFlaws) {
        try {
            boolean result = agvFlawService.syncFlaws(remoteFlaws);
            return result ? AjaxResult.success(true) : AjaxResult.error("同步失败");
        } catch (Exception e) {
            return AjaxResult.error("同步异常: " + e.getMessage());
        }
    }

    /**
     * 根据任务ID查询缺陷列表
     */
    @GetMapping("/task/{taskId}")
    public AjaxResult<List<AgvFlaw>> getByTaskId(@PathVariable Long taskId) {
        return AjaxResult.success(agvFlawService.getByTaskId(taskId));
    }
}
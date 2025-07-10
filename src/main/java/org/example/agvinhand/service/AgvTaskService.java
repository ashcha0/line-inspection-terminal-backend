package org.example.agvinhand.service;

import org.example.agvinhand.entity.AgvTask;
import java.util.List;

public interface AgvTaskService {
    AgvTask getById(Long id);

    List<AgvTask> listAll();

    boolean save(AgvTask task);

    boolean updateById(AgvTask task);

    boolean removeById(Long id);

    /**
     * 同步任务数据
     * 对比远端任务列表和本地数据，新增或更新任务
     */
    boolean syncTasks(List<AgvTask> remoteTasks);

    /**
     * 根据cloudTaskId查询任务
     */
    AgvTask getByCloudTaskId(Long cloudTaskId);
}
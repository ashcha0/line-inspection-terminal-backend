package org.example.agvinhand.service.impl;

import org.example.agvinhand.entity.AgvTask;
import org.example.agvinhand.mapper.AgvTaskMapper;
import org.example.agvinhand.service.AgvTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AgvTaskServiceImpl implements AgvTaskService {
    @Autowired
    private AgvTaskMapper agvTaskMapper;

    @Override
    public AgvTask getById(Long id) {
        return agvTaskMapper.selectById(id);
    }

    @Override
    public List<AgvTask> listAll() {
        return agvTaskMapper.selectList(null);
    }

    @Override
    public boolean save(AgvTask task) {
        return agvTaskMapper.insert(task) > 0;
    }

    @Override
    public boolean updateById(AgvTask task) {
        return agvTaskMapper.updateById(task) > 0;
    }

    @Override
    public boolean removeById(Long id) {
        return agvTaskMapper.deleteById(id) > 0;
    }

    @Override
    public boolean syncTasks(List<AgvTask> remoteTasks) {
        try {
            // 获取本地所有任务
            List<AgvTask> localTasks = listAll();

            // 将本地任务按cloudTaskId分组，方便查找
            Map<Long, AgvTask> localTaskMap = localTasks.stream()
                    .filter(task -> task.getCloudTaskId() != null)
                    .collect(Collectors.toMap(AgvTask::getCloudTaskId, task -> task));

            // 遍历远端任务列表
            for (AgvTask remoteTask : remoteTasks) {
                if (remoteTask.getCloudTaskId() == null) {
                    continue; // 跳过没有cloudTaskId的任务
                }

                AgvTask localTask = localTaskMap.get(remoteTask.getCloudTaskId());

                if (localTask == null) {
                    // 本地没有该任务，新增
                    remoteTask.setId(null); // 确保使用自增ID
                    save(remoteTask);
                } else {
                    // 本地已有该任务，检查是否需要更新
                    if (needUpdate(localTask, remoteTask)) {
                        remoteTask.setId(localTask.getId()); // 保持本地ID
                        updateById(remoteTask);
                    }
                }
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public AgvTask getByCloudTaskId(Long cloudTaskId) {
        return agvTaskMapper.selectByCloudTaskId(cloudTaskId);
    }

    /**
     * 判断是否需要更新任务
     * 比较关键字段，如果不同则需要更新
     */
    private boolean needUpdate(AgvTask localTask, AgvTask remoteTask) {
        // 比较关键字段
        return !localTask.getTaskStatus().equals(remoteTask.getTaskStatus()) ||
                !localTask.getTaskName().equals(remoteTask.getTaskName()) ||
                !localTask.getTaskCode().equals(remoteTask.getTaskCode()) ||
                !equals(localTask.getExecTime(), remoteTask.getExecTime()) ||
                !equals(localTask.getEndTime(), remoteTask.getEndTime()) ||
                !equals(localTask.getRound(), remoteTask.getRound()) ||
                !equals(localTask.getUploaded(), remoteTask.getUploaded());
    }

    /**
     * 安全的对象比较方法
     */
    private boolean equals(Object obj1, Object obj2) {
        if (obj1 == null && obj2 == null)
            return true;
        if (obj1 == null || obj2 == null)
            return false;
        return obj1.equals(obj2);
    }
}
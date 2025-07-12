package org.example.agvinhand.service.impl;

import org.example.agvinhand.entity.AgvTask;
import org.example.agvinhand.mapper.AgvTaskMapper;
import org.example.agvinhand.service.AgvTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class AgvTaskServiceImpl implements AgvTaskService {
    private static final Logger logger = Logger.getLogger(AgvTaskServiceImpl.class.getName());

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
        try {
            return agvTaskMapper.updateById(task) > 0;
        } catch (Exception e) {
            logger.severe("更新任务失败: " + e.getMessage() + ", taskId=" + task.getId());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean removeById(Long id) {
        return agvTaskMapper.deleteById(id) > 0;
    }

    @Override
    public boolean syncTasks(List<AgvTask> remoteTasks) {
        try {
            if (remoteTasks == null || remoteTasks.isEmpty()) {
                logger.info("没有任务数据需要同步");
                return true;
            }

            // 去除重复任务
            List<AgvTask> uniqueRemoteTasks = removeDuplicates(remoteTasks);
            logger.info("原始任务数量: " + remoteTasks.size() + ", 去重后数量: " + uniqueRemoteTasks.size());

            // 获取本地所有任务
            List<AgvTask> localTasks = listAll();
            logger.info("本地任务数量: " + localTasks.size());

            // 创建本地任务映射
            Map<Long, AgvTask> localTaskMap = new HashMap<>();
            for (AgvTask task : localTasks) {
                if (task.getCloudTaskId() != null) {
                    localTaskMap.put(task.getCloudTaskId(), task);
                }
            }

            int addCount = 0;
            int updateCount = 0;

            // 遍历远端任务列表
            for (AgvTask remoteTask : uniqueRemoteTasks) {
                if (remoteTask.getCloudTaskId() == null) {
                    logger.warning("跳过没有cloudTaskId的任务: taskCode=" + remoteTask.getTaskCode());
                    continue; // 跳过没有cloudTaskId的任务
                }

                try {
                    AgvTask localTask = localTaskMap.get(remoteTask.getCloudTaskId());

                    if (localTask == null) {
                        // 本地没有该任务，新增
                        remoteTask.setId(null); // 确保使用自增ID
                        save(remoteTask);
                        addCount++;
                    } else {
                        // 本地已有该任务，检查是否需要更新
                        if (needUpdate(localTask, remoteTask)) {
                            // 使用本地ID
                            Long localId = localTask.getId();
                            remoteTask.setId(localId);

                            // 直接使用XML中定义的更新语句而不是MyBatis的自动映射
                            if (updateById(remoteTask)) {
                                updateCount++;
                            }
                        }
                    }
                } catch (Exception e) {
                    // 记录异常但不中断同步
                    logger.warning("处理任务数据时出错: " + e.getMessage() + ", taskId=" + remoteTask.getCloudTaskId());
                    e.printStackTrace();
                }
            }

            logger.info("任务同步完成: 新增=" + addCount + ", 更新=" + updateCount);
            return true;
        } catch (Exception e) {
            logger.severe("任务同步发生异常: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 去除集合中的重复项
     */
    private List<AgvTask> removeDuplicates(List<AgvTask> tasks) {
        Map<Long, AgvTask> uniqueTasks = new HashMap<>();

        for (AgvTask task : tasks) {
            if (task.getCloudTaskId() != null) {
                // 如果有重复，保留最后一个
                uniqueTasks.put(task.getCloudTaskId(), task);
            }
        }

        return new ArrayList<>(uniqueTasks.values());
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
        try {
            // 比较关键字段
            boolean needUpdate = false;

            if (localTask.getTaskStatus() != null && remoteTask.getTaskStatus() != null) {
                needUpdate = needUpdate || !localTask.getTaskStatus().equals(remoteTask.getTaskStatus());
            }

            if (localTask.getTaskName() != null && remoteTask.getTaskName() != null) {
                needUpdate = needUpdate || !localTask.getTaskName().equals(remoteTask.getTaskName());
            }

            if (localTask.getTaskCode() != null && remoteTask.getTaskCode() != null) {
                needUpdate = needUpdate || !localTask.getTaskCode().equals(remoteTask.getTaskCode());
            }

            needUpdate = needUpdate ||
                    !equals(localTask.getExecTime(), remoteTask.getExecTime()) ||
                    !equals(localTask.getEndTime(), remoteTask.getEndTime()) ||
                    !equals(localTask.getRound(), remoteTask.getRound()) ||
                    !equals(localTask.getUploaded(), remoteTask.getUploaded());

            return needUpdate;
        } catch (Exception e) {
            logger.warning("比较任务更新时出错: " + e.getMessage());
            return false;
        }
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
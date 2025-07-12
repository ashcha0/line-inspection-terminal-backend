package org.example.agvinhand.service.impl;

import org.example.agvinhand.entity.AgvFlaw;
import org.example.agvinhand.mapper.AgvFlawMapper;
import org.example.agvinhand.service.AgvFlawService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class AgvFlawServiceImpl implements AgvFlawService {
    private static final Logger logger = Logger.getLogger(AgvFlawServiceImpl.class.getName());

    @Autowired
    private AgvFlawMapper agvFlawMapper;

    @Override
    public AgvFlaw getById(Long id) {
        return agvFlawMapper.selectById(id);
    }

    @Override
    public List<AgvFlaw> listAll() {
        return agvFlawMapper.selectList(null);
    }

    @Override
    public boolean save(AgvFlaw flaw) {
        return agvFlawMapper.insert(flaw) > 0;
    }

    @Override
    public boolean updateById(AgvFlaw flaw) {
        return agvFlawMapper.updateById(flaw) > 0;
    }

    @Override
    public boolean removeById(Long id) {
        return agvFlawMapper.deleteById(id) > 0;
    }

    @Override
    public boolean syncFlaws(List<AgvFlaw> remoteFlaws) {
        try {
            if (remoteFlaws == null || remoteFlaws.isEmpty()) {
                logger.info("没有缺陷数据需要同步");
                return true;
            }

            // 去除远端传来的重复数据
            List<AgvFlaw> uniqueRemoteFlaws = removeDuplicates(remoteFlaws);
            logger.info("原始缺陷数量: " + remoteFlaws.size() + ", 去重后数量: " + uniqueRemoteFlaws.size());

            // 获取本地所有缺陷
            List<AgvFlaw> localFlaws = listAll();
            logger.info("本地缺陷数量: " + localFlaws.size());

            // 创建本地缺陷映射，采用更可靠的唯一键
            Map<String, AgvFlaw> localFlawMap = new HashMap<>();
            for (AgvFlaw flaw : localFlaws) {
                if (flaw.getTaskId() != null && flaw.getFlawName() != null) {
                    // 使用更多字段组合成唯一键，减少冲突可能
                    String key = generateFlawKey(flaw);
                    localFlawMap.put(key, flaw);
                }
            }

            int addCount = 0;
            int updateCount = 0;

            // 遍历远端缺陷列表
            for (AgvFlaw remoteFlaw : uniqueRemoteFlaws) {
                if (remoteFlaw.getTaskId() == null || remoteFlaw.getFlawName() == null) {
                    logger.warning(
                            "跳过无效缺陷数据: taskId=" + remoteFlaw.getTaskId() + ", flawName=" + remoteFlaw.getFlawName());
                    continue; // 跳过无效数据
                }

                String key = generateFlawKey(remoteFlaw);
                AgvFlaw localFlaw = localFlawMap.get(key);

                try {
                    if (localFlaw == null) {
                        // 本地没有该缺陷，新增
                        remoteFlaw.setId(null); // 确保使用自增ID
                        save(remoteFlaw);
                        addCount++;
                    } else {
                        // 本地已有该缺陷，检查是否需要更新
                        if (needUpdate(localFlaw, remoteFlaw)) {
                            remoteFlaw.setId(localFlaw.getId()); // 保持本地ID
                            updateById(remoteFlaw);
                            updateCount++;
                        }
                    }
                } catch (Exception e) {
                    // 记录异常但不中断整个同步过程
                    logger.warning("处理缺陷数据时出错: " + e.getMessage() + ", 缺陷key=" + key);
                }
            }

            logger.info("缺陷同步完成: 新增=" + addCount + ", 更新=" + updateCount);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            logger.severe("缺陷同步发生异常: " + e.getMessage());
            return false;
        }
    }

    /**
     * 去除集合中的重复项
     */
    private List<AgvFlaw> removeDuplicates(List<AgvFlaw> flaws) {
        Map<String, AgvFlaw> uniqueFlaws = new HashMap<>();

        for (AgvFlaw flaw : flaws) {
            if (flaw.getTaskId() != null && flaw.getFlawName() != null) {
                String key = generateFlawKey(flaw);
                // 如果有重复，保留最后一个
                uniqueFlaws.put(key, flaw);
            }
        }

        return new ArrayList<>(uniqueFlaws.values());
    }

    /**
     * 生成缺陷的唯一键
     */
    private String generateFlawKey(AgvFlaw flaw) {
        return flaw.getTaskId() + "_" + flaw.getFlawName() + "_" +
                (flaw.getFlawDistance() != null ? flaw.getFlawDistance() : "0") + "_" +
                (flaw.getRound() != null ? flaw.getRound() : "1");
    }

    @Override
    public List<AgvFlaw> getByTaskId(Long taskId) {
        return agvFlawMapper.selectByTaskId(taskId);
    }

    /**
     * 判断是否需要更新缺陷
     * 比较关键字段，如果不同则需要更新
     */
    private boolean needUpdate(AgvFlaw localFlaw, AgvFlaw remoteFlaw) {
        // 比较关键字段
        return !equals(localFlaw.getFlawDesc(), remoteFlaw.getFlawDesc()) ||
                !equals(localFlaw.getFlawDistance(), remoteFlaw.getFlawDistance()) ||
                !equals(localFlaw.getFlawImage(), remoteFlaw.getFlawImage()) ||
                !equals(localFlaw.getFlawImageUrl(), remoteFlaw.getFlawImageUrl()) ||
                !equals(localFlaw.getFlawRtsp(), remoteFlaw.getFlawRtsp()) ||
                !equals(localFlaw.getShown(), remoteFlaw.getShown()) ||
                !equals(localFlaw.getConfirmed(), remoteFlaw.getConfirmed()) ||
                !equals(localFlaw.getUploaded(), remoteFlaw.getUploaded()) ||
                !equals(localFlaw.getFlawLength(), remoteFlaw.getFlawLength()) ||
                !equals(localFlaw.getFlawArea(), remoteFlaw.getFlawArea()) ||
                !equals(localFlaw.getLevel(), remoteFlaw.getLevel()) ||
                !equals(localFlaw.getCountNum(), remoteFlaw.getCountNum());
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
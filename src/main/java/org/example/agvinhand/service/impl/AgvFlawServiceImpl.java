package org.example.agvinhand.service.impl;

import org.example.agvinhand.entity.AgvFlaw;
import org.example.agvinhand.mapper.AgvFlawMapper;
import org.example.agvinhand.service.AgvFlawService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AgvFlawServiceImpl implements AgvFlawService {
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
            // 获取本地所有缺陷
            List<AgvFlaw> localFlaws = listAll();

            // 将本地缺陷按taskId和flawName分组，方便查找
            Map<String, AgvFlaw> localFlawMap = localFlaws.stream()
                    .filter(flaw -> flaw.getTaskId() != null && flaw.getFlawName() != null)
                    .collect(Collectors.toMap(
                            flaw -> flaw.getTaskId() + "_" + flaw.getFlawName(),
                            flaw -> flaw));

            // 遍历远端缺陷列表
            for (AgvFlaw remoteFlaw : remoteFlaws) {
                if (remoteFlaw.getTaskId() == null || remoteFlaw.getFlawName() == null) {
                    continue; // 跳过无效数据
                }

                String key = remoteFlaw.getTaskId() + "_" + remoteFlaw.getFlawName();
                AgvFlaw localFlaw = localFlawMap.get(key);

                if (localFlaw == null) {
                    // 本地没有该缺陷，新增
                    remoteFlaw.setId(null); // 确保使用自增ID
                    save(remoteFlaw);
                } else {
                    // 本地已有该缺陷，检查是否需要更新
                    if (needUpdate(localFlaw, remoteFlaw)) {
                        remoteFlaw.setId(localFlaw.getId()); // 保持本地ID
                        updateById(remoteFlaw);
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
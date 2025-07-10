package org.example.agvinhand.service;

import org.example.agvinhand.entity.AgvFlaw;
import java.util.List;

public interface AgvFlawService {
    AgvFlaw getById(Long id);

    List<AgvFlaw> listAll();

    boolean save(AgvFlaw flaw);

    boolean updateById(AgvFlaw flaw);

    boolean removeById(Long id);

    /**
     * 同步缺陷数据
     * 对比远端缺陷列表和本地数据，新增或更新缺陷
     */
    boolean syncFlaws(List<AgvFlaw> remoteFlaws);

    /**
     * 根据任务ID查询缺陷列表
     */
    List<AgvFlaw> getByTaskId(Long taskId);
}
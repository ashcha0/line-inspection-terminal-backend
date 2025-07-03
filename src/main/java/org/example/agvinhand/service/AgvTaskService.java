package org.example.agvinhand.service;

import org.example.agvinhand.entity.AgvTask;
import java.util.List;

public interface AgvTaskService {
    AgvTask getById(Long id);

    List<AgvTask> listAll();

    boolean save(AgvTask task);

    boolean updateById(AgvTask task);

    boolean removeById(Long id);
}
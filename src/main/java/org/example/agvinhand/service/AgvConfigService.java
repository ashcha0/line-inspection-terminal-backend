package org.example.agvinhand.service;

import org.example.agvinhand.entity.AgvConfig;
import java.util.List;

public interface AgvConfigService {
    AgvConfig getById(Long id);

    List<AgvConfig> listAll();

    boolean save(AgvConfig config);

    boolean updateById(AgvConfig config);

    boolean removeById(Long id);
}
package org.example.agvinhand.service.impl;

import org.example.agvinhand.entity.AgvConfig;
import org.example.agvinhand.mapper.AgvConfigMapper;
import org.example.agvinhand.service.AgvConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgvConfigServiceImpl implements AgvConfigService {
    @Autowired
    private AgvConfigMapper agvConfigMapper;

    @Override
    public AgvConfig getById(Long id) {
        return agvConfigMapper.selectById(id);
    }

    @Override
    public List<AgvConfig> listAll() {
        return agvConfigMapper.selectList(null);
    }

    @Override
    public boolean save(AgvConfig config) {
        return agvConfigMapper.insert(config) > 0;
    }

    @Override
    public boolean updateById(AgvConfig config) {
        return agvConfigMapper.updateById(config) > 0;
    }

    @Override
    public boolean removeById(Long id) {
        return agvConfigMapper.deleteById(id) > 0;
    }
}
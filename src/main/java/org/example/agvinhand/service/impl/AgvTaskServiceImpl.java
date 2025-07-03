package org.example.agvinhand.service.impl;

import org.example.agvinhand.entity.AgvTask;
import org.example.agvinhand.mapper.AgvTaskMapper;
import org.example.agvinhand.service.AgvTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
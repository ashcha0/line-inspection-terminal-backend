package org.example.agvinhand.service.impl;

import org.example.agvinhand.entity.AgvStatus;
import org.example.agvinhand.mapper.AgvStatusMapper;
import org.example.agvinhand.service.AgvStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgvStatusServiceImpl implements AgvStatusService {
    @Autowired
    private AgvStatusMapper agvStatusMapper;

    @Override
    public AgvStatus getById(Long id) {
        return agvStatusMapper.selectById(id);
    }

    @Override
    public List<AgvStatus> listAll() {
        return agvStatusMapper.selectList(null);
    }

    @Override
    public boolean save(AgvStatus status) {
        return agvStatusMapper.insert(status) > 0;
    }

    @Override
    public boolean updateById(AgvStatus status) {
        return agvStatusMapper.updateById(status) > 0;
    }

    @Override
    public boolean removeById(Long id) {
        return agvStatusMapper.deleteById(id) > 0;
    }
}
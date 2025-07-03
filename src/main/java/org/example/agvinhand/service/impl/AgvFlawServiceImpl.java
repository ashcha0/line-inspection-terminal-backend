package org.example.agvinhand.service.impl;

import org.example.agvinhand.entity.AgvFlaw;
import org.example.agvinhand.mapper.AgvFlawMapper;
import org.example.agvinhand.service.AgvFlawService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
package org.example.agvinhand.service;

import org.example.agvinhand.entity.AgvStatus;
import java.util.List;

public interface AgvStatusService {
    AgvStatus getById(Long id);

    List<AgvStatus> listAll();

    boolean save(AgvStatus status);

    boolean updateById(AgvStatus status);

    boolean removeById(Long id);
}
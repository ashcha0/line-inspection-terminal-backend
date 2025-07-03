package org.example.agvinhand.service;

import org.example.agvinhand.entity.AgvFlaw;
import java.util.List;

public interface AgvFlawService {
    AgvFlaw getById(Long id);

    List<AgvFlaw> listAll();

    boolean save(AgvFlaw flaw);

    boolean updateById(AgvFlaw flaw);

    boolean removeById(Long id);
}
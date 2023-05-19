package com.mstore.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.mstore.entity.ModelsEntity;
import com.mstore.repository.ModelsRepository;

@Service
public class ModelsService {

  @Autowired
  ModelsRepository repo;

  public List<ModelsEntity> fetchModel(String name) {
    return this.repo.getModelByName(name);
  }
}

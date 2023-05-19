package com.mstore.repository;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import com.mstore.entity.ModelsEntity;

@Repository
public interface ModelsRepository extends MongoRepository<ModelsEntity, String> {

  @Query("{name : ?0}")
  List<ModelsEntity> getModelByName(String name);
}

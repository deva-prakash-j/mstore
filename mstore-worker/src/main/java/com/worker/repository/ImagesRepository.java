package com.worker.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import com.worker.entity.ImagesEntity;

@Repository
public interface ImagesRepository extends MongoRepository<ImagesEntity, String> {

  public ImagesEntity findByUrl(String url);
}

package com.worker.service;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.worker.entity.ImagesEntity;
import com.worker.repository.ImagesRepository;

@Service
public class ImageService {

  @Autowired
  private ImagesRepository imageRepository;

  public void saveImage(ImagesEntity entity) {
    imageRepository.save(entity);
  }

  public Optional<ImagesEntity> fintById(String id) {
    return imageRepository.findById(id);
  }

  public ImagesEntity findByURL(String url) {
    return imageRepository.findByUrl(url);
  }
}

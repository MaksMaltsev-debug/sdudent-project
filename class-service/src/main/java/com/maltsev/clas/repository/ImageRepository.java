package com.maltsev.clas.repository;

import com.maltsev.clas.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image, Long> {
    Optional<Image> findByUserId(String id);

    void deleteByUserId(String userId);
}

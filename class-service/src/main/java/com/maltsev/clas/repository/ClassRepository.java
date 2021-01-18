package com.maltsev.clas.repository;

import com.maltsev.clas.entity.ClassEntity;
import com.maltsev.clas.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClassRepository extends JpaRepository<ClassEntity, String> {

     List<ClassEntity> findAllByCreatorId(String userId);

}

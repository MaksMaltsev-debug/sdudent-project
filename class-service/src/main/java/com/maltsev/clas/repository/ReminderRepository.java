package com.maltsev.clas.repository;

import com.maltsev.clas.entity.ReminderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReminderRepository extends JpaRepository<ReminderEntity, String> {
    List<ReminderEntity> findAllByUserId(String userId);
}

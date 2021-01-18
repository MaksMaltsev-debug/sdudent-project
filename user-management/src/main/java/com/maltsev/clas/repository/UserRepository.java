package com.maltsev.clas.repository;

import com.maltsev.clas.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, String> {
    Optional<UserEntity> findById(String id);

    void deleteById(String id);

    Optional<UserEntity> findByLogin(String login);
}


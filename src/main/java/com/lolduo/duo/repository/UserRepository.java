package com.lolduo.duo.repository;

import com.lolduo.duo.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<UserEntity, String> {
    List<UserEntity> findAllByTier(String tier);
    List<UserEntity> findAllByTierAndRank(String tier, String rank);
}

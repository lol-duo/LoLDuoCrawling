package com.lolduo.duo.repository;

import com.lolduo.duo.entity.LoLUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoLUserRepository extends JpaRepository<LoLUserEntity, String> {
}

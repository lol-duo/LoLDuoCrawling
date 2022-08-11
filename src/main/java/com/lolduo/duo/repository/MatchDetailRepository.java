package com.lolduo.duo.repository;

import com.lolduo.duo.entity.MatchEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface MatchDetailRepository extends JpaRepository<MatchEntity, Long> {
    List<MatchEntity> findAllByDate(LocalDate localDate);
}

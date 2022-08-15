package com.lolduo.duo.repository.gameInfo;

import com.lolduo.duo.entity.gameInfo.DoubleMatchEntity;
import com.lolduo.duo.entity.gameInfo.SoloMatchEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface DoubleMatchRepository extends JpaRepository<DoubleMatchEntity, Long>,IMatchRepository {
    List<DoubleMatchEntity> findAllByDate(LocalDate localDate);
}
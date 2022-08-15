package com.lolduo.duo.repository.gameInfo;

import com.lolduo.duo.entity.MatchEntity;
import com.lolduo.duo.entity.gameInfo.SoloMatchEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface SoloMatchRepository extends JpaRepository<SoloMatchEntity, Long> ,IMatchRepository{
    List<SoloMatchEntity> findAllByDate(LocalDate localDate);
}

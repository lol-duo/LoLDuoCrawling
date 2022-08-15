package com.lolduo.duo.repository.gameInfo;

import com.lolduo.duo.entity.gameInfo.TripleMatchEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface TripleMatchRepository extends JpaRepository<TripleMatchEntity,Long> ,IMatchRepository{
    List<TripleMatchEntity> findAllByDate(LocalDate localDate);
}

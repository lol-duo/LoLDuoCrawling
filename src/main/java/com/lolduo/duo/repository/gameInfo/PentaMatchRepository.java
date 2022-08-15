package com.lolduo.duo.repository.gameInfo;

import com.lolduo.duo.entity.gameInfo.PentaMatchEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface PentaMatchRepository extends JpaRepository<PentaMatchEntity,Long> ,IMatchRepository{
    List<PentaMatchEntity> findAllByDate(LocalDate localDate);

}

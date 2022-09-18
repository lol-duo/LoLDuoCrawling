package com.lolduo.duo.repository.InitialDataRepository;

import com.lolduo.duo.entity.InitialDataEntity.ChampionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChampionRepository extends JpaRepository<ChampionEntity,Long> {
}

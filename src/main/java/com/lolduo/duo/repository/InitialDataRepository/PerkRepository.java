package com.lolduo.duo.repository.InitialDataRepository;

import com.lolduo.duo.entity.InitialDataEntity.PerkEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PerkRepository extends JpaRepository<PerkEntity,Long> {
}

package com.lolduo.duo.repository.InitialDataRepository;

import com.lolduo.duo.entity.InitialDataEntity.SpellEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpellRepository extends JpaRepository<SpellEntity,Long> {
}

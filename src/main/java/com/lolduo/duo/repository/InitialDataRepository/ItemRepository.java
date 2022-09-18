package com.lolduo.duo.repository.InitialDataRepository;

import com.lolduo.duo.entity.InitialDataEntity.ItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<ItemEntity,Long> {
}

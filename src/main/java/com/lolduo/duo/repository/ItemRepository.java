package com.lolduo.duo.repository;

import com.lolduo.duo.entity.item.ItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<ItemEntity, Long> {
}

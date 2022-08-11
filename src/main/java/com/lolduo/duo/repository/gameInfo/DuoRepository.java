package com.lolduo.duo.repository.gameInfo;

import com.lolduo.duo.entity.gameInfo.DuoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface DuoRepository extends JpaRepository<DuoEntity, Long> {
    @Query(value = "select * from duo where json_contains(champion,?1) and  json_contains(position,?2)",nativeQuery = true)
    Optional<DuoEntity> findByChampionAndPosition(String championId, String position);
}

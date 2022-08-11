package com.lolduo.duo.repository.gameInfo;

import com.lolduo.duo.entity.gameInfo.TrioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TrioRepository extends JpaRepository<TrioEntity,Long> {
    @Query(value = "select * from trio where json_contains(champion,?1) and  json_contains(position,?2)",nativeQuery = true)
    Optional<TrioEntity> findByChampionAndPosition(String championId, String position);
}

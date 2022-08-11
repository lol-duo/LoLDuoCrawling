package com.lolduo.duo.repository.gameInfo;

import com.lolduo.duo.entity.gameInfo.QuintetEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface QuintetRepository extends JpaRepository<QuintetEntity,Long> {
    @Query(value = "select * from team where json_contains(champion,?1) and  json_contains(position,?2)",nativeQuery = true)
    Optional<QuintetEntity> findByChampionAndPosition(String championId, String position);
}

package com.lolduo.duo.repository.clientInfo;

import com.lolduo.duo.entity.clientInfo.DuoInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface DuoInfoRepository extends JpaRepository<DuoInfoEntity,Long>, ICombinationInfoRepository {

    @Query(value = "select * from duo_info where json_contains(champion_id,?1) and json_contains(position,?2) and json_contains(json_extract(position, '$.*'), ?3) and not json_has_exclude_position(position, ?1, ?4) order by win_count / all_count DESC limit 30",nativeQuery = true)
    Optional<List<DuoInfoEntity>> findAllByChampionIdAndPositionDesc(String championId, String position, String positionList, String excludePositionList);

    @Query(value = "select * from duo_info where json_contains(champion_id,?1) and json_contains(position,?2) and json_contains(json_extract(position, '$.*'), ?3) and not json_has_exclude_position(position, ?1, ?4) order by win_count / all_count ASC limit 30",nativeQuery = true)
    Optional<List<DuoInfoEntity>> findAllByChampionIdAndPositionAsc(String championId, String position, String positionList, String excludePositionList);

    @Query(value = "select * from duo_info where json_contains(champion_id,?1) and json_contains(position,?2)",nativeQuery = true)
    Optional<DuoInfoEntity> findByChampionIdAndPosition(String championId, String position);
}

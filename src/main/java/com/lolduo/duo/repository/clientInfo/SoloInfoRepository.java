package com.lolduo.duo.repository.clientInfo;

import com.lolduo.duo.entity.clientInfo.SoloInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SoloInfoRepository extends JpaRepository<SoloInfoEntity,Long> {

    @Query(value = "select * from solo_info where champion_id = ?1 and position = ?2 order by win_count / all_count DESC limit 30",nativeQuery = true)
    Optional<List<SoloInfoEntity>> findAllByChampionIdAndPositionDesc(Long championId, String position);

    @Query(value = "select * from solo_info where champion_id = ?1 and position = ?2 order by win_count / all_count ASC limit 30",nativeQuery = true)
    Optional<List<SoloInfoEntity>> findAllByChampionIdAndPositionAsc(Long championId, String position);

    @Query(value = "select * from solo_info where champion_id = ?1 order by win_count / all_count DESC limit 30",nativeQuery = true)
    Optional<List<SoloInfoEntity>> findAllByChampionIdDesc(Long championId);

    @Query(value = "select * from solo_info where champion_id = ?1 order by win_count / all_count ASC limit 30",nativeQuery = true)
    Optional<List<SoloInfoEntity>> findAllByChampionIdAsc(Long championId);

    @Query(value = "select * from solo_info where position = ?1 order by win_count / all_count DESC limit 30",nativeQuery = true)
    Optional<List<SoloInfoEntity>> findAllByPositionDesc(String position);

    @Query(value = "select * from solo_info where position = ?1 order by win_count / all_count ASC limit 30",nativeQuery = true)
    Optional<List<SoloInfoEntity>> findAllByPositionAsc(String position);

    @Query(value = "select * from solo_info order by win_count / all_count DESC limit 30",nativeQuery = true)
    Optional<List<SoloInfoEntity>> findAllDesc();

    @Query(value = "select * from solo_info order by win_count / all_count ASC limit 30",nativeQuery = true)
    Optional<List<SoloInfoEntity>> findAllAsc();

    Optional<SoloInfoEntity> findByChampionIdAndPosition(Long championId, String position);
}

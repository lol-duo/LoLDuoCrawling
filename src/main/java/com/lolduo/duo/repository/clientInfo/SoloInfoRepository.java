package com.lolduo.duo.repository.clientInfo;

import com.lolduo.duo.entity.clientInfo.SoloInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SoloInfoRepository extends JpaRepository<SoloInfoEntity,Long> {
    Optional<SoloInfoEntity> findByChampionIdAndPosition(Long championId, String position);
}

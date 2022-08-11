package com.lolduo.duo.repository.clientInfo;

import com.lolduo.duo.entity.clientInfo.ICombinationInfoEntity;

import java.util.List;
import java.util.Optional;

public interface ICombinationInfoRepository {
    Optional<? extends List<? extends ICombinationInfoEntity>> findAllByChampionIdAndPositionDesc(String championId, String position, String positionList, String excludePositionList);
    Optional<? extends List<? extends ICombinationInfoEntity>> findAllByChampionIdAndPositionAsc(String championId, String position, String positionList, String excludePositionList);
    Optional<? extends ICombinationInfoEntity> findByChampionIdAndPosition(String championId, String position);
}

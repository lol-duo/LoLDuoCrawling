package com.lolduo.duo.repository.clientInfo;

import com.lolduo.duo.entity.clientInfo.ICombiEntity;
import com.lolduo.duo.entity.gameInfo.IMatchEntity;

import java.util.List;
import java.util.Optional;

public interface ICombiRepository {
    List<? extends ICombiEntity> findAllByChampionIdAndPositionDesc(String championId, String position, String positionList, String excludePositionList);
    List<? extends ICombiEntity> findAllByChampionIdAndPositionAsc(String championId, String position, String positionList, String excludePositionList);
    Optional<? extends ICombiEntity> findByChampionIdAndPosition(String championId, String position);
}

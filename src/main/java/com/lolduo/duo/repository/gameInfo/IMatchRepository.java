package com.lolduo.duo.repository.gameInfo;

import com.lolduo.duo.entity.gameInfo.IMatchEntity;
import com.lolduo.duo.entity.gameInfo.PentaMatchEntity;

import java.time.LocalDate;
import java.util.List;

public interface IMatchRepository {
    List<? extends IMatchEntity> findAllByDate(LocalDate localDate);
}

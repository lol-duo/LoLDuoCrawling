package com.lolduo.duo.repository.gameInfo;

import com.lolduo.duo.entity.gameInfo.DuoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface DuoRepository extends JpaRepository<DuoEntity, Long> {

}

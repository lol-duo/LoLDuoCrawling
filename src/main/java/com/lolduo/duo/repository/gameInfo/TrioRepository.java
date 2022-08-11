package com.lolduo.duo.repository.gameInfo;

import com.lolduo.duo.entity.gameInfo.TrioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TrioRepository extends JpaRepository<TrioEntity,Long> {
}

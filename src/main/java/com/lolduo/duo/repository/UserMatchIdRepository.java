package com.lolduo.duo.repository;

import com.lolduo.duo.entity.UserMatchIdEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface UserMatchIdRepository extends JpaRepository<UserMatchIdEntity, Long> {
    @Query("select distinct u.matchId from UserMatchIdEntity u where u.date = ?1")
    List<String> findAllIdByDate(LocalDate date);

    UserMatchIdEntity findByMatchIdAndPuuid(String matchId, String puuid);
}

package com.lolduo.duo.repository;

import com.lolduo.duo.entity.UserMatchIdEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface UserMatchIdRepository extends JpaRepository<UserMatchIdEntity, Long> {
    @Query(value = "select distinct match_id from user_match where date = ?1 limit ?2, ?3", nativeQuery = true)
    List<String> findAllIdByDate(LocalDate date, int start, int count);

    @Query("select count(distinct um.matchId) from UserMatchIdEntity um where um.date = ?1")
    Long countByDate(LocalDate date);
    UserMatchIdEntity findByMatchIdAndPuuid(String matchId, String puuid);
}

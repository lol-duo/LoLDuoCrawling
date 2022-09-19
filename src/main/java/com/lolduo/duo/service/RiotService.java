package com.lolduo.duo.service;


import com.lolduo.duo.data.NowLocalDate;
import com.lolduo.duo.dto.RiotAPI.league_v4.LeagueEntiryDTO;
import com.lolduo.duo.dto.RiotAPI.league_v4.LeagueListDTO;
import com.lolduo.duo.dto.RiotAPI.match_v5.MatchDto;
import com.lolduo.duo.dto.RiotAPI.summoner_v4.SummonerDTO;
import com.lolduo.duo.dto.RiotAPI.timeline.MatchTimeLineDto;
import com.lolduo.duo.entity.UserEntity;
import com.lolduo.duo.repository.UserMatchIdRepository;
import com.lolduo.duo.repository.UserRepository;
import com.lolduo.duo.service.slack.SlackNotifyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@EnableScheduling
@RequiredArgsConstructor
@Slf4j
public class RiotService{
    private final SlackNotifyService slackNotifyService;
    private final RiotApiSaveService riotApiSaveService;
    private final RiotApiList riotApiList;
    private final UserRepository userRepository;
    private final UserMatchIdRepository matchIdRepository;

    private boolean isDetailWorking = false;
    private final Integer MAX = 205;
    private void setLog(String message){
        log.info(message);
        slackNotifyService.sendMessage(message);
    }
    public ResponseEntity<?> settingInitialData(String version){
        riotApiSaveService.championInitialDataSave(riotApiList.setChampion(version));
        riotApiSaveService.itemInitialDataSave(riotApiList.setItem(version));
        riotApiSaveService.fullItemInitialDataSave(riotApiList.setItem(version));
        riotApiSaveService.spellInitialDataSave(riotApiList.setSpell(version));
        riotApiSaveService.perkInitialDataSave(riotApiList.setPerk(version));
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @Scheduled(cron = "0 0 0 * * *",zone = "Asia/Seoul")
    public ResponseEntity<?> settingPackage(){
        if(isDetailWorking){
            setLog("saveMatchDetail이 이미 동작중입니다.");
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else {
            isDetailWorking = true;
            setLog("RiotService start");
        }
        Long endTime = System.currentTimeMillis() / 1000;
        Long startTime = endTime - 60 * 60 * 24;

        NowLocalDate nowLocalDate = new NowLocalDate(startTime, endTime, LocalDate.now());


        setUserByTopTier("challenger");
        setLog("challenger 종료 time : "+ LocalDateTime.now());
        setUserByTopTier("grandmaster");
        setLog("grandmaster 종료 time : "+ LocalDateTime.now());
        setUserByTopTier("master");
        setLog("master 종료 time : "+ LocalDateTime.now());
        setUserByTierAndRank("DIAMOND", "I");
        setLog("DIAMOND I 종료 time : "+ LocalDateTime.now());
        setUserByTierAndRank("DIAMOND", "II");
        setLog("DIAMOND II 종료 time : "+ LocalDateTime.now());
        setUserByTierAndRank("DIAMOND", "III");
        setLog("DIAMOND III 종료 time : "+ LocalDateTime.now());
        setUserByTierAndRank("DIAMOND", "IV");
        setLog("DIAMOND IV 종료 time : "+ LocalDateTime.now());


        setUserByTierAndRank("PLATINUM", "I");
        setLog("PLATINUM I 종료 time : "+ LocalDateTime.now());
        setUserByTierAndRank("PLATINUM", "II");
        setLog("PLATINUM II 종료 time : "+ LocalDateTime.now());
        setUserByTierAndRank("PLATINUM", "III");
        setLog("PLATINUM III 종료 time : "+ LocalDateTime.now());
        setUserByTierAndRank("PLATINUM", "IV");
        setLog("PLATINUM IV 종료 time : "+ LocalDateTime.now());


        setAllMatchByTier("challenger", nowLocalDate);
        setLog("challenger 종료 time : "+ LocalDateTime.now());
        setAllMatchByTier("grandmaster", nowLocalDate);
        setLog("grandmaster 종료 time : "+ LocalDateTime.now());
        setAllMatchByTier("master", nowLocalDate);
        setLog("master 종료 time : "+ LocalDateTime.now());
        setAllMatchByTierAndRank("DIAMOND", "I", nowLocalDate);
        setLog("DIAMOND I 종료 time : "+ LocalDateTime.now());
        setAllMatchByTierAndRank("DIAMOND", "II", nowLocalDate);
        setLog("DIAMOND II 종료 time : "+ LocalDateTime.now());
        setAllMatchByTierAndRank("DIAMOND", "III", nowLocalDate);
        setLog("DIAMOND III 종료 time : "+ LocalDateTime.now());
        setAllMatchByTierAndRank("DIAMOND", "IV", nowLocalDate);
        setLog("DIAMOND IV 종료 time : "+ LocalDateTime.now());

        setAllMatchByTierAndRank("PLATINUM", "I", nowLocalDate);
        setLog("PLATINUM I 종료 time : "+ LocalDateTime.now());
        setAllMatchByTierAndRank("PLATINUM", "II", nowLocalDate);
        setLog("PLATINUM II 종료 time : "+ LocalDateTime.now());
        setAllMatchByTierAndRank("PLATINUM", "III", nowLocalDate);
        setLog("PLATINUM III 종료 time : "+ LocalDateTime.now());
        setAllMatchByTierAndRank("PLATINUM", "IV", nowLocalDate);
        setLog("PLATINUM IV 종료 time : "+ LocalDateTime.now());

        setMatchDetailByNowLocalDate(nowLocalDate);
        setLog("setMatchDetailByNowLocalDate 종료 time : "+ LocalDateTime.now());

        isDetailWorking = false;
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void setUserByTierAndRank(String tier, String rank){
        Long page = 1L;
        List<LeagueEntiryDTO> leagueEntiryDTO = null;
        do {
            leagueEntiryDTO = riotApiList.getSummonerListByTier(tier, rank, page);

            if(leagueEntiryDTO == null)
                return;

            leagueEntiryDTO.forEach(leagueEntiry -> {
                UserEntity userEntity = userRepository.findById(leagueEntiry.getSummonerId()).orElse(null);

                if(userEntity == null){
                    SummonerDTO summonerDTO = riotApiList.getPuuIdBySummonerId(leagueEntiry.getSummonerId());
                    riotApiSaveService.userSave(leagueEntiry, summonerDTO.getPuuid());
                }
                else{
                    riotApiSaveService.userSave(leagueEntiry, userEntity.getPuuid());
                }
            });
            page = page + 1;
        }while(leagueEntiryDTO.size() == MAX);
    }


    private void setUserByTopTier(String tier) {

        LeagueListDTO leagueListDTO = riotApiList.getSummonerListByTier(tier);
        leagueListDTO.getEntries().forEach(leagueItemDTO -> {

            UserEntity userEntity = userRepository.findById(leagueItemDTO.getSummonerId()).orElse(null);

            if(userEntity == null){
                SummonerDTO summonerDTO = riotApiList.getPuuIdBySummonerId(leagueItemDTO.getSummonerId());
                riotApiSaveService.userSave(leagueItemDTO, summonerDTO.getPuuid(), tier);
            }
            else{
                riotApiSaveService.userSave(leagueItemDTO, userEntity.getPuuid(),tier);
            }
        });
    }
    private void setAllMatchByTier(String tier, NowLocalDate nowDate){
        List<UserEntity> userEntityList = userRepository.findAllByTier(tier);
        userEntityList.forEach(userEntity -> {
            setMatchListByPuuid(userEntity.getPuuid(), nowDate);
        });
    }

    private void setAllMatchByTierAndRank(String tier, String rank, NowLocalDate nowDate){
        List<UserEntity> userEntityList = userRepository.findAllByTierAndRank(tier, rank);
        userEntityList.forEach(userEntity -> {
            setMatchListByPuuid(userEntity.getPuuid(), nowDate);
        });
    }
    private void setMatchListByPuuid(String puuid, NowLocalDate nowDate){

        List<String> matchList = riotApiList.getMatchId(nowDate.getStartTime(), nowDate.getEndTime(), puuid);

        matchList.forEach(matchId -> {
            riotApiSaveService.matchSave(matchId, puuid, nowDate.getLocalDate());
        });

    }

    private void setMatchDetailByNowLocalDate(NowLocalDate nowDate){
        Long matchIdListSize = matchIdRepository.countByDate(nowDate.getLocalDate());

        Integer start = 0, count = 5000;
        List<String> matchIdList = matchIdRepository.findAllIdByDate(nowDate.getLocalDate(),start,count);

        do {
            setLog("matchDetail 진행도 : " + start + " / " + matchIdListSize);
            matchIdList.forEach(matchId -> {
                MatchDto matchDTO = riotApiList.getMatchDetailByMatchId(matchId);
                MatchTimeLineDto matchTimeLineDto = riotApiList.getMatchTimeLineByMatchId(matchId);
                riotApiSaveService.matchDetailSave(matchDTO, nowDate.getLocalDate(),matchTimeLineDto);
            });
            start += count;
            matchIdList = matchIdRepository.findAllIdByDate(nowDate.getLocalDate(), start, count);
        }while(matchIdList.size() != 0);
    }

}

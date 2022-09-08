package com.lolduo.duo.service;


import com.lolduo.duo.dto.RiotAPI.league_v4.LeagueEntiryDTO;
import com.lolduo.duo.dto.RiotAPI.league_v4.LeagueListDTO;
import com.lolduo.duo.dto.RiotAPI.summoner_v4.SummonerDTO;
import com.lolduo.duo.entity.UserEntity;
import com.lolduo.duo.repository.UserRepository;
import com.lolduo.duo.service.slack.SlackNotifyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@EnableScheduling
@RequiredArgsConstructor
@Slf4j
public class RiotService implements ApplicationRunner{


    private final SlackNotifyService slackNotifyService;
    private final RiotApiSaveService riotApiSaveService;
    private final RiotApiList riotApiList;
    private final UserRepository userRepository;

    private final Integer MAX = 205;
    private void setLog(String message){
        log.info(message);
        slackNotifyService.sendMessage(message);
    }
    @Override
    public void run(ApplicationArguments args) throws Exception{
        setLog("RiotService start");
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

        setAllMatchByTier("challenger");
        setLog("challenger 종료 time : "+ LocalDateTime.now());
        setAllMatchByTier("grandmaster");
        setLog("grandmaster 종료 time : "+ LocalDateTime.now());
        setAllMatchByTier("master");
        setLog("master 종료 time : "+ LocalDateTime.now());
        setAllMatchByTierAndRank("DIAMOND", "I");
        setLog("DIAMOND I 종료 time : "+ LocalDateTime.now());
        setAllMatchByTierAndRank("DIAMOND", "II");
        setLog("DIAMOND II 종료 time : "+ LocalDateTime.now());
        setAllMatchByTierAndRank("DIAMOND", "III");
        setLog("DIAMOND III 종료 time : "+ LocalDateTime.now());
        setAllMatchByTierAndRank("DIAMOND", "IV");
        setLog("DIAMOND IV 종료 time : "+ LocalDateTime.now());
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
        return;
    }
    private void setAllMatchByTier(String tier){
        List<UserEntity> userEntityList = userRepository.findAllByTier(tier);
        userEntityList.forEach(userEntity -> {
            setMatchListByPuuid(userEntity.getPuuid());
        });
    }

    private void setAllMatchByTierAndRank(String tier, String rank){
        List<UserEntity> userEntityList = userRepository.findAllByTierAndRank(tier, rank);
        userEntityList.forEach(userEntity -> {
            setMatchListByPuuid(userEntity.getPuuid());
        });
    }
    private void setMatchListByPuuid(String puuid){
        Long now = System.currentTimeMillis();
        Long beginTime = now - 1000 * 60 * 60 * 24;
        LocalDate nowDate = LocalDate.now();

        List<String> matchList = riotApiList.getMatchId(beginTime, now, puuid);
        matchList.forEach(matchId -> {
            riotApiSaveService.matchSave(matchId, puuid, nowDate);
        });
    }


}

package com.lolduo.duo.service;


import com.lolduo.duo.dto.RiotAPI.league_v4.LeagueEntiryDTO;
import com.lolduo.duo.dto.RiotAPI.league_v4.LeagueListDTO;
import com.lolduo.duo.dto.RiotAPI.summoner_v4.SummonerDTO;
import com.lolduo.duo.entity.UserEntity;
import com.lolduo.duo.repository.UserRepository;
import com.lolduo.duo.service.slack.SlackNotifyService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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
    @Override
    public void run(ApplicationArguments args) throws Exception{
        log.info("RiotService 시작 time: {} ", LocalDateTime.now());
        setUserByTopTier("challenger");
        log.info("challenger 종료 time: {} ", LocalDateTime.now());
        setUserByTopTier("grandmaster");
        log.info("grandmaster 종료 time: {} ", LocalDateTime.now());
        setUserByTopTier("master");
        log.info("master 종료 time: {} ", LocalDateTime.now());
        setUserByTierAndRank("DIAMOND", "I");
        log.info("DIAMOND I 종료 time: {} ", LocalDateTime.now());
        setUserByTierAndRank("DIAMOND", "II");
        log.info("DIAMOND II 종료 time: {} ", LocalDateTime.now());
        setUserByTierAndRank("DIAMOND", "III");
        log.info("DIAMOND III 종료 time: {} ", LocalDateTime.now());
        setUserByTierAndRank("DIAMOND", "IV");
        log.info("DIAMOND IV 종료 time: {} ", LocalDateTime.now());
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


}

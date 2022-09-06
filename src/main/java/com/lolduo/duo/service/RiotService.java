package com.lolduo.duo.service;


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

@Service
@EnableScheduling
@RequiredArgsConstructor
@Slf4j
public class RiotService implements ApplicationRunner{


    private final SlackNotifyService slackNotifyService;
    private final RiotApiSaveService riotApiSaveService;
    private final RiotApiList riotApiList;
    private final UserRepository userRepository;
    @Override
    public void run(ApplicationArguments args) throws Exception{
        getPuuIdList("challenger");
        getPuuIdList("grandmaster");
        getPuuIdList("master");
    }


    private void getPuuIdList(String tier) {

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

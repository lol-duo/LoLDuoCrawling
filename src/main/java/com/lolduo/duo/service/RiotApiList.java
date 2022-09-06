package com.lolduo.duo.service;

import com.lolduo.duo.dto.RiotAPI.league_v4.LeagueListDTO;
import com.lolduo.duo.dto.RiotAPI.summoner_v4.SummonerDTO;
import com.lolduo.duo.service.slack.SlackNotifyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RequiredArgsConstructor
@Component
public class RiotApiList {
    private final SlackNotifyService slackNotifyService;
    @Value("${riot.key}")
    private String riotKey;

    private Long currentMillis = System.currentTimeMillis();

    private void checkTime(){
        Long nowMillis = System.currentTimeMillis();
        if(nowMillis - currentMillis > 200){
            currentMillis = nowMillis;
        }
        else{
            try {
                Thread.sleep(200 - (nowMillis - currentMillis));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            currentMillis = System.currentTimeMillis();
        }
    }
    private HttpEntity<Void> setRiotHeader(){
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Riot-Token", riotKey);
        return new HttpEntity<>(headers);
    }

    /** 해당 tier의 유저들을 가져온다.(challenger, grandmaster, master)
     * @return LeagueListDTO || null
     * @param tier*/
    public LeagueListDTO getSummonerListByTier(String tier){
        checkTime();
        String url = "https://kr.api.riotgames.com/lol/league/v4/"+tier+"leagues/by-queue/RANKED_SOLO_5x5";
        try{
            return new RestTemplate().exchange(url, HttpMethod.GET, setRiotHeader(), LeagueListDTO.class).getBody();
        }catch (Exception e){
            log.info("getPuuIdList 에러발생 : {}",e.getMessage());
            slackNotifyService.sendMessage("getPuuid 1번 error \n" + e.getMessage());
            return null;
        }
    }

    /** summonerId로 puuid를 가져온다.
     * @return SummonerDTO || null
     * @param summonerId*/
    public SummonerDTO getPuuIdBySummonerId(String summonerId){
        checkTime();
        String url = "https://kr.api.riotgames.com/lol/summoner/v4/summoners/" + summonerId;
        try{
            return new RestTemplate().exchange(url, HttpMethod.GET, setRiotHeader(), SummonerDTO.class).getBody();
        }catch (Exception e){
            log.info("getPuuIdList 에러발생 summuner: {}",e.getMessage());
            slackNotifyService.sendMessage("getPuuid 2번 error \n" + e.getMessage());
            return null;
        }
    }
}

package com.lolduo.duo.service;

import com.lolduo.duo.dto.RiotAPI.league_v4.LeagueEntiryDTO;
import com.lolduo.duo.dto.RiotAPI.league_v4.LeagueListDTO;
import com.lolduo.duo.dto.RiotAPI.match_v5.MatchDto;
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

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class RiotApiList {
    private final SlackNotifyService slackNotifyService;
    @Value("${riot.key}")
    private String riotKey;

    private Long currentMillis = System.currentTimeMillis();

    /** api 호출 시간을 조절하는 함수*/
    private void checkTime(){
        Long nowMillis = System.currentTimeMillis();
        if(nowMillis - currentMillis > 205){
            currentMillis = nowMillis;
        }
        else{
            try {
                Thread.sleep(205 - (nowMillis - currentMillis));
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
            slackNotifyService.sendMessage("riot summonerList tier error \n" + e.getMessage());
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
            slackNotifyService.sendMessage("riot summonerIdDetail(puuid) error \n" + e.getMessage());
            return null;
        }
    }


    public List<LeagueEntiryDTO> getSummonerListByTier(String tier, String rank, Long page){
        checkTime();
        String url = "https://kr.api.riotgames.com/lol/league/v4/entries/RANKED_SOLO_5x5/"+tier+"/"+rank+"?page=" + page;
        try{
            ResponseEntity<LeagueEntiryDTO[]> response = new RestTemplate().exchange(url, HttpMethod.GET, setRiotHeader(), LeagueEntiryDTO[].class);
            return List.of(response.getBody());
        }catch (Exception e){
            log.info("getPuuIdList 에러발생 : {}",e.getMessage());
            slackNotifyService.sendMessage("riot summonerList rank error \n" + e.getMessage());
            return null;
        }
    }

    public List<String> getMatchId(Long startTime, Long endTime, String puuid) {
        checkTime();
        String url = "https://asia.api.riotgames.com/lol/match/v5/matches/by-puuid/"+ puuid + "/ids?startTime=" + startTime + "&endTime=" + endTime + "&type=ranked&start=0&count=100";
        try {
            ResponseEntity<List> response = new RestTemplate().exchange(url , HttpMethod.GET, setRiotHeader(), List.class);
            return response.getBody();
        }catch (Exception e) {
            log.info("getMatchId 에러발생 : {}",e.getMessage());
            slackNotifyService.sendMessage("riot matchId api error \n" + e.getMessage());
            return null;
        }
    }
    public MatchDto getMatchDetailByMatchId(String matchId) {
        checkTime();
        String url = "https://asia.api.riotgames.com/lol/match/v5/matches/" + matchId;
        try {
            return new RestTemplate().exchange(url , HttpMethod.GET, setRiotHeader(), MatchDto.class).getBody();
        }catch (Exception e) {
            log.info("getMatchDetailByMatchId 에러발생 : {}",e.getMessage());
            slackNotifyService.sendMessage("riot matchDetail api error \n" + e.getMessage() + "matchId : " + matchId);
            return null;
        }
    }
}

package com.lolduo.duo.service;


import com.lolduo.duo.dto.RiotAPI.league_v4.LeagueListDTO;
import com.lolduo.duo.dto.RiotAPI.summoner_v4.SummonerDTO;
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

    @Value("${riot.key}")
    private String riotKey;
    private final SlackNotifyService slackNotifyService;
    @Override
    public void run(ApplicationArguments args) throws Exception{

    }

    private HttpEntity<Void> setRiotHeader(){
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Riot-Token", riotKey);
        return new HttpEntity<>(headers);
    }

    private void getPuuIdList(String league) {
        String url = "https://kr.api.riotgames.com/lol/league/v4/"+league+"leagues/by-queue/RANKED_SOLO_5x5";
        String url_summoner = "https://kr.api.riotgames.com/lol/summoner/v4/summoners/";

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<LeagueListDTO> response = null;

        try{
            response = restTemplate.exchange(url, HttpMethod.GET, setRiotHeader(), LeagueListDTO.class);
        }catch (Exception e){
            log.info("getPuuIdList 에러발생 : {}",e.getMessage());
            slackNotifyService.sendMessage("getPuuid 1번 error \n" + e.getMessage());
            return;
        }

        response.getBody().getEntries().forEach(leagueItemDTO -> {
            String puuid = null;
            try{
                puuid = restTemplate.exchange(url_summoner + leagueItemDTO.getSummonerId(), HttpMethod.GET, setRiotHeader(), SummonerDTO.class).getBody().getPuuid();
            }catch (Exception e){
                log.info("getPuuIdList 에러발생 summuner: {}",e.getMessage());
                slackNotifyService.sendMessage("getPuuid 2번 error \n" + e.getMessage());
                return;
            }



        });
        return;
    }


}

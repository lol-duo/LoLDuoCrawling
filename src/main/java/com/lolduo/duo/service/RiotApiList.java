package com.lolduo.duo.service;

import com.lolduo.duo.dto.RiotAPI.league_v4.LeagueEntiryDTO;
import com.lolduo.duo.dto.RiotAPI.league_v4.LeagueListDTO;
import com.lolduo.duo.dto.RiotAPI.match_v5.MatchDto;
import com.lolduo.duo.dto.RiotAPI.summoner_v4.SummonerDTO;
import com.lolduo.duo.dto.RiotAPI.timeline.MatchTimeLineDto;
import com.lolduo.duo.dto.ddr.champion.ChampionDto;
import com.lolduo.duo.dto.ddr.item.ItemDto;
import com.lolduo.duo.dto.ddr.perk.PerkDto;
import com.lolduo.duo.dto.ddr.spell.SpellDto;
import com.lolduo.duo.service.slack.SlackNotifyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
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

    /** N초 동안 sleep 하는 함수
     * N = sleepTime */
    private void sleepAndRestart(){
        Long SLEEP_TIME = 300000L;
        try {
            slackNotifyService.sendMessage(SLEEP_TIME/1000L +"초 후 재실행합니다. \n");
            Thread.sleep(SLEEP_TIME);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    /** api 헤더 설정 함수*/
    private HttpEntity<Void> setRiotHeader(){
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Riot-Token", riotKey);
        return new HttpEntity<>(headers);
    }

    /** 해당 tier의 유저들을 가져온다.(challenger, grandmaster, master)
     * @return LeagueListDTO || null
     * @param tier - user의 rank*/
    public LeagueListDTO getSummonerListByTier(String tier){
        checkTime();
        while(true) {
            String url = "https://kr.api.riotgames.com/lol/league/v4/" + tier + "leagues/by-queue/RANKED_SOLO_5x5";
            try {
                return new RestTemplate().exchange(url, HttpMethod.GET, setRiotHeader(), LeagueListDTO.class).getBody();
            } catch (Exception e) {
                log.info("getPuuIdList 에러발생 : {}", e.getMessage());
                slackNotifyService.sendMessage("riot summonerList tier error \n" + e.getMessage());
                sleepAndRestart();
            }
        }
    }

    /** summonerId로 puuid를 가져온다.
     * @return SummonerDTO || null
     * @param summonerId - summonerId */
    public SummonerDTO getPuuIdBySummonerId(String summonerId){
        checkTime();
        while(true) {
            String url = "https://kr.api.riotgames.com/lol/summoner/v4/summoners/" + summonerId;
            try {
                return new RestTemplate().exchange(url, HttpMethod.GET, setRiotHeader(), SummonerDTO.class).getBody();
            } catch (Exception e) {
                log.info("getPuuIdList 에러발생 summuner: {}", e.getMessage());
                slackNotifyService.sendMessage("riot summonerIdDetail(puuid) error \n" + e.getMessage());
                sleepAndRestart();
            }
        }
    }


    public List<LeagueEntiryDTO> getSummonerListByTier(String tier, String rank, Long page){
        checkTime();
        while(true) {
            String url = "https://kr.api.riotgames.com/lol/league/v4/entries/RANKED_SOLO_5x5/" + tier + "/" + rank + "?page=" + page;
            try {
                ResponseEntity<LeagueEntiryDTO[]> response = new RestTemplate().exchange(url, HttpMethod.GET, setRiotHeader(), LeagueEntiryDTO[].class);
                return List.of(response.getBody());
            } catch (Exception e) {
                log.info("getPuuIdList 에러발생 : {}", e.getMessage());
                slackNotifyService.sendMessage("riot summonerList rank error \n" + e.getMessage());
                sleepAndRestart();
            }
        }
    }

    public List<String> getMatchId(Long startTime, Long endTime, String puuid) {
        checkTime();
        while(true) {
            String url = "https://asia.api.riotgames.com/lol/match/v5/matches/by-puuid/" + puuid + "/ids?startTime=" + startTime + "&endTime=" + endTime + "&type=ranked&start=0&count=100";
            try {
                ResponseEntity<List> response = new RestTemplate().exchange(url, HttpMethod.GET, setRiotHeader(), List.class);
                return response.getBody();
            } catch (Exception e) {
                log.info("getMatchId 에러발생 : {}", e.getMessage());
                slackNotifyService.sendMessage("riot matchId api error \n" + e.getMessage());
                sleepAndRestart();
            }
        }
    }
    public MatchDto getMatchDetailByMatchId(String matchId) {
        checkTime();
        while(true) {
            String url = "https://asia.api.riotgames.com/lol/match/v5/matches/" + matchId;
            try {
                return new RestTemplate().exchange(url, HttpMethod.GET, setRiotHeader(), MatchDto.class).getBody();
            } catch (Exception e) {
                log.info("getMatchDetailByMatchId 에러발생 : {}", e.getMessage());
                slackNotifyService.sendMessage("riot matchDetail api error \n" + e.getMessage() + "matchId : " + matchId);
                sleepAndRestart();
            }
        }
    }
    public MatchTimeLineDto getMatchTimeLineByMatchId(String matchId){
        checkTime();
        while(true) {
            String url = "https://asia.api.riotgames.com/lol/match/v5/matches/" + matchId + "/timeline";
            try {
                return new RestTemplate().exchange(url, HttpMethod.GET, setRiotHeader(), MatchTimeLineDto.class).getBody();
            } catch (Exception e) {
                log.info("getMatchTimeLineByMatchId 에러발생 : {}", e.getMessage());
                slackNotifyService.sendMessage("riot match TimeLine api error \n" + e.getMessage() + "matchId : " + matchId);
                sleepAndRestart();
            }
        }
    }

    /** ddr 사이트에서 아이템 정보를 가져오는 함수
     * RIOT KEY를 사용하지 않음
     * @return ItemDto || null
     * @param version - 롤 패치 버전*/
    public ItemDto setItem(String version) {
        String url = "https://ddragon.leagueoflegends.com/cdn/" + version + "/data/ko_KR/item.json";
        try {
            return new RestTemplate().exchange(url, HttpMethod.GET, setRiotHeader(), ItemDto.class).getBody();
        } catch (Exception e) {
            log.info("setItem 에러발생 : {}", e.getMessage());
            slackNotifyService.sendMessage("riot initial Data api(DDR - Item) error \n" + e.getMessage());
            return null;
        }
    }

    /** ddr 사이트에서 챔피언 정보를 가져오는 함수
     * RIOT KEY를 사용하지 않음
     * @return ChampionDto || null
     * @param version - 롤 패치 버전 */
    public ChampionDto setChampion(String version) {
        String url = "https://ddragon.leagueoflegends.com/cdn/" + version + "/data/ko_KR/champion.json";
        try {
            return new RestTemplate().exchange(url, HttpMethod.GET, setRiotHeader(), ChampionDto.class).getBody();
        } catch (Exception e) {
            log.info("setChampion 에러발생 : {}", e.getMessage());
            slackNotifyService.sendMessage("riot initial Data api(DDR - Champion) error \n" + e.getMessage());
            return null;
        }
    }

    /** ddr 사이트에서 스펠 정보를 가져오는 함수
     * RIOT KEY를 사용하지 않음
     * @return SpellDto || null
     * @param version - 롤 패치 버전 */
    public SpellDto setSpell(String version) {
        String url = "https://ddragon.leagueoflegends.com/cdn/" + version + "/data/ko_KR/summoner.json";
        try {
            return new RestTemplate().exchange(url, HttpMethod.GET, setRiotHeader(), SpellDto.class).getBody();
        } catch (Exception e) {
            log.info("setSpell 에러발생 : {}", e.getMessage());
            slackNotifyService.sendMessage("riot initial Data api(DDR - Spell) error \n" + e.getMessage());
            return null;
        }
    }

    /** ddr 사이트에서 룬 정보를 가져오는 함수
     * RIOT KEY를 사용하지 않음
     * @return List<PerkDto> || null
     * @param version - 롤 패치 버전 */
    public List<PerkDto> setPerk(String version){
        String url = "https://ddragon.leagueoflegends.com/cdn/"+version+"/data/ko_KR/runesReforged.json";
        try{
            return Arrays.asList(new RestTemplate().exchange(url, HttpMethod.GET, setRiotHeader(), PerkDto[].class).getBody());
        } catch (Exception e){
            log.info("setPerk 에러발생 : {}", e.getMessage());
            slackNotifyService.sendMessage("riot initial Data api(DDR - Perk) error \n" + e.getMessage());
            return null;
        }
    }

}

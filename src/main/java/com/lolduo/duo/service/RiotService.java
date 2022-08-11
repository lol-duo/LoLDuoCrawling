package com.lolduo.duo.service;


import com.lolduo.duo.dto.champion.ChampionDto;
import com.lolduo.duo.dto.item.ItemDto;
import com.lolduo.duo.dto.league_v4.LeagueListDTO;
import com.lolduo.duo.dto.setting.perk.PerkDto;
import com.lolduo.duo.dto.setting.perk.PerkRune;
import com.lolduo.duo.dto.spell.SpellDto;
import com.lolduo.duo.dto.summoner_v4.SummonerDTO;
import com.lolduo.duo.dto.timeline.MatchTimeLineDto;
import com.lolduo.duo.entity.*;
import com.lolduo.duo.entity.gameInfo.DuoEntity;
import com.lolduo.duo.entity.gameInfo.QuintetEntity;
import com.lolduo.duo.entity.gameInfo.TrioEntity;
import com.lolduo.duo.entity.item.ItemEntity;
import com.lolduo.duo.entity.item.ItemFullEntity;
import com.lolduo.duo.dto.match_v5.MatchDto;
import com.lolduo.duo.dto.match_v5.Participant;
import com.lolduo.duo.entity.gameInfo.SoloEntity;
import com.lolduo.duo.repository.*;
import com.lolduo.duo.repository.gameInfo.DuoRepository;
import com.lolduo.duo.repository.gameInfo.QuintetRepository;
import com.lolduo.duo.repository.gameInfo.SoloRepository;
import com.lolduo.duo.repository.gameInfo.TrioRepository;
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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Service
@EnableScheduling
@RequiredArgsConstructor
@Slf4j
public class RiotService implements ApplicationRunner{

    @Value("${riot.key}")
    private String key;
    private String version;
    private final ItemRepository itemRepository;
    private final ItemFullRepository itemFullRepository;
    private final PerkRepository perkRepository;
    private final ChampionRepository championRepository;
    private final SpellRepository spellRepository;
    private final SoloRepository soloRepository;
    private final DuoRepository duoRepository;
    private final TrioRepository trioRepository;
    private final QuintetRepository quintetRepository;
    private final LoLUserRepository lolUserRepository;
    private final MatchDetailRepository matchDetailRepository;
    private final SlackNotifyService slackNotifyService;
    private final InfoService infoService;

    public void setKey(String key) {
        this.key = key;
    }
    public void setVersion(String version) {
        this.version = version;
    }
    @Override
    public void run(ApplicationArguments args) throws Exception{
        setVersion("12.14.1");
        //setItem();
        //setChampion();
        //setSpell();
        //setPerk();
        //All();
        //test();
        log.info("ready");
    }
    public void test(){
        String[] st = {"KR_6048833978",
                "KR_6048791185",
                "KR_6047665628",
                "KR_6047556921",
                "KR_6045011909",
                "KR_6044866927",
                "KR_6044652920",
                "KR_6044599971",
                "KR_6044558095",
                "KR_6044545900",
                "KR_6043272985",
                "KR_6043196788",
                "KR_6043163990",
                "KR_6042147114",
                "KR_6041967954",
                "KR_6041961802",
                "KR_6032993563",
                "KR_6032944098",
                "KR_6032809258",
                "KR_6031458458"};
        getMatchInfo(new HashSet<>(Arrays.asList(st)));
        log.info("solo~team 정보 저장완료 ");
        log.info("2차 가공 start");
        infoService.makeSoloInfo();
        infoService.makeDuoInfo();
        infoService.makeTrioInfo();
        infoService.makeQuintetInfo();
        log.info("2차 가공 end");
    }
    @Scheduled(cron = "1 0 0 * * *", zone = "Asia/Seoul")
    private void All(){
        Long endTime = System.currentTimeMillis() / 1000;
        Long startTime = endTime - 43200;
        slackNotifyService.sendMessage(slackNotifyService.nowTime() + "challenger list 가져오기 start");
        log.info("get challenger start");
        getPuuIdList("challenger");

        slackNotifyService.sendMessage(slackNotifyService.nowTime() + "grandmaster list 가져오기 start");
        log.info("get grandmaster start");
        getPuuIdList("grandmaster");

        slackNotifyService.sendMessage(slackNotifyService.nowTime() + "master list 가져오기 start");
        log.info("get master start");
        getPuuIdList("master");

        Set<String> matchIdList = new HashSet<>();

        slackNotifyService.sendMessage(slackNotifyService.nowTime() + "challenger matchId 만들기 start");
        log.info("make challenger matchIList start");
        matchIdList.addAll(getMatchId(startTime,endTime,"challenger"));

        slackNotifyService.sendMessage(slackNotifyService.nowTime() + "grandmaster matchId 만들기 start");
        log.info("make grandmaster matchIList start");
        matchIdList.addAll(getMatchId(startTime,endTime,"grandmaster"));

        slackNotifyService.sendMessage(slackNotifyService.nowTime() + "master matchId 만들기 start");
        log.info("make master matchIList start");
        matchIdList.addAll(getMatchId(startTime,endTime,"master"));

        slackNotifyService.sendMessage(slackNotifyService.nowTime() + "matchId 만들기 start");
        log.info("getMatch Info start");
        getMatchInfo(matchIdList);
        log.info("solo~team 정보 저장완료 ");
        log.info("2차 가공 start");

        setSolo();
        setDuo();
        setTrio();
        setQuintet();

        slackNotifyService.sendMessage(slackNotifyService.nowTime() + "SoloInfo 만들기 start");
        infoService.makeSoloInfo();

        slackNotifyService.sendMessage(slackNotifyService.nowTime() + "DuoInfo 만들기 start");
        infoService.makeDuoInfo();

        slackNotifyService.sendMessage(slackNotifyService.nowTime() + "TrioInfo 만들기 start");
        infoService.makeTrioInfo();

        slackNotifyService.sendMessage(slackNotifyService.nowTime() + "QuintetInfo 만들기 start");
        infoService.makeQuintetInfo();
        log.info("2차 가공 end");
    }

    private void makeFullItem(ItemDto item){
        Set<String> itemIdList = item.getData().keySet();
        for(String itemId : itemIdList){
            if(item.getData().get(itemId).getInto() == null && item.getData().get(itemId).getGold().getTotal() > 1500L){
                itemFullRepository.save(new ItemFullEntity(Long.parseLong(itemId)));
            }
        }
    }
    private void getMatchInfo(Set<String> matchIdList){
        String url = "https://asia.api.riotgames.com/lol/match/v5/matches/";

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Riot-Token", key);
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        matchIdList.forEach(matchId -> {
            final int PlayerNum = 10;
            ResponseEntity<MatchTimeLineDto> time_match = null;
            try{
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                time_match = restTemplate.exchange(url + matchId + "/timeline", HttpMethod.GET, requestEntity, MatchTimeLineDto.class);
            }catch (Exception e){
                log.info("matchId : " + matchId + " 에러");
                return;
            }
            List<List<Long>> playerItemList = new ArrayList<List<Long>>();
            for(int i =0 ; i<= PlayerNum; i++){
                playerItemList.add(new ArrayList<>());
            }

            Map<String, Long> puuIdMap = new HashMap<>();
            time_match.getBody().getInfo().getParticipants().forEach(participantDto -> {
                puuIdMap.put(participantDto.getPuuid(), participantDto.getParticipantId());
            });
            time_match.getBody().getInfo().getFrames().forEach(frameDto -> {
                frameDto.getEvents().forEach(eventDto -> {
                    if(eventDto.getType().equals("ITEM_PURCHASED") && itemFullRepository.findById(eventDto.getItemId()).orElse(null) != null){
                        playerItemList.get(eventDto.getParticipantId().intValue()).add(eventDto.getItemId());
                    }
                });
            });
            for(int i =0 ; i<= PlayerNum; i++){
                playerItemList.get(i).add(0L);
                playerItemList.get(i).add(0L);
                playerItemList.get(i).add(0L);
            }
            ResponseEntity<MatchDto> response_match = null;
            try {
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                response_match = restTemplate.exchange(url + matchId, HttpMethod.GET, requestEntity, MatchDto.class);
            }catch (Exception e){
                log.info("matchId : " + matchId + " 에러");
                return;
            }
            String tier = getTier(response_match.getBody());
            matchDetailRepository.save(new MatchEntity(LocalDate.now(ZoneId.of("Asia/Seoul")),response_match.getBody(),  playerItemList, puuIdMap,tier));
        });
    }
    private String getTier(MatchDto matchDto){
        Map<String, Long> tierNumList = new HashMap<>();
        tierNumList.put("challenger", 1L);
        tierNumList.put("grandmaster", 2L);
        tierNumList.put("master", 3L);

        Map<Integer, String> tierNameList =  new HashMap<>();
        tierNameList.put(1, "challenger");
        tierNameList.put(2, "grandmaster");
        tierNameList.put(3, "master");

        Long tierNum = 0L;
        List<Participant> participantList = matchDto.getInfo().getParticipants();
        LoLUserEntity loLUserEntity = null ;
        for(Participant participant : participantList) {
            loLUserEntity = lolUserRepository.findById(participant.getPuuid()).orElse(null);
            if(loLUserEntity==null){
                tierNum += 3;
            }
            else{
                tierNum += tierNumList.get(loLUserEntity.getTier());
            }
        }
        return tierNameList.get(Math.round(tierNum/ 10));
    }

    private void setSolo(){
        List<MatchEntity> matchEntity = matchDetailRepository.findAllByDate(LocalDate.now(ZoneId.of("Asia/Seoul")));
        matchEntity.forEach(match -> {
            MatchDto matchDto = match.getMatchInfo();
            List<List<Long>> playerItemList = match.getPlayerItemList();
            Map<String, Long> puuIdMap = match.getPuuIdMap();
            String tier = match.getTier();

            matchDto.getInfo().getParticipants().forEach(participant -> {
                Boolean win = participant.getWin();
                String position = participant.getTeamPosition();
                List<Long> itemList = playerItemList.get(puuIdMap.get(participant.getPuuid()).intValue());
                TreeSet<Long> spellList = new TreeSet<>();
                spellList.add(participant.getSummoner1Id());
                spellList.add(participant.getSummoner2Id());
                Long championId = participant.getChampionId();
                List<Long> perkList = new ArrayList<>();
                perkList.add(participant.getPerks().getStatPerks().getDefense());
                perkList.add(participant.getPerks().getStatPerks().getOffense());
                perkList.add(participant.getPerks().getStatPerks().getFlex());
                participant.getPerks().getStyles().forEach(perkStyle -> {
                    perkStyle.getSelections().forEach(perkStyleSelection -> {
                        perkList.add(perkStyleSelection.getPerk());
                    });
                    perkList.add(perkStyle.getStyle());
                });
                Collections.sort(perkList);
                soloRepository.save(new SoloEntity(tier, win,position,itemList,spellList,championId,perkList));
            });
        });

    }
    private void setDuo() {
        List<MatchEntity> matchEntity = matchDetailRepository.findAllByDate(LocalDate.now(ZoneId.of("Asia/Seoul")));
        matchEntity.forEach(match -> {
            MatchDto matchDto = match.getMatchInfo();
            List<List<Long>> playerItemList = match.getPlayerItemList();
            Map<String, Long> puuIdMap = match.getPuuIdMap();
            String tier = match.getTier();
            Map<String, Boolean> visitedWin = new HashMap<>();
            Map<String, Boolean> visitedLose = new HashMap<>();
            matchDto.getInfo().getParticipants().forEach(participant -> {
                if (participant.getWin() == true) {
                    visitedWin.put(participant.getPuuid(), false);
                } else {
                    visitedLose.put(participant.getPuuid(), false);
                }
            });
            //2인 정보 save
            combination(matchDto, playerItemList, puuIdMap, new ArrayList<>(), visitedWin, true, 2, 0, tier);
            combination(matchDto, playerItemList, puuIdMap, new ArrayList<>(), visitedLose, false, 2, 0, tier);
        });
    }
    private void setTrio() {
        List<MatchEntity> matchEntity = matchDetailRepository.findAllByDate(LocalDate.now(ZoneId.of("Asia/Seoul")));
        matchEntity.forEach(match -> {
            MatchDto matchDto = match.getMatchInfo();
            List<List<Long>> playerItemList = match.getPlayerItemList();
            Map<String, Long> puuIdMap = match.getPuuIdMap();
            String tier = match.getTier();
            Map<String, Boolean> visitedWin = new HashMap<>();
            Map<String, Boolean> visitedLose = new HashMap<>();
            matchDto.getInfo().getParticipants().forEach(participant -> {
                if (participant.getWin() == true) {
                    visitedWin.put(participant.getPuuid(), false);
                } else {
                    visitedLose.put(participant.getPuuid(), false);
                }
            });
            //3인 정보 save
            combination(matchDto, playerItemList, puuIdMap, new ArrayList<>(), visitedWin, true, 3, 0, tier);
            combination(matchDto, playerItemList, puuIdMap, new ArrayList<>(), visitedLose, false, 3, 0, tier);
        });
    }
    private void setQuintet() {
        List<MatchEntity> matchEntity = matchDetailRepository.findAllByDate(LocalDate.now(ZoneId.of("Asia/Seoul")));
        matchEntity.forEach(match -> {
            MatchDto matchDto = match.getMatchInfo();
            List<List<Long>> playerItemList = match.getPlayerItemList();
            Map<String, Long> puuIdMap = match.getPuuIdMap();
            String tier = match.getTier();
            List<Participant> winParticipantList = new ArrayList<>();
            List<Participant> loseParticipantList = new ArrayList<>();
            matchDto.getInfo().getParticipants().forEach(participant -> {
                if (participant.getWin() == true) {
                    winParticipantList.add(participant);
                } else {
                    loseParticipantList.add(participant);
                }
            });
            //5인 정보 save
            saveMatchInfo(winParticipantList, playerItemList, puuIdMap, true, 5, tier);
            saveMatchInfo(loseParticipantList, playerItemList, puuIdMap, false, 5, tier);
        });
    }
    private void combination(MatchDto matchDto, List<List<Long>> playerItemList, Map<String, Long> puuIdMap,List<Participant> participantList,Map<String,Boolean> visited,Boolean win,int number,int start,String tier){
        if(participantList.size()==number){
            saveMatchInfo(participantList,playerItemList,puuIdMap,win,number, tier);
            return;
        }
        for(int i = start; i< matchDto.getInfo().getParticipants().size(); i++){
            Participant participant = matchDto.getInfo().getParticipants().get(i);
            if (visited.containsKey(participant.getPuuid())==true && visited.get(participant.getPuuid())==false) {
                participantList.add(participant);
                visited.put(participant.getPuuid(),true);
                combination(matchDto,playerItemList,puuIdMap,participantList,visited,win,number,i+1, tier);
                participantList.remove(participant);
                visited.put(participant.getPuuid(),false);
            }
        }
    }
    private void saveMatchInfo(List<Participant> participantList, List<List<Long>> playerItemList, Map<String, Long> puuIdMap, Boolean win,int number,String tier){
        Map<Long,String> positionMap = new HashMap<>();
        Map<Long,List<Long>> itemListMap = new HashMap<>();
        Map<Long,TreeSet<Long>> spellListMap = new HashMap<>();
        TreeSet<Long> championList = new TreeSet<>();
        Map<Long,List<Long>> perkListMap = new HashMap<>();

        // 260line까지 sestSolo와 중복되는 부분, 추후에 가능하면 AOP 적용
        participantList.forEach(participant -> {
            String position = participant.getTeamPosition();
            List<Long> itemList = playerItemList.get(puuIdMap.get(participant.getPuuid()).intValue());
            TreeSet<Long> spellList = new TreeSet<>();
            spellList.add(participant.getSummoner1Id());
            spellList.add(participant.getSummoner2Id());
            Long championId = participant.getChampionId();
            List<Long> perkList = new ArrayList<>();
            perkList.add(participant.getPerks().getStatPerks().getDefense());
            perkList.add(participant.getPerks().getStatPerks().getOffense());
            perkList.add(participant.getPerks().getStatPerks().getFlex());
            participant.getPerks().getStyles().forEach(perkStyle -> {
                perkStyle.getSelections().forEach(perkStyleSelection -> {
                    perkList.add(perkStyleSelection.getPerk());
                });
                perkList.add(perkStyle.getStyle());
            });
            Collections.sort(perkList);
            positionMap.put(championId,position);
            itemListMap.put(championId,itemList);
            spellListMap.put(championId,spellList);
            championList.add(championId);
            perkListMap.put(championId,perkList);
        });
        if(number==2){
            duoRepository.save(new DuoEntity(tier,win,positionMap,itemListMap,spellListMap,championList,perkListMap) );

        }
        else if(number==3){
            trioRepository.save(new TrioEntity(tier,win,positionMap,itemListMap,spellListMap,championList,perkListMap));
        }
        else if(number==5){
            quintetRepository.save(new QuintetEntity(tier,win,positionMap,itemListMap,spellListMap,championList,perkListMap));
        }

    }
    private void getPuuIdList(String league) {
        String url = "https://kr.api.riotgames.com/lol/league/v4/"+league+"leagues/by-queue/RANKED_SOLO_5x5";
        String url_summoner = "https://kr.api.riotgames.com/lol/summoner/v4/summoners/";

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Riot-Token", key);
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<LeagueListDTO> response = null;
        try{
            response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, LeagueListDTO.class);
        }catch (Exception e){
            log.info("에러발생 : {}",e.getMessage());
            return;
        }
        response.getBody().getEntries().forEach(leagueItemDTO -> {
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                log.info("getSummonerId thread Error!");
                throw new RuntimeException(e);
            }
            String puuid = null;
            try{
                puuid = restTemplate.exchange(url_summoner + leagueItemDTO.getSummonerId(), HttpMethod.GET, requestEntity, SummonerDTO.class).getBody().getPuuid();
            }catch (Exception e){
                log.info("에러발생 summuner: {}",e.getMessage());
                return;
            }

            LoLUserEntity lolUserEntity = lolUserRepository.findById(puuid).orElse(null);
            if(lolUserEntity == null)
                lolUserRepository.save(new LoLUserEntity(puuid,league));
            else if(lolUserEntity.getPuuid() != puuid){
                lolUserEntity.setTier(league);
                lolUserRepository.save(lolUserEntity);
            }
        });
        return;
    }

    private Set<String> getMatchId(Long startTime, Long endTime, String league) {
        String url = "https://asia.api.riotgames.com/lol/match/v5/matches/by-puuid/";
        List<String> puuidList = lolUserRepository.findPuuidsByLeague(league);

        Set<String> matchList = new HashSet<>();
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Riot-Token", key);
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        puuidList.forEach(puuid -> {
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            ResponseEntity<List> response = null;
            try {
                response = restTemplate.exchange(url + puuid + "/ids?startTime=" + startTime + "&endTime=" + endTime + "&type=ranked&start=0&count=100", HttpMethod.GET, requestEntity, List.class);
            }catch (Exception e) {
                log.info("에러발생 : {}",e.getMessage());
                return;
            }

            matchList.addAll(response.getBody());
        });
        return matchList;
    }

    //  패치 별 아이템, 챔피언 ,스펠, 룬 정보 정보들 DB에 세팅해주는 부분들
    private void setItem(){
        String url = "https://ddragon.leagueoflegends.com/cdn/"+version+"/data/ko_KR/item.json";

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Riot-Token", key);
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<ItemDto> item = restTemplate.exchange(url, HttpMethod.GET, requestEntity, ItemDto.class);
        //추후에 map iter 로 변경하여서 n번
        Set<String> itemIdList = item.getBody().getData().keySet();
        for(String itemId : itemIdList){
            itemRepository.save(new ItemEntity(Long.parseLong(itemId), item.getBody().getData().get(itemId).getName(),itemId + ".png"));
        }
        makeFullItem(item.getBody());
    }
    private void setChampion(){
        String url = "https://ddragon.leagueoflegends.com/cdn/"+version+"/data/ko_KR/champion.json";

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Riot-Token", key);
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<ChampionDto> championList = restTemplate.exchange(url, HttpMethod.GET, requestEntity, ChampionDto.class);

        Set<String> championIdList = championList.getBody().getData().keySet();
        for(String championId : championIdList){
            championRepository.save(new ChampionEntity(Long.parseLong(championList.getBody().getData().get(championId).getKey()), championList.getBody().getData().get(championId).getName(),championId + ".png"));
        }
        championRepository.save(new ChampionEntity(0L,"A","A.png"));
    }
    private void setSpell(){
        String url = "https://ddragon.leagueoflegends.com/cdn/"+version+"/data/ko_KR/summoner.json";

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Riot-Token", key);
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<SpellDto> spellList = restTemplate.exchange(url, HttpMethod.GET, requestEntity, SpellDto.class);

        Set<String> spellIdList = spellList.getBody().getData().keySet();
        for(String spellId : spellIdList){
            if(Integer.parseInt(spellList.getBody().getData().get(spellId).getKey()) > 21) {
                continue;
            }
            spellRepository.save(new SpellEntity(Long.parseLong(spellList.getBody().getData().get(spellId).getKey()), spellList.getBody().getData().get(spellId).getName(),spellId + ".png"));
        }
    }
    private void setPerk(){
        String url = "https://ddragon.leagueoflegends.com/cdn/"+version+"/data/ko_KR/runesReforged.json";

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Riot-Token", key);
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<PerkDto[]> response_perkDtoList = restTemplate.exchange(url, HttpMethod.GET, requestEntity, PerkDto[].class);
        List<PerkDto> perkDtoList = Arrays.asList(response_perkDtoList.getBody());
        perkDtoList.forEach(perkDto -> {
            for(int i = 0; i < perkDto.getSlots().size(); i++){
                List<PerkRune> perkRuneList = perkDto.getSlots().get(i).getRunes();
                for(int j = 0; j < perkRuneList.size(); j++){
                    perkRepository.save(new PerkEntity(perkRuneList.get(j).getId(),perkRuneList.get(j).getName(), perkRuneList.get(j).getIcon()));
                }
            }
            perkRepository.save(new PerkEntity(perkDto.getId(), perkDto.getName(), perkDto.getIcon()));
        });

    }
}

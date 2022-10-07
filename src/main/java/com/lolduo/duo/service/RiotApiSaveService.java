package com.lolduo.duo.service;

import com.lolduo.duo.dto.RiotAPI.league_v4.LeagueEntiryDTO;
import com.lolduo.duo.dto.RiotAPI.league_v4.LeagueItem;
import com.lolduo.duo.dto.RiotAPI.match_v5.MatchDto;
import com.lolduo.duo.dto.RiotAPI.timeline.MatchTimeLineDto;
import com.lolduo.duo.dto.ddr.champion.ChampionDto;
import com.lolduo.duo.dto.ddr.item.ItemDto;
import com.lolduo.duo.dto.ddr.perk.PerkDto;
import com.lolduo.duo.dto.ddr.perk.PerkRune;
import com.lolduo.duo.dto.ddr.spell.SpellDto;
import com.lolduo.duo.entity.InitialDataEntity.*;
import com.lolduo.duo.entity.MatchDetailEntity;
import com.lolduo.duo.entity.UserEntity;
import com.lolduo.duo.entity.UserMatchIdEntity;
import com.lolduo.duo.repository.InitialDataRepository.*;
import com.lolduo.duo.repository.MatchDetailRepository;
import com.lolduo.duo.repository.UserMatchIdRepository;
import com.lolduo.duo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class RiotApiSaveService {

    private final UserRepository userRepository;
    private final UserMatchIdRepository userMatchIdRepository;
    private final MatchDetailRepository matchDetailRepository;
    private final ChampionRepository championRepository;
    private final SpellRepository spellRepository;
    private final PerkRepository perkRepository;
    private final ItemRepository itemRepository;
    private final ItemFullRepository itemFullRepository;

    public boolean isSameUserInfo(UserEntity userEntity, LeagueEntiryDTO leagueEntiryDTO){
        if(!userEntity.getSummonerName().equals(leagueEntiryDTO.getSummonerName()))
            return false;
        if(!userEntity.getTier().equals(leagueEntiryDTO.getTier()))
            return false;
        return userEntity.getRank().equals(leagueEntiryDTO.getRank());
    }

    public boolean isSameUserInfo(UserEntity userEntity, LeagueItem leagueItem, String tier){
        if(!userEntity.getSummonerName().equals(leagueItem.getSummonerName()))
            return false;
        if(!userEntity.getTier().equals(tier))
            return false;
        return userEntity.getRank().equals(leagueItem.getRank());
    }

    public void userSave(LeagueEntiryDTO leagueEntiryDTO, String puuid){
        UserEntity userEntity = userRepository.findById(leagueEntiryDTO.getSummonerId()).orElse(null);
        if(userEntity == null)
            userRepository.save(new UserEntity(leagueEntiryDTO.getSummonerId(), puuid, leagueEntiryDTO.getSummonerName(), leagueEntiryDTO.getTier(), leagueEntiryDTO.getRank()));
        else if(!isSameUserInfo(userEntity,leagueEntiryDTO)){
            userEntity.setSummonerName(leagueEntiryDTO.getSummonerName());
            userEntity.setTier(leagueEntiryDTO.getTier());
            userEntity.setRank(leagueEntiryDTO.getRank());
            userRepository.save(userEntity);
        }
    }
    public void userSave(LeagueItem leagueItem, String puuid, String tier){
        UserEntity userEntity = userRepository.findById(leagueItem.getSummonerId()).orElse(null);
        if(userEntity == null)
            userRepository.save(new UserEntity(leagueItem.getSummonerId(), puuid, leagueItem.getSummonerName(), tier, leagueItem.getRank()));
        else if(!isSameUserInfo(userEntity,leagueItem,tier)){
            userEntity.setSummonerName(leagueItem.getSummonerName());
            userEntity.setTier(tier);
            userEntity.setRank(leagueItem.getRank());
            userRepository.save(userEntity);
        }
    }

    public void matchSave(String matchId, String puuid, LocalDate localDate){
        UserMatchIdEntity userMatchIdEntity = userMatchIdRepository.findByMatchIdAndPuuid(matchId, puuid);
        if(userMatchIdEntity == null)
            userMatchIdRepository.save(new UserMatchIdEntity(puuid, matchId, localDate));
    }

    public void matchDetailSave(MatchDto matchDto, LocalDate localDate, MatchTimeLineDto matchTimeLineDto){
        MatchDetailEntity matchDetailEntity = matchDetailRepository.findById(matchDto.getMetadata().getMatchId()).orElse(null);
        if(matchDetailEntity == null)
            matchDetailRepository.save(new MatchDetailEntity(matchDto.getMetadata().getMatchId(),localDate,matchDto,matchTimeLineDto));
    }

    public void championInitialDataSave(ChampionDto championDto){
        Set<String> championIdList = championDto.getData().keySet();
        for(String championId : championIdList) {
            ChampionEntity championEntity = championRepository.findById(Long.parseLong(championDto.getData().get(championId).getKey())).orElse(null);
            if(championEntity ==null) {
                championRepository.save(new ChampionEntity(Long.parseLong(championDto.getData().get(championId).getKey()), championDto.getData().get(championId).getName(), championId + ".png"));
                log.info("Champion : " + Long.parseLong(championDto.getData().get(championId).getKey()) + ", " + championDto.getData().get(championId).getName() +" , " + championId+".png" + " 저장");
            }
            else
                log.info(Long.parseLong(championDto.getData().get(championId).getKey()) + "라는 ID가 Champion 테이블에 이미 존재합니다. 저장하지 않습니다");
        }
    }
    public void spellInitialDataSave(SpellDto spellDto){
        Set<String> spellIdList = spellDto.getData().keySet();
        for(String spellId : spellIdList){
            if(Integer.parseInt(spellDto.getData().get(spellId).getKey()) >21)
                continue;
            SpellEntity spellEntity = spellRepository.findById(Long.valueOf(spellDto.getData().get(spellId).getKey())).orElse(null);
            if(spellEntity == null) {
                spellRepository.save(new SpellEntity(Long.parseLong(spellDto.getData().get(spellId).getKey()), spellDto.getData().get(spellId).getName(), spellId + ".png"));
                log.info("Spell : " + Long.parseLong(spellDto.getData().get(spellId).getKey()) + ", " + spellDto.getData().get(spellId).getName() + ", " + spellId + ".png 저장");
            }
            else
                log.info(Long.valueOf(spellDto.getData().get(spellId).getKey()) + "라는 ID가 Spell 테이블에 이미 존재합니다. 저장하지 않습니다.");
        }
    }
    public void perkInitialDataSave(List<PerkDto> perkDtoList){
        perkDtoList.forEach(perkDto -> {
            for(int i = 0; i < perkDto.getSlots().size(); i++){
                List<PerkRune> perkRuneList = perkDto.getSlots().get(i).getRunes();
                for (PerkRune perkRune : perkRuneList) {
                    PerkEntity perkEntity = (perkRepository.findById(perkRune.getId()).orElse(null));
                    if (perkEntity == null) {
                        perkRepository.save(new PerkEntity(perkRune.getId(), perkRune.getName(), perkRune.getIcon()));
                        log.info("Perk : " + perkRune.getId() + ", " + perkRune.getName() + ", " + perkRune.getIcon() + " 저장");
                    }
                }
            }
            PerkEntity perkEntity = perkRepository.findById(perkDto.getId()).orElse(null);
            if(perkEntity == null) {
                perkRepository.save(new PerkEntity(perkDto.getId(), perkDto.getName(), perkDto.getIcon()));
                log.info("Perk : " + perkDto.getId() + ", " +perkDto.getName() + ", " + perkDto.getIcon() +" 저장");
            }
            });
    }
    public void itemInitialDataSave(ItemDto itemDto){
        Set<String> itemIdList = itemDto.getData().keySet();
        for(String itemId : itemIdList){
            ItemEntity itemEntity = itemRepository.findById(Long.parseLong(itemId)).orElse(null);
            if(itemEntity == null) {
                itemRepository.save(new ItemEntity(Long.parseLong(itemId), itemDto.getData().get(itemId).getName(), itemId + ".png"));
                log.info("Item : " + Long.parseLong(itemId) + ", " + itemDto.getData().get(itemId).getName() + ", " + itemId + ".png" + " 저장");
            }
        }
    }
    public void fullItemInitialDataSave(ItemDto itemDto){
        if(itemFullRepository.findAll().size() !=0)
            return;
        Set<String> itemIdList = itemDto.getData().keySet();
        for(String itemId : itemIdList){
            if(itemDto.getData().get(itemId).getInto() == null && itemDto.getData().get(itemId).getGold().getTotal() > 1500L){
                itemFullRepository.save(new ItemFullEntity(Long.parseLong(itemId)));
                log.info("ItemFull : " +Long.parseLong(itemId) +" 저장");
            }
        }
        //신화 추가 부분, 신화는 오른때문에 진화트리가 있어버려서, ddr api만으로는 신화구분이 어려움.따라서 직접 추가.
        itemFullRepository.save(new ItemFullEntity(6630L));
        itemFullRepository.save(new ItemFullEntity(6631L));
        itemFullRepository.save(new ItemFullEntity(6632L));
        itemFullRepository.save(new ItemFullEntity(3078L));
        itemFullRepository.save(new ItemFullEntity(6671L));
        itemFullRepository.save(new ItemFullEntity(6672L));
        itemFullRepository.save(new ItemFullEntity(6673L));
        itemFullRepository.save(new ItemFullEntity(6691L));
        itemFullRepository.save(new ItemFullEntity(6692L));
        itemFullRepository.save(new ItemFullEntity(6693L));
        itemFullRepository.save(new ItemFullEntity(6653L));
        itemFullRepository.save(new ItemFullEntity(6655L));
        itemFullRepository.save(new ItemFullEntity(6656L));
        itemFullRepository.save(new ItemFullEntity(4644L));
        itemFullRepository.save(new ItemFullEntity(3152L));
        itemFullRepository.save(new ItemFullEntity(4633L));
        itemFullRepository.save(new ItemFullEntity(4636L));
        itemFullRepository.save(new ItemFullEntity(6662L));
        itemFullRepository.save(new ItemFullEntity(6664L));
        itemFullRepository.save(new ItemFullEntity(3068L));
        itemFullRepository.save(new ItemFullEntity(2065L));
        itemFullRepository.save(new ItemFullEntity(3190L));
        itemFullRepository.save(new ItemFullEntity(3001L));
        itemFullRepository.save(new ItemFullEntity(4005L));
        itemFullRepository.save(new ItemFullEntity(6617L));
    }
}

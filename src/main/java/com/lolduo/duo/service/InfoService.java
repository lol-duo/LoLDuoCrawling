package com.lolduo.duo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lolduo.duo.entity.clientInfo.*;
import com.lolduo.duo.entity.clientInfo.sub.Item;
import com.lolduo.duo.entity.clientInfo.sub.Perk;
import com.lolduo.duo.entity.clientInfo.sub.Spell;
import com.lolduo.duo.entity.gameInfo.*;
import com.lolduo.duo.repository.clientInfo.*;
import com.lolduo.duo.repository.gameInfo.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class InfoService {
    private final SoloMatchRepository soloMatchRepository;
    private final DoubleMatchRepository doubleMatchRepository;
    private final TripleMatchRepository tripleMatchRepository;
    private final PentaMatchRepository pentaMatchRepository;
    private final SoloCombiRepository soloCombiRepository;
    private final DoubleCombiRepository doubleCombiRepository;
    private final TripleCombiRepository tripleCombiRepository;
    private final PentaCombiRepository pentaCombiRepository;

    public void makeCombiInfo(int number,LocalDate yesterday) {
        log.info("makeCombiInfo-start : " + number);
        ICombiRepository combiRepository = getInfoRepository(number);
        IMatchRepository matchRepository = getMatchRepository(number);
        ObjectMapper objectMapper = new ObjectMapper();

        log.info("before makeCombiInfo - matchRepository.findAll() " + number );
        List<? extends IMatchEntity> matchEntitiyList = matchRepository.findAllByDate(yesterday);
        log.info("makeCombiInfo - matchRepository.findAll() , size : " + matchEntitiyList.size());
        matchEntitiyList.forEach(matchEntity -> {
            ICombiEntity combiEntity = null;
            try {
                combiEntity = combiRepository.findByChampionIdAndPosition(objectMapper.writeValueAsString(matchEntity.getChampionList()), objectMapper.writeValueAsString(matchEntity.getPositionMap())).orElse(null);
            } catch (JsonProcessingException e) {
                log.error("objectMapper writeValue error");
            }
            if (combiEntity == null || combiEntity.getAllCount()==null) {
                log.info("combiEntity is null ");
                combiEntity = getCombiEntity(number);
                Perk perk = new Perk(matchEntity.getPerkListMap(), 1L, 1L);
                Spell spell = new Spell(matchEntity.getSpellListMap(), 1L, 1L);
                Item item = new Item(matchEntity.getItemListMap(), 1L, 1L);
                List<Perk> perkList = new LinkedList<>();
                List<Spell> spellList = new LinkedList<>();
                List<Item> itemList = new LinkedList<>();
                combiEntity.setAllCount(1L);
                combiEntity.setChampionIdList(matchEntity.getChampionList());
                combiEntity.setPositionMap(matchEntity.getPositionMap());
                if (matchEntity.getWin()) {
                    perkList.add(perk);
                    spellList.add(spell);
                    itemList.add(item);
                    combiEntity.setPerkList(perkList);
                    combiEntity.setItemList(itemList);
                    combiEntity.setSpellList(spellList);
                    combiEntity.setWinCount(1L);
                    saveCombiEntity(number,combiEntity);
                }
                else {
                    perk.setWin(0L);
                    spell.setWin(0L);
                    item.setWin(0L);
                    perkList.add(perk);
                    spellList.add(spell);
                    itemList.add(item);
                    combiEntity.setPerkList(perkList);
                    combiEntity.setItemList(itemList);
                    combiEntity.setSpellList(spellList);
                    combiEntity.setWinCount(0L);
                    saveCombiEntity(number,combiEntity);
                }
            }
            else {
                combiEntity.setAllCount(combiEntity.getAllCount()+1);
                if(matchEntity.getWin()) {
                    combiEntity.setWinCount(combiEntity.getWinCount()+1);
                    updateItemList(combiEntity.getItemList(),matchEntity.getItemListMap(),true);
                    updatePerkList(combiEntity.getPerkList(),matchEntity.getPerkListMap(),true);
                    updateSpellList(combiEntity.getSpellList(),matchEntity.getSpellListMap(),true);
                }
                else {
                    updateItemList(combiEntity.getItemList(),matchEntity.getItemListMap(),false);
                    updatePerkList(combiEntity.getPerkList(),matchEntity.getPerkListMap(),false);
                    updateSpellList(combiEntity.getSpellList(),matchEntity.getSpellListMap(),false);
                }
                saveCombiEntity(number,combiEntity);
            }
        });
        log.info("makeCombiInfo-end : " + number);
    }
    public void saveCombiEntity(int number, ICombiEntity iCombiEntity){
        if(number ==1) {
            log.info(number + " combi : "+ "allCount : " +iCombiEntity.getAllCount() + "winCount : " + "Id and position : " +iCombiEntity.getChampionId() + " " + iCombiEntity.getPosition());
            soloCombiRepository.save((SoloCombiEntity)iCombiEntity);
        }
        else if(number ==2) {
            doubleCombiRepository.save((DoubleCombiEntity) iCombiEntity);
        }
        else if(number ==3) {
            tripleCombiRepository.save( (TripleCombiEntity) iCombiEntity);
        }
        else if(number ==5) {
            pentaCombiRepository.save( (PentaCombiEntity) iCombiEntity);
        }
        else {
            log.info("saveCombiEntity - save faliled!");
            return;
        }
    }
    private void updateItemList(List<Item> infoItemList, Map<Long,List<Long>> itemList,boolean win){
        boolean isUpdated = false;
        for(int i = 0 ; i <infoItemList.size();i++){
            Item item = infoItemList.get(i);
            if(item.getItemMap().values().containsAll(itemList.values())){
                if(win)
                    infoItemList.get(i).setWin(item.getWin()+1);
                infoItemList.get(i).setAllCount(item.getAllCount()+1);
                isUpdated =true;
                break;
            }
        }
        if(!isUpdated){
            if(win)
                infoItemList.add(new Item(itemList,1L,1L));
            else
                infoItemList.add(new Item(itemList,0L,1L));
        }
    }

    private void updatePerkList(List<Perk> infoPerkList, Map<Long,List<Long>> perkList,boolean win){
        boolean isUpdated =false;
        for(int i = 0 ; i < infoPerkList.size();i++){
            Perk perk = infoPerkList.get(i);
            if(perk.getPerkMap().values().containsAll(perkList.values())){
                if(win)
                    infoPerkList.get(i).setWin(perk.getWin()+1);
                infoPerkList.get(i).setAllCount(perk.getAllCount()+1);
                isUpdated=true;
                break;
            }
        }
        if(!isUpdated){
            if(win)
                infoPerkList.add(new Perk(perkList,1L,1L));
            else
                infoPerkList.add(new Perk(perkList,0L,1L));

        }
    }
    private void updateSpellList(List<Spell> infoSpellList, Map<Long, TreeSet<Long>> spellList,boolean win){
        boolean isUpdated =false;
        for(int i = 0 ; i < infoSpellList.size();i++){
            Spell spell = infoSpellList.get(i);
            if(spell.getSpellMap().values().containsAll(spellList.values())){
                if(win)
                    infoSpellList.get(i).setWin(spell.getWin()+1);
                infoSpellList.get(i).setAllCount(spell.getAllCount()+1);
                isUpdated=true;
                break;
            }
        }
        if(!isUpdated){
            if(win)
                infoSpellList.add(new Spell(spellList,1L,1L));
            else
                infoSpellList.add(new Spell(spellList,0L,1L));
        }
    }

    private ICombiRepository getInfoRepository(int championCount) {
        if (championCount == 1) {
            log.info("getInfoRepository() - championCount : {}, 1명", championCount);
            return soloCombiRepository;
        }
        else if (championCount == 2) {
            log.info("getInfoRepository() - championCount : {}, 2명", championCount);
            return doubleCombiRepository;
        }
        else if (championCount == 3) {
            log.info("getInfoRepository() - championCount : {}, 3명", championCount);
            return tripleCombiRepository;
        }
        else if (championCount == 5) {
            log.info("getInfoRepository() - championCount : {}, 5명", championCount);
            return pentaCombiRepository;
        }
        else {
            log.info("getInfoRepository() - 요청 문제 발생");
            return null;
        }
    }
    private IMatchRepository getMatchRepository(int championCount) {
        if (championCount == 1) {
            log.info("getMatchRepository() - championCount : {}, 1명", championCount);
            return soloMatchRepository;
        }
        else if (championCount == 2) {
            log.info("getMatchRepository() - championCount : {}, 2명", championCount);
            return doubleMatchRepository;
        }
        else if (championCount == 3) {
            log.info("getMatchRepository() - championCount : {}, 3명", championCount);
            return tripleMatchRepository;
        }
        else if (championCount == 5) {
            log.info("getMatchRepository() - championCount : {}, 5명", championCount);
            return pentaMatchRepository;
        }
        else {
            log.info("getMatchRepository() - 요청 문제 발생");
            return null;
        }
    }
    private ICombiEntity getCombiEntity(int championCount){
        if (championCount == 1) {
            log.info("getMatchRepository() - championCount : {}, 1명", championCount);
            return new SoloCombiEntity();
        }
        else if (championCount == 2) {
            log.info("getMatchRepository() - championCount : {}, 2명", championCount);
            return new DoubleCombiEntity();
        }
        else if (championCount == 3) {
            log.info("getMatchRepository() - championCount : {}, 3명", championCount);
            return new TripleCombiEntity();
        }
        else if (championCount == 5) {
            log.info("getMatchRepository() - championCount : {}, 5명", championCount);
            return new PentaCombiEntity();
        }
        else {
            log.info("getMatchRepository() - 요청 문제 발생");
            return null;
        }
    }
}

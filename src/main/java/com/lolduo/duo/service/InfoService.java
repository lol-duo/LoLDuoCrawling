package com.lolduo.duo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lolduo.duo.entity.clientInfo.*;
import com.lolduo.duo.entity.gameInfo.DuoEntity;
import com.lolduo.duo.entity.gameInfo.QuintetEntity;
import com.lolduo.duo.entity.gameInfo.SoloEntity;
import com.lolduo.duo.entity.gameInfo.TrioEntity;
import com.lolduo.duo.repository.clientInfo.DuoInfoRepository;
import com.lolduo.duo.repository.clientInfo.QuintetInfoRepository;
import com.lolduo.duo.repository.clientInfo.SoloInfoRepository;
import com.lolduo.duo.repository.clientInfo.TrioInfoRepository;
import com.lolduo.duo.repository.gameInfo.DuoRepository;
import com.lolduo.duo.repository.gameInfo.QuintetRepository;
import com.lolduo.duo.repository.gameInfo.SoloRepository;
import com.lolduo.duo.repository.gameInfo.TrioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


import java.util.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class InfoService {
    private final SoloRepository soloRepository;
    private final DuoRepository duoRepository;
    private final TrioRepository trioRepository;
    private final QuintetRepository quintetRepository;
    private final SoloInfoRepository soloInfoRepository;
    private final DuoInfoRepository duoInfoRepository;
    private final TrioInfoRepository trioInfoRepository;
    private final QuintetInfoRepository quintetInfoRepository;


    public void makeQuintetInfo(){
        log.info("makeQuintetInfo-start");
        List<QuintetEntity> quintetEntityList = quintetRepository.findAll();
        log.info("makeDuoInfo - quintetRepository.findAll() end");
        ObjectMapper objectMapper = new ObjectMapper();

        quintetEntityList.forEach(quintetEntity -> {
            ICombinationInfoEntity quintetInfoEntity = null;
            try {
                quintetInfoEntity =  quintetInfoRepository.findByChampionIdAndPosition(objectMapper.writeValueAsString(quintetEntity.getChampionList()),objectMapper.writeValueAsString(quintetEntity.getPositionMap())).orElse(null);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            if(quintetInfoEntity==null){
                Perk perk = new Perk(quintetEntity.getPerkListMap(),1L);
                Spell spell =new Spell(quintetEntity.getSpellListMap(),1L);
                Item item = new Item(quintetEntity.getItemListMap(),1L);
                List<Perk> perkList = new LinkedList<>();
                List<Spell> spellList = new LinkedList<>();
                List<Item> itemList = new LinkedList<>();
                if(quintetEntity.getWin()){
                    perkList.add(perk);
                    spellList.add(spell);
                    itemList.add(item);
                    quintetInfoRepository.save(new QuintetInfoEntity(quintetEntity.getChampionList(),quintetEntity.getPositionMap(),1L,1L,perkList,spellList,itemList));
                }
                else{
                    perk.setWin(0L);
                    spell.setWin(0L);
                    item.setWin(0L);
                    perkList.add(perk);
                    spellList.add(spell);
                    itemList.add(item);
                    quintetInfoRepository.save(new QuintetInfoEntity(quintetEntity.getChampionList(),quintetEntity.getPositionMap(),1L,0L,perkList,spellList,itemList));
                }
            }
            else{
                quintetInfoEntity.setAllCount(quintetInfoEntity.getAllCount()+1);
                if(quintetEntity.getWin()){
                    quintetInfoEntity.setWinCount(quintetInfoEntity.getWinCount()+1);
                    updateItemList(quintetInfoEntity.getItemList(),quintetEntity.getItemListMap());
                    updatePerkList(quintetInfoEntity.getPerkList(),quintetEntity.getPerkListMap());
                    updateSpellList(quintetInfoEntity.getSpellList(),quintetEntity.getSpellListMap());
                }
                log.info("값 존재,수정 후 save: " );
                quintetInfoRepository.save((QuintetInfoEntity) quintetInfoEntity);
            }
        });
        log.info("makeQuintetInfo-end");
    }
    public void makeTrioInfo(){
        log.info("makeTrioInfo-start");
        List<TrioEntity> trioEntityList = trioRepository.findAll();
        log.info("makeDuoInfo - trioRepository.findAll() end");
        ObjectMapper objectMapper = new ObjectMapper();
        trioEntityList.forEach(trioEntity -> {
            ICombinationInfoEntity trioInfoEntity =null;
            try {
                trioInfoEntity = trioInfoRepository.findByChampionIdAndPosition(objectMapper.writeValueAsString(trioEntity.getChampionList()),objectMapper.writeValueAsString(trioEntity.getPositionMap())).orElse(null);
            } catch (JsonProcessingException e) {
                log.error("objectMapper writeValue error");
            }
            if(trioInfoEntity==null){
                Perk perk = new Perk(trioEntity.getPerkListMap(),1L);
                Spell spell =new Spell(trioEntity.getSpellListMap(),1L);
                Item item = new Item(trioEntity.getItemListMap(),1L);
                List<Perk> perkList = new LinkedList<>();
                List<Spell> spellList = new LinkedList<>();
                List<Item> itemList = new LinkedList<>();
                if(trioEntity.getWin()){
                    perkList.add(perk);
                    spellList.add(spell);
                    itemList.add(item);
                    trioInfoRepository.save(new TrioInfoEntity(trioEntity.getChampionList(),trioEntity.getPositionMap(),1L,1L,perkList,spellList,itemList));
                }
                else{
                    perk.setWin(0L);
                    spell.setWin(0L);
                    item.setWin(0L);
                    perkList.add(perk);
                    spellList.add(spell);
                    itemList.add(item);
                    trioInfoRepository.save(new TrioInfoEntity(trioEntity.getChampionList(),trioEntity.getPositionMap(),1L,0L,perkList,spellList,itemList));
                }
            }
            else{
                trioInfoEntity.setAllCount(trioInfoEntity.getAllCount()+1);
                if(trioEntity.getWin()){
                    trioInfoEntity.setWinCount(trioInfoEntity.getWinCount()+1);
                    updateItemList(trioInfoEntity.getItemList(),trioEntity.getItemListMap());
                    updatePerkList(trioInfoEntity.getPerkList(),trioEntity.getPerkListMap());
                    updateSpellList(trioInfoEntity.getSpellList(),trioEntity.getSpellListMap());
                }
                log.info("값 존재,수정 후 save: " );
                trioInfoRepository.save((TrioInfoEntity) trioInfoEntity);
            }
        });
        log.info("makeTrioInfo-end");
    }
    public void makeSoloInfo(){
        log.info("makeSoloInfo-start");
        //solo 날짜 추가하여 진행.
        List<SoloEntity> soloEntityList = soloRepository.findAll();
        soloEntityList.forEach(soloEntity -> {
            SoloInfoEntity soloInfoEntity = soloInfoRepository.findByChampionIdAndPosition(soloEntity.getChampion(),soloEntity.getPosition()).orElse(null);

            Map<Long,List<Long>> perkMap = new HashMap<>();
            perkMap.put(soloEntity.getChampion(),soloEntity.getPerkList());

            Map<Long, TreeSet<Long>> spellMap = new HashMap<>();
            spellMap.put(soloEntity.getChampion(),soloEntity.getSpellList());

            Map<Long, List<Long>> itemMap = new HashMap<>();
            itemMap.put(soloEntity.getChampion(),soloEntity.getItemList());

            if(soloInfoEntity==null){
                Perk perk = new Perk(perkMap, soloEntity.getWin() ? 1L : 0L);
                Spell spell =new Spell(spellMap,soloEntity.getWin() ? 1L : 0L);
                Item item =new Item(itemMap,soloEntity.getWin() ? 1L : 0L);

                List<Perk> perkList = new ArrayList<>();
                List<Spell> spellList = new ArrayList<>();
                List<Item> itemList = new ArrayList<>();

                perkList.add(perk);
                spellList.add(spell);
                itemList.add(item);
                log.info("soloInfo Save : championId = {}, position = {}, AllCount = {}, WinCount = {}", soloEntity.getPosition(), soloEntity.getPosition(), 1,1);
                soloInfoRepository.save(new SoloInfoEntity(soloEntity.getChampion(), soloEntity.getPosition(),1L,soloEntity.getWin() ? 1L : 0L,perkList,spellList,itemList));
            }
            else{
                soloInfoEntity.setAllCount(soloInfoEntity.getAllCount()+1);
                if(soloEntity.getWin()){
                    soloInfoEntity.setWinCount(soloInfoEntity.getWinCount()+1);
                    updateItemList(soloInfoEntity.getItemList(),itemMap);
                    updatePerkList(soloInfoEntity.getPerkList(),perkMap);
                    updateSpellList(soloInfoEntity.getSpellList(),spellMap);
                }
                log.info("값 존재,수정 후 save: " );
                log.info("soloInfo Update : championId = {}, position = {}, AllCount = {}, WinCount = {}", soloInfoEntity.getPosition(), soloInfoEntity.getPosition(), soloInfoEntity.getAllCount(),soloInfoEntity.getWinCount());
                soloInfoRepository.save(soloInfoEntity);
            }
        });
        log.info("makeSoloInfo-end");
    }
    public void makeDuoInfo()  {
        log.info("makeDuoInfo-start");
        List<DuoEntity> duoEntityList = duoRepository.findAll();
        log.info("makeDuoInfo - duoRepository.findAll() end");
        ObjectMapper objectMapper = new ObjectMapper();
        duoEntityList.forEach(duoEntity -> {
            ICombinationInfoEntity duoInfoEntity = null;
            try {
                duoInfoEntity = duoInfoRepository.findByChampionIdAndPosition(objectMapper.writeValueAsString(duoEntity.getChampion()),objectMapper.writeValueAsString(duoEntity.getPosition())).orElse(null);
            } catch (JsonProcessingException e) {
                log.error("objectMapper writeValue error");
            }
            if(duoInfoEntity==null){
                Perk perk =new Perk(duoEntity.getPerkList(),1L);
                Spell spell =new Spell(duoEntity.getSpellList(),1L);
                Item item = new Item(duoEntity.getItemList(),1L);
                List<Perk> perkList = new LinkedList<>();
                List<Spell> spellList = new LinkedList<>();
                List<Item> itemList = new LinkedList<>();
                if(duoEntity.getWin()){
                    perkList.add(perk);
                    spellList.add(spell);
                    itemList.add(item);
                    duoInfoRepository.save(new DuoInfoEntity(duoEntity.getChampion(),duoEntity.getPosition(),1L,1L,perkList,spellList,itemList));
                }
                else{
                    perk.setWin(0L);
                    spell.setWin(0L);
                    item.setWin(0L);
                    perkList.add(perk);
                    spellList.add(spell);
                    itemList.add(item);
                    duoInfoRepository.save(new DuoInfoEntity(duoEntity.getChampion(),duoEntity.getPosition(),1L,0L,perkList,spellList,itemList));
                }
            }
            else{
                duoInfoEntity.setAllCount(duoInfoEntity.getAllCount()+1);
                if(duoEntity.getWin()){
                    duoInfoEntity.setWinCount(duoInfoEntity.getWinCount()+1);
                    updateItemList(duoInfoEntity.getItemList(),duoEntity.getItemList());
                    updatePerkList(duoInfoEntity.getPerkList(),duoEntity.getPerkList());
                    updateSpellList(duoInfoEntity.getSpellList(),duoEntity.getSpellList());
                }
                log.info("값 존재,수정 후 save: " );
                duoInfoRepository.save((DuoInfoEntity) duoInfoEntity);
            }
        });
        log.info("makeDuoInfo-end");
    }



    private void updateItemList(List<Item> infoItemList, Map<Long,List<Long>> itemList){
        boolean isUpdated = false;
        for(int i = 0 ; i <infoItemList.size();i++){
            Item item = infoItemList.get(i);
            if(item.getItemMap().values().containsAll(itemList.values())){
                infoItemList.get(i).setWin(item.getWin()+1);
                isUpdated =true;
                break;
            }
        }
        if(!isUpdated){
            infoItemList.add(new Item(itemList,1L));
        }
    }

    private void updatePerkList(List<Perk> infoPerkList, Map<Long,List<Long>> perkList){
        boolean isUpdated =false;
        for(int i = 0 ; i < infoPerkList.size();i++){
            Perk perk = infoPerkList.get(i);
            if(perk.getPerkMap().values().containsAll(perkList.values())){
                infoPerkList.get(i).setWin(perk.getWin()+1);
                isUpdated=true;
                break;
            }
        }
        if(!isUpdated){
            infoPerkList.add(new Perk(perkList,1L));
        }
    }
    private void updateSpellList(List<Spell> infoSpellList, Map<Long, TreeSet<Long>> spellList){
        boolean isUpdated =false;
        for(int i = 0 ; i < infoSpellList.size();i++){
            Spell spell = infoSpellList.get(i);
            if(spell.getSpellMap().values().containsAll(spellList.values())){
                spell.setWin(spell.getWin()+1);
                infoSpellList.add(i,spell);
                infoSpellList.remove(i+1);
                isUpdated=true;
                break;
            }
        }
        if(!isUpdated){
            infoSpellList.add(new Spell(spellList,1L));
        }
    }
}

package com.lolduo.duo.service;

import com.lolduo.duo.dto.RiotAPI.league_v4.LeagueEntiryDTO;
import com.lolduo.duo.dto.RiotAPI.league_v4.LeagueItem;
import com.lolduo.duo.dto.RiotAPI.league_v4.LeagueListDTO;
import com.lolduo.duo.entity.UserEntity;
import com.lolduo.duo.entity.UserMatchIdEntity;
import com.lolduo.duo.repository.UserMatchIdRepository;
import com.lolduo.duo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class RiotApiSaveService {

    private final UserRepository userRepository;
    private final UserMatchIdRepository userMatchIdRepository;

    public boolean isSameUserInfo(UserEntity userEntity, LeagueEntiryDTO leagueEntiryDTO){
        if(!userEntity.getSummonerName().equals(leagueEntiryDTO.getSummonerName()))
            return false;
        if(!userEntity.getTier().equals(leagueEntiryDTO.getTier()))
            return false;
        if(!userEntity.getRank().equals(leagueEntiryDTO.getRank()))
            return false;
        return true;
    }

    public boolean isSameUserInfo(UserEntity userEntity, LeagueItem leagueItem, String tier){
        if(!userEntity.getSummonerName().equals(leagueItem.getSummonerName()))
            return false;
        if(!userEntity.getTier().equals(tier))
            return false;
        if(!userEntity.getRank().equals(leagueItem.getRank()))
            return false;
        return true;
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
        UserMatchIdEntity userMatchIdEntity = new UserMatchIdEntity(puuid,matchId,localDate);
        userMatchIdRepository.save(userMatchIdEntity);
    }

}

package com.lolduo.duo.service;

import com.lolduo.duo.dto.RiotAPI.league_v4.LeagueItem;
import com.lolduo.duo.dto.RiotAPI.league_v4.LeagueListDTO;
import com.lolduo.duo.entity.UserEntity;
import com.lolduo.duo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RiotApiSaveService {

    private final UserRepository userRepository;

    public boolean isSameUserInfo(UserEntity userEntity, LeagueItem leagueItem, String tier){
        if(!userEntity.getSummonerName().equals(leagueItem.getSummonerName()))
            return false;
        if(!userEntity.getTier().equals(tier))
            return false;
        if(!userEntity.getRank().equals(leagueItem.getRank()))
            return false;
        return true;
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


}

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

    public boolean isSameUserInfo(UserEntity userEntity, LeagueItem leagueItem, String league){
        if(!userEntity.getSummonerId().equals(leagueItem.getSummonerId()))
            return false;
        if(!userEntity.getSummonerName().equals(leagueItem.getSummerName()))
            return false;
        if(!userEntity.getTier().equals(league))
            return false;
        return true;
    }

    public void userSave(LeagueItem leagueItem, String puuid, String league){
        UserEntity userEntity = userRepository.findById(puuid).orElse(null);

        if(userEntity == null){
            userEntity = new UserEntity(puuid,leagueItem.getSummerName(),leagueItem.getSummonerId(), league);
            userRepository.save(userEntity);
        }
        else{
            if(isSameUserInfo(userEntity,leagueItem,league)){

            }
        }
    }


}

package com.lolduo.duo.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@NoArgsConstructor
@Table(name = "user")
@Getter
public class UserEntity {
    @Id
    @Column(name = "puuid")
    private String puuid;

    @Column(name = "summoner_name")
    private String summonerName;

    @Column(name = "summoner_id")
    private String summonerId;

    @Column(name = "tier")
    private String tier;

    public UserEntity(String puuid, String summonerName, String summonerId, String tier) {
        this.puuid = puuid;
        this.summonerName = summonerName;
        this.summonerId = summonerId;
        this.tier = tier;
    }

    public void setSummonerId(String summonerId) {
        this.summonerId = summonerId;
    }

    public void setSummonerName(String summonerName) {
        this.summonerName = summonerName;
    }

    public void setTier(String tier) {
        this.tier = tier;
    }
}

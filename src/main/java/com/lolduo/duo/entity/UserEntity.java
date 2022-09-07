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
    @Column(name = "summoner_id")
    private String summonerId;
    @Column(name = "puuid")
    private String puuid;

    @Column(name = "summoner_name")
    private String summonerName;
    @Column(name = "tier")
    private String tier;

    @Column(name = "rank")
    private String rank;

    public UserEntity(String summonerId, String puuid, String summonerName, String tier, String rank) {
        this.summonerId = summonerId;
        this.puuid = puuid;
        this.summonerName = summonerName;
        this.tier = tier;
        this.rank = rank;
    }

    public void setSummonerName(String summonerName) {
        this.summonerName = summonerName;
    }

    public void setTier(String tier) {
        this.tier = tier;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }
}

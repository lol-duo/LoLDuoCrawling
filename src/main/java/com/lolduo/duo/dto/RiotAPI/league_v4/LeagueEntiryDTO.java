package com.lolduo.duo.dto.RiotAPI.league_v4;

import lombok.Getter;

@Getter
public class LeagueEntiryDTO {
    private String leagueId;
    private String summonerId;
    private String summonerName;
    private String queueType;
    private String tier;
    private String rank;
    private Long leaguePoints;
    private Long wins;
    private Long losses;
    private Boolean veteran;
    private Boolean inactive;
    private Boolean freshBlood;
    private Boolean hotStreak;
    private MiniSeries miniSeries;
}

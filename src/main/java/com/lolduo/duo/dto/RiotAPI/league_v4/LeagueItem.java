package com.lolduo.duo.dto.RiotAPI.league_v4;

import lombok.Getter;

@Getter
public class LeagueItem {
    private Boolean freshBlood;
    private Long wins;
    private String summonerName;
    private MiniSeries miniSeries;
    private Boolean inactive;
    private Boolean veteran;
    private Boolean hotStreak;
    private String rank;
    private Long leaguePoints;
    private Long losses;
    private String summonerId;

}

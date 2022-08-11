package com.lolduo.duo.entity.match_v5;

import lombok.Getter;

import java.util.List;

@Getter
public class Metadata {
    private String dataVersion;
    private String matchId;
    private List<String> participants;

}

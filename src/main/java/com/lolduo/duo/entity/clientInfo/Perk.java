package com.lolduo.duo.entity.clientInfo;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
@Getter
@NoArgsConstructor
public class Perk {
    private Map<Long, List<Long>> perkMap;
    private Long win;
    public void setWin(Long win) {
        this.win = win;
    }

    public Perk(Map<Long, List<Long>> perkMap, Long win) {
        this.perkMap = perkMap;
        this.win = win;
    }
}

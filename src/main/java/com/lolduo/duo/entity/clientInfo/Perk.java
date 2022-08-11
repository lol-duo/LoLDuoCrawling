package com.lolduo.duo.entity.clientInfo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
@Getter
@NoArgsConstructor
@Setter
public class Perk implements Serializable {
    private Map<Long, List<Long>> perkMap;
    private Long win;
    public Perk(Map<Long, List<Long>> perkMap, Long win) {
        this.perkMap = perkMap;
        this.win = win;
    }
}

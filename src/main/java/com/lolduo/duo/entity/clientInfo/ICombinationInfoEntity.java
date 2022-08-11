package com.lolduo.duo.entity.clientInfo;

import java.util.List;
import java.util.Map;
import java.util.TreeSet;

public interface ICombinationInfoEntity {
    Long getId();
    TreeSet<Long> getChampionId();
    Map<Long, String> getPosition();
    Long getAllCount();
    Long getWinCount();
    List<Perk> getPerkList();
    List<Spell> getSpellList();
    List<Item> getItemList();

    void setAllCount(Long allCount);
    void setWinCount(Long winCount);
}

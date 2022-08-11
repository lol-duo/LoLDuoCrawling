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

    void setId(Long id);
    void setChampionId(TreeSet<Long> championId);
    void setPosition(Map<Long, String> position);
    void setAllCount(Long allCount);
    void setWinCount(Long winCount);
    void setPerkList(List<Perk> perkList);
    void setSpellList(List<Spell> spellList);
    void setItemList(List<Item> itemList);
}

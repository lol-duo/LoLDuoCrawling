package com.lolduo.duo.entity.clientInfo;

import com.fasterxml.jackson.databind.JsonNode;
import com.lolduo.duo.entity.clientInfo.sub.Item;
import com.lolduo.duo.entity.clientInfo.sub.Perk;
import com.lolduo.duo.entity.clientInfo.sub.Spell;
import com.vladmihalcea.hibernate.type.json.JsonType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.util.*;

@Entity
@NoArgsConstructor
@Table(name = "solo_combi")
@Getter
@TypeDef(name = "json", typeClass = JsonType.class,defaultForType = JsonNode.class)
public class SoloCombiEntity implements ICombiEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Type(type = "json")
    @Column(name = "champion_id",columnDefinition = "json")
    private TreeSet<Long> championId = new TreeSet<>();

    @Type(type = "json")
    @Column(name = "position", columnDefinition = "json")
    private Map<Long, String> position = new HashMap<>();

    @Column(name = "all_count")
    private Long allCount;

    @Column(name = "win_count")
    private Long winCount;

    @Type(type = "json")
    @Column(name = "perk_list", columnDefinition = "json")
    private List<Perk> perkList = new ArrayList<>();

    @Type(type = "json")
    @Column(name = "spell_list", columnDefinition = "json")
    private List<Spell> spellList = new ArrayList<>();

    @Type(type = "json")
    @Column(name = "item_list", columnDefinition = "json")
    private List<Item> itemList;

    @Override
    public void setAllCount(Long allCount) {
        this.allCount = allCount;
    }

    @Override
    public void setWinCount(Long winCount) {
        this.winCount = winCount;
    }

    @Override
    public void setPerkList(List<Perk> perkList) {
        this.perkList = perkList;
    }
    @Override
    public void setItemList(List<Item> itemList) {
        this.itemList = itemList;
    }
    @Override
    public void setSpellList(List<Spell> spellList) {
        this.spellList =spellList;
    }
    public void setChampionIdList(TreeSet<Long> championId) {
        this.championId = championId;
    }

    public void setPositionMap(Map<Long, String> position) {
        this.position = position;
    }
}

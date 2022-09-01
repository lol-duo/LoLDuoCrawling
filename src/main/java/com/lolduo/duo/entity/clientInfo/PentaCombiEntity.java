package com.lolduo.duo.entity.clientInfo;

import com.fasterxml.jackson.databind.JsonNode;
import com.lolduo.duo.entity.clientInfo.sub.Item;
import com.lolduo.duo.entity.clientInfo.sub.Perk;
import com.lolduo.duo.entity.clientInfo.sub.Spell;
import com.vladmihalcea.hibernate.type.json.JsonType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

@Entity
@NoArgsConstructor
@Table(name = "penta_combi", indexes = @Index(name = "idx_pos_champ_win_all", columnList = "position, champion_id, win_count, all_count"))
@Getter
@TypeDef(name = "json", typeClass = JsonType.class,defaultForType = JsonNode.class)
public class PentaCombiEntity implements Serializable, ICombiEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Type(type = "json")
    @Column(name = "champion_id",columnDefinition = "varchar(50)")
    private TreeSet<Long> championId;

    @Type(type = "json")
    @Column(name = "position", columnDefinition = "varchar(100)")
    private Map<Long, String> position;

    @Column(name = "perk_myth_item")
    private String perkMythItem;

    @Column(name = "all_count")
    private Long allCount;

    @Column(name = "win_count")
    private Long winCount;

    @Type(type = "json")
    @Column(name = "perk_list", columnDefinition = "json")
    private List<Perk> perkList;

    @Type(type = "json")
    @Column(name = "spell_list", columnDefinition = "json")
    private List<Spell> spellList;

    @Type(type = "json")
    @Column(name = "item_list", columnDefinition = "json")
    private List<Item> itemList;

    public PentaCombiEntity(TreeSet<Long> championId, Map<Long, String> position, String perkMythItem, Long allCount, Long winCount, List<Perk> perkList, List<Spell> spellList, List<Item> itemList) {
        this.championId = championId;
        this.position = position;
        this.perkMythItem = perkMythItem;
        this.allCount = allCount;
        this.winCount = winCount;
        this.perkList = perkList;
        this.spellList = spellList;
        this.itemList = itemList;
    }

    @Override
    public void setAllCount(Long allCount) {
        this.allCount = allCount;
    }

    @Override
    public void setWinCount(Long winCount) {
        this.winCount = winCount;
    }
}

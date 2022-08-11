package com.lolduo.duo.entity.clientInfo;

import com.fasterxml.jackson.databind.JsonNode;
import com.vladmihalcea.hibernate.type.json.JsonType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

@Entity
@NoArgsConstructor
@Table(name = "solo_info")
@Setter
@Getter
@TypeDef(name = "json", typeClass = JsonType.class,defaultForType = JsonNode.class)
public class SoloInfoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "champion_id")
    private Long championId;
    @Column(name = "position")
    private String position;
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

    public SoloInfoEntity(Long championId, String position, Long allCount, Long winCount, List<Perk> perkList, List<Spell> spellList, List<Item> itemList) {
        this.championId = championId;
        this.position = position;
        this.allCount = allCount;
        this.winCount = winCount;
        this.perkList = perkList;
        this.spellList = spellList;
        this.itemList = itemList;
    }
}

package com.lolduo.duo.entity.gameInfo;

import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.util.List;
import java.util.TreeSet;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "solo")
@TypeDef(name = "json", typeClass = JsonStringType.class)
public class SoloEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "tier")
    private String tier;

    @Column(name = "win")
    private Boolean win;

    @Column(name = "position")
    private String position;

    @Type(type = "json")
    @Column(name = "item_list", columnDefinition = "json")
    private List<Long> itemList;

    @Type(type = "json")
    @Column(name = "spell_list", columnDefinition = "json")
    private TreeSet<Long> spellList;
    @Column(name = "champion")
    private Long champion;

    @Type(type = "json")
    @Column(name = "perk_list", columnDefinition = "json")
    private List<Long> perkList;

    public SoloEntity(String tier, Boolean win, String position, List<Long> itemList, TreeSet<Long> spellList, Long champion, List<Long> perkList) {
        this.tier = tier;
        this.win = win;
        this.position = position;
        this.itemList = itemList;
        this.spellList = spellList;
        this.champion = champion;
        this.perkList = perkList;
    }
}

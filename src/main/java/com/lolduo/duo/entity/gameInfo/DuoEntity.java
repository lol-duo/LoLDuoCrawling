package com.lolduo.duo.entity.gameInfo;

import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "duo")
@TypeDef(name = "json", typeClass = JsonStringType.class)
public class DuoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "tier")
    private String tier;

    @Column(name = "win")
    private Boolean win;

    @Type(type = "json")
    @Column(name = "position", columnDefinition = "json")
    private Map<Long, String> position;

    @Type(type = "json")
    @Column(name = "item_list", columnDefinition = "json")
    private Map<Long,List<Long>> itemList;

    @Type(type = "json")
    @Column(name = "spell_list", columnDefinition = "json")
    private Map<Long,TreeSet<Long>> spellList;
    @Type(type = "json")
    @Column(name = "champion",columnDefinition = "json")
    private TreeSet<Long> champion;

    @Type(type = "json")
    @Column(name = "perk_list", columnDefinition = "json")
    private Map<Long,List<Long>> perkList;

    public DuoEntity(String tier, Boolean win, Map<Long, String> position, Map<Long, List<Long>> itemList, Map<Long, TreeSet<Long>> spellList, TreeSet<Long> champion, Map<Long, List<Long>> perkList) {
        this.tier = tier;
        this.win = win;
        this.position = position;
        this.itemList = itemList;
        this.spellList = spellList;
        this.champion = champion;
        this.perkList = perkList;
    }
}

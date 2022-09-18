package com.lolduo.duo.entity.InitialDataEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "champion")
public class ChampionEntity {
    @Id
    private Long id;
    @Column
    private String name;
    @Column
    private String imgUrl;
    public ChampionEntity(Long id, String name, String imgUrl) {
        this.id = id;
        this.name = name;
        this.imgUrl = imgUrl;
    }
}

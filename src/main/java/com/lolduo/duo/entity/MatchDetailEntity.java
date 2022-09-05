package com.lolduo.duo.entity;

import com.lolduo.duo.dto.RiotAPI.match_v5.MatchDto;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@NoArgsConstructor
@Table(name = "match_detail")
@Getter
@TypeDef(name = "json", typeClass = JsonStringType.class)
public class MatchDetailEntity {
    @Id
    @Column(name = "match_id")
    private String matchId;

    @Column(name = "date")
    private LocalDate date;

    @Type(type = "json")
    @Column(name = "match_info", columnDefinition = "json")
    private MatchDto matchInfo;

    public MatchDetailEntity(String matchId, LocalDate date, MatchDto matchInfo) {
        this.matchId = matchId;
        this.date = date;
        this.matchInfo = matchInfo;
    }
}

package com.lolduo.duo.entity;


import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@NoArgsConstructor
@Table(name = "user_match")
@Getter
public class UserMatchIdEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "puuid")
    private String puuid;

    @Column(name = "match_id")
    private String matchId;

    @Column(name = "date")
    private LocalDate date;

    public UserMatchIdEntity(String puuid, String matchId, LocalDate date) {
        this.puuid = puuid;
        this.matchId = matchId;
        this.date = date;
    }
}

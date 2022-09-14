package com.lolduo.duo.data;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class NowLocalDate {
    Long startTime;
    Long endTime;
    LocalDate localDate;

    public NowLocalDate(Long startTime, Long endTime, LocalDate localDate) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.localDate = localDate;
    }
}

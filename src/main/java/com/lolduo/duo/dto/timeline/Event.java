package com.lolduo.duo.dto.timeline;

import lombok.Getter;

@Getter
public class Event {
    private Long itemId;
    private Long participantId;
    private String type;
}

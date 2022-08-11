package com.lolduo.duo.dto.timeline;

import lombok.Getter;

import java.util.List;

@Getter
public class Info {
    List<Frame> frames;
    List<Participant> participants;
}

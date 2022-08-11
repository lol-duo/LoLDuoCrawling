package com.lolduo.duo.entity.clientInfo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Convert;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Getter
@NoArgsConstructor
@Setter
public class Item implements Serializable {
    private Map<Long, List<Long>> ItemMap;
    private Long win;
    public Item(Map<Long, List<Long>> itemMap, Long win) {
        ItemMap = itemMap;
        this.win = win;
    }
}

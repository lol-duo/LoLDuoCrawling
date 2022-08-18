package com.lolduo.duo.service.temp;

import java.util.ArrayList;
import java.util.List;

public class MythItemList {
    private static List<Long> mythItemList;

    public static List<Long> getMythItemList() {
        if (mythItemList == null) {
            mythItemList = new ArrayList<>();
            // 전사
            mythItemList.add(6630L);
            mythItemList.add(6631L);
            mythItemList.add(6632L);
            mythItemList.add(3078L);
            // 원거리 딜러
            mythItemList.add(6671L);
            mythItemList.add(6672L);
            mythItemList.add(6673L);
            // 암살자
            mythItemList.add(6691L);
            mythItemList.add(6692L);
            mythItemList.add(6693L);
            // 마법사
            mythItemList.add(6653L);
            mythItemList.add(6655L);
            mythItemList.add(6656L);
            mythItemList.add(4644L);
            mythItemList.add(3152L);
            mythItemList.add(4633L);
            mythItemList.add(4636L);
            // 탱커
            mythItemList.add(6662L);
            mythItemList.add(6664L);
            mythItemList.add(3068L);
            // 서포터
            mythItemList.add(2065L);
            mythItemList.add(3190L);
            mythItemList.add(3001L);
            mythItemList.add(4005L);
            mythItemList.add(6617L);
        }
        return mythItemList;
    }

    public static void freeList() {
        mythItemList = null;
    }
}
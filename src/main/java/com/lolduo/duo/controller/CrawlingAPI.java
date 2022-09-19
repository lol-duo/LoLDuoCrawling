package com.lolduo.duo.controller;

import com.lolduo.duo.service.RiotService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class CrawlingAPI {
    private final RiotService riotService;

    @PostMapping("/saveMatchDetail")
    @ApiOperation(value ="하루치의 match detail 저장한다.", notes = "챌린저 ~ 다이아까지의 user 정보 및 해당 유저들이 플레이한 match data를 저장한다.",response = String.class)
    public ResponseEntity<?> saveMatchDetail() {
        return riotService.settingPackage();
    }
    /*
    requstparam
    startTime
    endTime
    versionNumber //분류만을 위한 숫자임.
    tier
     */

    @PostMapping("/settingInitialData")
    @ApiOperation(value ="DDR 에서 기본적인 정보들을 저장한다.", notes = "champion,perk,item,itemFull,spell 데이터를 저장한다.",response = String.class)
    public ResponseEntity<?> settingInitialData(@RequestBody String version) {
        return riotService.settingInitialData(version);
    }
}

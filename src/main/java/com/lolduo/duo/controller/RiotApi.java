package com.lolduo.duo.controller;


import com.lolduo.duo.dto.KeyDto;
import com.lolduo.duo.dto.VersionDto;
import com.lolduo.duo.service.RiotService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RiotApi {

    private final RiotService riotService;
    @PostMapping("/setKey")
    @ApiOperation(value ="riot key 세팅", notes = "기존의 riot key값을 변경한다.")
    @ApiResponses({
            @ApiResponse(code =200, message = "키 값이 정상적으로 세팅되었습니다.")
    })

    public void setKey(@RequestBody KeyDto key){
        riotService.setKey(key.getKey());
    }
    @PostMapping("/setVersion")
    @ApiOperation(value ="version 세팅", notes = "라이엇 패치 버전을 변경한다.")
    @ApiResponses({
            @ApiResponse(code =200, message = "버전이 정상적으로 변경되었습니다.")
    })
    public void setVersion(@RequestBody VersionDto version){
        riotService.setVersion(version.getVersion());
    }
}

package com.lolduo.duo.controller;

import com.lolduo.duo.dto.client.ChampionInfoDTO;
import com.lolduo.duo.service.ClientService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
@CrossOrigin(originPatterns = "*")
@RestController
@RequiredArgsConstructor
public class ClientApi {
    private final ClientService clientService;
    
    @GetMapping("/getChampionList")
    @ApiOperation(value ="챔피언 리스트 반환", notes = "챔피언의 챔피언 id, 이름에 대한 정보를 제공한다.")
    public ResponseEntity<?> getChampionList() {
        return clientService.getChampionList();
    }

    @PostMapping("/getInfo")
    public ResponseEntity<?> getInfo(@RequestBody ArrayList<ChampionInfoDTO> championInfoDTOList){
        return clientService.getChampionInfoList(championInfoDTOList);
    }
}

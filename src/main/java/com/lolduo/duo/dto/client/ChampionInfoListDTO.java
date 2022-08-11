package com.lolduo.duo.dto.client;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
@Getter
@NoArgsConstructor
public class ChampionInfoListDTO implements Comparable<ChampionInfoListDTO>{
    private List<ClientChampionInfoDTO> clientChampionInfoDTOList;
    @ApiModelProperty(example = "58.7%")
    private String winRate;

    public ChampionInfoListDTO(List<ClientChampionInfoDTO> clientChampionInfoDTOList, String winRate) {
        this.clientChampionInfoDTOList = clientChampionInfoDTOList;
        this.winRate = winRate;
    }

    @Override
    public int compareTo(ChampionInfoListDTO championInfoListDTO) {
        int a = Double.valueOf(this.winRate.substring(0,this.winRate.length()-1)).intValue();
        int b= Double.valueOf(championInfoListDTO.getWinRate().substring(0, championInfoListDTO.getWinRate().length()-1)).intValue();
        if(b < a)
            return -1;
        else if(a==b)
            return 0;
        else
            return 1;
    }
}

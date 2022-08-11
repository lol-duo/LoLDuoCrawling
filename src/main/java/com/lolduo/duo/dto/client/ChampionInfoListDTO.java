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

    @ApiModelProperty(example = "1,000게임")
    private String allCount;
    public ChampionInfoListDTO(List<ClientChampionInfoDTO> clientChampionInfoDTOList, String winRate,String allCount) {
        this.clientChampionInfoDTOList = clientChampionInfoDTOList;
        this.winRate = winRate;
        this.allCount = allCount;
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

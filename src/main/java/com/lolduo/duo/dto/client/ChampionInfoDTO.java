package com.lolduo.duo.dto.client;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChampionInfoDTO {
    @ApiModelProperty(example = "1")
    private Long championId;
    @ApiModelProperty(example = "TOP | MIDDLE | JUNGLE | BOTTOM | UTILITY")
    private String position;

    public ChampionInfoDTO(Long championId, String position) {
        this.championId = championId;
        this.position = position;
    }
}

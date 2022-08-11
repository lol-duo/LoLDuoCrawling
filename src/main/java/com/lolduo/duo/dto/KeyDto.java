package com.lolduo.duo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

@Getter
public class KeyDto {
    @ApiModelProperty(example = "RGAPI-286c8d2b-d4eb-45a5-a34e-a81d2d4dbfca")
    private String key;
}

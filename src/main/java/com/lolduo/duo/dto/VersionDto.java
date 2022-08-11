package com.lolduo.duo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

@Getter
public class VersionDto {
    @ApiModelProperty(example = "12.13.1")
    private String version;
}

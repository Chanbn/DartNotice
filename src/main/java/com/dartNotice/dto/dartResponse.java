package com.dartNotice.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class dartResponse {
    private String status;
    private String message;
    @JsonProperty("total_count")
    private int totalCount;
    private List<dartItem> list;

}

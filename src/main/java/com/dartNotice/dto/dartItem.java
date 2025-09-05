package com.dartNotice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class dartItem {

    @JsonProperty("corp_name")
    private String corpName;     // 회사명

    @JsonProperty("report_nm")
    private String reportNm;     // 공시명

    @JsonProperty("rcept_no")
    private String rceptNo;      // 접수번호 (고유키 역할)

    @JsonProperty("rcept_dt")
    private String rceptDt;      // 접수일자 (YYYYMMDD)
}

package com.dartNotice.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "dart_report")
public class Notice {
	@Id
	@Column(name = "recpt_no", nullable = false, unique = true)
	private String recptNo; //공시접수번호
	
	@Column(name = "corp_name")
	private String corpName;
	
	@Column(name = "repotr_nm")
	private String reportNm;
	
	@Column(name = "recpt_dt")
	private LocalDate recptDt;
		
	}
 
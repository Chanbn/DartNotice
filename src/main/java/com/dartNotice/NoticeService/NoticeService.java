package com.dartNotice.NoticeService;

import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.hibernate.engine.jdbc.env.spi.AnsiSqlKeywords;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.dartNotice.dto.dartItem;
import com.dartNotice.dto.dartResponse;
import com.dartNotice.model.Notice;
import com.dartNotice.repository.NoticeRepository;

@Service
public class NoticeService {

	private final NoticeRepository noticeRepository;
	private final String DART_API_KEY="08ed36abbf5655d629c36bc824926763e50cb94b";
	private final String API_URL = "https://opendart.fss.or.kr/api/list.json";

	private final RestTemplate restTemplate = new RestTemplate();
	
	private final List<String> Keywords = Arrays.asList("단일판매", "공급계약", "자산양수도", "출자", "투자판단", "공장신설");
	private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
	
	public NoticeService(NoticeRepository noticeRepository) {
		this.noticeRepository=noticeRepository;
	}	
	
	public Page<Notice> getPageNotices(int page, int size){
		Pageable pageable = PageRequest.of(page, size,Sort.by("recptDt").descending());
		return noticeRepository.findAll(pageable);
	}
	
	@Scheduled(fixedRate = 3600000)
	public void saveReports() {

		List<dartItem> items = getReports();
		List<String> recptNoFromApi = items.stream()
				.map(dartItem::getRceptNo)
				.collect(Collectors.toList());
		
		List<Notice> existsRecpt = noticeRepository.findByRecptNoIn(recptNoFromApi); 
		Set<String> existingRecptNos = existsRecpt.stream()
				.map(Notice::getRecptNo)
				.collect(Collectors.toSet());
		
		for(dartItem item : items ) { 
			if(!existingRecptNos.contains(item.getRceptNo()))
			{Notice notice = new Notice();
			notice.setRecptNo(item.getRceptNo());
			notice.setCorpName(item.getCorpName());
			notice.setReportNm(item.getReportNm());
            LocalDate date = LocalDate.parse(item.getRceptDt(),formatter);
            notice.setRecptDt(date);
            noticeRepository.save(notice);
			}
		}
	}
	
	public List<dartItem> getReports(){	
		URI url = UriComponentsBuilder.fromUriString(API_URL)
                .queryParam("crtfc_key", DART_API_KEY)
                .queryParam("page_no", 1)
                .queryParam("page_count", 100)
                .build()
                .toUri();
		dartResponse response = restTemplate.getForObject(url, dartResponse.class);
		if(response==null||!"000".equals(response.getStatus())) {
			throw new RuntimeException("API 호출 실패 또는 오류 상태");
		}
		return response.getList().stream()
				.filter(item -> Keywords.stream().anyMatch(k -> item.getReportNm().contains(k)))
				.collect(Collectors.toList());
	}
	
	public List<dartItem> getFilteredReports(String startDate, String endDate){
		URI url = UriComponentsBuilder.fromUriString(API_URL)
                .queryParam("crtfc_key", DART_API_KEY)
                .queryParam("bgn_de", startDate)
                .queryParam("end_de", endDate)
                .queryParam("page_no", 1)
                .queryParam("page_count", 100)
                .build()
                .toUri();
		dartResponse response = restTemplate.getForObject(url, dartResponse.class);
		if(response==null||!"000".equals(response.getStatus())) {
			throw new RuntimeException("API 호출 실패 또는 오류 상태");
		}
		
		return response.getList().stream()
				.filter(item -> Keywords.stream().anyMatch(k -> item.getReportNm().contains(k)))
				.collect(Collectors.toList());
	}
	
	public List<Notice> getAllNotices(){
		return noticeRepository.findAll();
	}
	
	
	public void initSaveReports() {
		LocalDate start = LocalDate.of(2020, 1, 1);
		LocalDate end = LocalDate.now();
		DateTimeFormatter fomatter = DateTimeFormatter.ofPattern("yyyyMMdd");
		
		while(!start.isAfter(end)) {
			
			LocalDate periodEnd = start.plusMonths(3).minusDays(1);
			if(periodEnd.isAfter(end)) {
				periodEnd = end;
			}
			
			String startDateStr = start.format(fomatter);
			String endDateStr = periodEnd.format(fomatter);
			
			System.out.println("▶ 저장 중: " + startDateStr + " ~ " + endDateStr);
			
			List<dartItem> items = getFilteredReports(startDateStr, endDateStr);
			saveToDb(items);
			System.out.println("✅ 저장 완료: " + startDateStr + " ~ " + endDateStr);
			
			start = periodEnd.plusDays(1);
		}
		
	}
	
	private void saveToDb(List<dartItem> items) {
	    List<String> recptNoFromApi = items.stream()
	            .map(dartItem::getRceptNo)
	            .collect(Collectors.toList());

	    List<Notice> existsRecpt = noticeRepository.findByRecptNoIn(recptNoFromApi);
	    Set<String> existingRecptNos = existsRecpt.stream()
	            .map(Notice::getRecptNo)
	            .collect(Collectors.toSet());

	    List<Notice> newNotices = new ArrayList<>();
	    for (dartItem item : items) {
	        if (!existingRecptNos.contains(item.getRceptNo())) {
	            Notice notice = new Notice();
	            notice.setRecptNo(item.getRceptNo());
	            notice.setCorpName(item.getCorpName());
	            notice.setReportNm(item.getReportNm());
	            LocalDate date = LocalDate.parse(item.getRceptDt(),formatter);
	            notice.setRecptDt(date);
	            newNotices.add(notice);
	        }
	    }
	    noticeRepository.saveAll(newNotices);
	}


}

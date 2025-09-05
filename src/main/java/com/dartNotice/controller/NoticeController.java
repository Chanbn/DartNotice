package com.dartNotice.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dartNotice.NoticeService.NoticeService;
import com.dartNotice.dto.dartItem;
import com.dartNotice.model.Notice;

@Controller
public class NoticeController {

	private final NoticeService noticeService;
	public NoticeController(NoticeService noticeService) {
		this.noticeService = noticeService;
	}
	
	@GetMapping("/init/save")
	public String initReports() {
		noticeService.initSaveReports();
		return "dart-result";
	}
	
	@GetMapping("/notices")
	public String getNotices(@RequestParam(name="page", defaultValue = "0") int page, @RequestParam(name="size", defaultValue = "10") int size, Model model) {
		Page<Notice> noticePage = noticeService.getPageNotices(page, size);
		
		model.addAttribute("notices",noticePage.getContent());
		model.addAttribute("currentPage",page);
		model.addAttribute("totalPages",noticePage.getTotalPages());
		return "notices";
	}
	
	
	@GetMapping("/notice/reports")
	public String getReports(Model model){
		List<dartItem> reports =  noticeService.getReports();
		
		model.addAttribute("reports", reports);
		return "dart-result";
	}
	

	
}

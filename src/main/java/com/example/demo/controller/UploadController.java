package com.example.demo.controller;

import com.example.demo.service.UploadService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@RestController
public class UploadController {
	
	private final UploadService uploadService;
	
	public UploadController(UploadService uploadService) {
		this.uploadService = uploadService;
	}

	@PostMapping("/files/upload_interactive")
	public Collection<List<String>> upload(@RequestParam("file") MultipartFile file) throws Exception{
		return uploadService.upload(file).values();
	}
}

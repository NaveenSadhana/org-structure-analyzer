package com.example.analyzer.controller;

import com.example.analyzer.service.EmployeeAnalyzerService;
import com.example.analyzer.validator.CSVValidator;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api")
public class EmployeeController {

    @Autowired
    private EmployeeAnalyzerService analyzer;

    @Autowired
    private CSVValidator csvValidator;

    @PostMapping("/upload")
    public Object uploadCSV(@RequestParam(value = "file", required = false) MultipartFile file) {
        try {
            File csvFile;
            if (file != null && !file.isEmpty()) {
                log.info("Received file: {}", file.getOriginalFilename());
                csvValidator.validateCSV(file);
                csvFile = File.createTempFile("uploaded-", ".csv");
                file.transferTo(csvFile);
                log.info("File uploaded and saved as: {}", csvFile.getAbsolutePath());
            } else {
                log.warn("No file uploaded, using default CSV file");
                csvFile = new ClassPathResource("employees.csv").getFile();
            }
            MDC.put("file_name", csvFile.getName());
            Map<String, List<String>> analysisReport = analyzer.readCSV(csvFile.getAbsolutePath());
            log.info("CSV file processed successfully");
            return ResponseEntity.ok(analysisReport);
        } catch (IOException e) {
            log.error("Failed to process CSV file: {}", e.getMessage());
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", Collections.singletonList("Failed to process CSV file.")));
        }
    }
}
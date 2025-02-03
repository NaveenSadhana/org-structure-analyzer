package com.example.analyzer.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeAnalyzerServiceTest {

    private EmployeeAnalyzerService service;

    @BeforeEach
    void setUp() {
        service = new EmployeeAnalyzerService();
    }

    @Test
    void readCSV_shouldReadCSVFileCorrectly() throws IOException {
        String filePath = "src/test/resources/employees.csv";
        Map<String, List<String>> results = service.readCSV(filePath);

        assertNotNull(results);
        assertFalse(results.isEmpty());
    }

    @Test
    void readCSV_shouldHandleEmptyCSVFile() throws IOException {
        String filePath = "src/test/resources/empty.csv";
        Map<String, List<String>> results = service.readCSV(filePath);

        assertNotNull(results);
        assertTrue(results.get("underpaidManagers").isEmpty());
        assertTrue(results.get("overpaidManagers").isEmpty());
        assertTrue(results.get("excessiveDepthEmployees").isEmpty());
    }

    @Test
    void readCSV_shouldHandleCSVFileWithMissingManagerId() throws IOException {
        String filePath = "src/test/resources/employees.csv";
        Map<String, List<String>> results = service.readCSV(filePath);

        assertNotNull(results);
        assertFalse(results.isEmpty());
        assertNotNull(service.getCeo());
    }

    @Test
    void getAnalysisResults_shouldIdentifyUnderpaidManagers() throws IOException {
        String filePath = "src/test/resources/employees.csv";
        Map<String, List<String>> results = service.readCSV(filePath);

        assertNotNull(results);
        assertFalse(results.get("underpaidManagers").isEmpty());
    }

    @Test
    void getAnalysisResults_shouldIdentifyOverpaidManagers() throws IOException {
        String filePath = "src/test/resources/overpaid_managers.csv";
        Map<String, List<String>> results = service.readCSV(filePath);

        assertNotNull(results);
        assertFalse(results.get("overpaidManagers").isEmpty());
    }

    @Test
    void getAnalysisResults_shouldIdentifyExcessiveDepthEmployees() throws IOException {
        String filePath = "src/test/resources/excessive_depth.csv";
        Map<String, List<String>> results = service.readCSV(filePath);

        assertNotNull(results);
        assertFalse(results.get("excessiveDepthEmployees").isEmpty());
    }
}
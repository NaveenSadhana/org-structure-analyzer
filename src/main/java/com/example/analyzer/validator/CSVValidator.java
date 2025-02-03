package com.example.analyzer.validator;

import com.example.analyzer.exception.InvalidCSVException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.regex.Pattern;

@Slf4j
@Component
public class CSVValidator {
    private static final String HEADER = "Id,firstName,lastName,salary,managerId";
    private static final Pattern NUMBER_PATTERN = Pattern.compile("^\\d+(\\.\\d+)?$");

    public void validateCSV(MultipartFile file) throws InvalidCSVException {
        log.info("Validating CSV file: {}", file.getOriginalFilename());
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line = reader.readLine();
            if (line == null || !line.equals(HEADER)) {
                log.error("Invalid CSV header. Expected: {}", HEADER);
                throw new InvalidCSVException("Invalid CSV header. Expected: " + HEADER);
            }

            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length < 4 || values.length > 5) {
                    log.error("Invalid CSV format at line: {}", line);
                    throw new InvalidCSVException("Invalid CSV format at line: " + line);
                }
                if (!NUMBER_PATTERN.matcher(values[0]).matches() || !NUMBER_PATTERN.matcher(values[3]).matches()) {
                    log.error("Invalid numeric values in line: {}", line);
                    throw new InvalidCSVException("Invalid numeric values in line: " + line);
                }
            }
        } catch (Exception e) {
            log.error("Error reading CSV file: {}", e.getMessage());
            throw new InvalidCSVException("Error reading CSV file: " + e.getMessage());
        }
        log.info("CSV file validated successfully");
    }
}
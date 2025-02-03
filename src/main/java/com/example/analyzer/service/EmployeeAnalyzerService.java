package com.example.analyzer.service;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

@Slf4j
@Service
public class EmployeeAnalyzerService {
    public final Map<Integer, Employee> employeeMap = new HashMap<>();
    @Getter
    private Employee ceo = null;

    public Map<String, List<String>> readCSV(String filePath) throws IOException {
        log.info("Reading CSV file from path: {}", filePath);
        try (var br = new BufferedReader(new FileReader(filePath))) {
            br.lines().skip(1).forEach(line -> {
                Employee employee = parseCSVLine(line);
                employeeMap.put(employee.id(), employee);
                if (employee.managerId() == null) {
                    ceo = employee;
                } else {
                    var manager = employeeMap.get(employee.managerId());
                    if (manager != null) {
                        // Add the employee as a subordinate to the manager
                        manager.subordinates().add(employee);
                    }
                }
            });
        } catch (IOException e) {
            log.error("Error reading CSV file: {}", e.getMessage());
            throw new IOException("Error reading CSV file", e);
        }
        log.info("CSV file read successfully");
        // Return the analysis results
        return getAnalysisResults();
    }

    private Employee parseCSVLine(String line) {
        var parts = line.split(",");
        return new Employee(
                Integer.parseInt(parts[0]),
                parts[1],
                parts[2],
                Double.parseDouble(parts[3]),
                parts.length > 4 && !parts[4].isEmpty() ? Integer.parseInt(parts[4]) : null
        );
    }

    private int getDepth(Employee emp) {
        int depth = 0;
        while (emp.managerId() != null) {
            emp = employeeMap.get(emp.managerId());
            depth++;
        }
        return depth;
    }

    private Map<String, List<String>> getAnalysisResults() {
        List<String> underpaidManagers = new ArrayList<>();
        List<String> overpaidManagers = new ArrayList<>();
        List<String> excessiveDepthEmployees = new ArrayList<>();

        // Check for underpaid and overpaid managers
        employeeMap.values().forEach(manager -> {
            if (!manager.subordinates().isEmpty()) {
                double avgSalary = manager.subordinates().stream().mapToDouble(Employee::salary).average().orElse(0.0);
                double minAllowedSalary = avgSalary * 1.2;
                double maxAllowedSalary = avgSalary * 1.5;

                if (manager.salary() < minAllowedSalary) {
                    underpaidManagers.add(String.format("Manager %s earns %.2f less than required", manager.firstName(), minAllowedSalary - manager.salary()));
                } else if (manager.salary() > maxAllowedSalary) {
                    overpaidManagers.add(String.format("Manager %s earns %.2f more than allowed", manager.firstName(), manager.salary() - maxAllowedSalary));
                }
            }
        });

        // Check for employees with excessive reporting depth
        employeeMap.values().forEach(emp -> {
            int depth = getDepth(emp);
            if (depth > 4) {
                excessiveDepthEmployees.add(String.format("Employee %s:%s has a reporting depth of %d, which is %d too many",
                        emp.firstName(), emp.id(), depth, depth - 4));
            }
        });

        // Prepare the result map : underpaidManagers, overpaidManagers, excessiveDepthEmployees
        return prepareResult(underpaidManagers, overpaidManagers, excessiveDepthEmployees);
    }

    private static Map<String, List<String>> prepareResult(List<String> underpaidManagers, List<String> overpaidManagers, List<String> excessiveDepthEmployees) {
        Map<String, List<String>> result = new LinkedHashMap<>();

        result.put("underpaidManagers", underpaidManagers);
        result.put("overpaidManagers", overpaidManagers);
        result.put("excessiveDepthEmployees", excessiveDepthEmployees);
        log.info("Analysis results generated successfully {}", result);
        return result;
    }

    /**
     * Represents an Employee with an id, first name, last name, salary, manager id, and a list of subordinates.
     * This class is implemented as a record to provide a compact and immutable data structure.
     *
     * @param id The unique identifier of the employee.
     * @param firstName The first name of the employee.
     * @param lastName The last name of the employee.
     * @param salary The salary of the employee.
     * @param managerId The id of the manager of the employee. Can be null if the employee has no manager.
     * @param subordinates The list of subordinates under this employee. Initialized to an empty list.
     */
    public record Employee(int id, String firstName, String lastName, double salary, Integer managerId, List<Employee> subordinates) {
        public Employee(int id, String firstName, String lastName, double salary, Integer managerId) {
            this(id, firstName, lastName, salary, managerId, new ArrayList<>());
        }
    }
}
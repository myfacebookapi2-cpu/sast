package com.accenture.sast.tracker.controller;

import com.accenture.sast.tracker.model.Employee;
import com.accenture.sast.tracker.service.EmployeeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService service;

    public EmployeeController(EmployeeService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Employee> create(@RequestBody Employee employee) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(employee));
    }

    @GetMapping
    public List<Employee> list() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> getById(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // The search endpoint is wired to the vulnerable EmployeeService.search()
    @GetMapping("/search")
    public List<Employee> search(@RequestParam String name) {
        return service.search(name);
    }

    @GetMapping("/{id}/profile")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Map<String, Object>> getProfile(@PathVariable Long id) {
        Optional<Employee> opt = service.findById(id);
        if (opt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Employee e = opt.get();
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("id", e.getId());
        body.put("name", e.getName());
        body.put("salary", e.getSalary());
        body.put("nationalId", e.getNationalId());
        return ResponseEntity.ok(body);
    }
}

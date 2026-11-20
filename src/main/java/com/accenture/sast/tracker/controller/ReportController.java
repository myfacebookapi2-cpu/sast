package com.accenture.sast.tracker.controller;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private static final String REPORT_DIR = "C:/reports/";

    @GetMapping("/download")
    public ResponseEntity<Resource> download(@RequestParam String filename) throws IOException {
        Path reportDirectory = Paths.get(REPORT_DIR).toAbsolutePath().normalize();
        Path resolved = reportDirectory.resolve(filename).normalize();

        if (Paths.get(filename).isAbsolute() || !resolved.startsWith(reportDirectory)
                || resolved.equals(reportDirectory) || !Files.isRegularFile(resolved)) {
            return ResponseEntity.notFound().build();
        }

        String safeFilename = resolved.getFileName().toString();
        byte[] content = Files.readAllBytes(resolved);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + safeFilename.replace("\"", "") + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new ByteArrayResource(content));
    }
}

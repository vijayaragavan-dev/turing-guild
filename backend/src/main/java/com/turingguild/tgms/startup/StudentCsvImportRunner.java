package com.turingguild.tgms.startup;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Order(2)
@RequiredArgsConstructor
public class StudentCsvImportRunner implements ApplicationRunner {

    private final StudentCsvImportService studentCsvImportService;

    @Override
    public void run(ApplicationArguments args) {
        log.info("StudentCsvImportRunner triggered.");

        try {
            studentCsvImportService.importStudents();
        } catch (Exception e) {
            log.error("StudentCsvImportRunner FAILED with exception:", e);
        }

        log.info("StudentCsvImportRunner completed.");
    }
}

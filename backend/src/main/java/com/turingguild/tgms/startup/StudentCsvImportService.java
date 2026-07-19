package com.turingguild.tgms.startup;

import com.turingguild.tgms.entity.User;
import com.turingguild.tgms.entity.enums.Role;
import com.turingguild.tgms.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

@Slf4j
@Service
@RequiredArgsConstructor
public class StudentCsvImportService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void importStudents() {
        long studentCount = userRepository.countByRole(Role.STUDENT);

        if (studentCount > 0) {
            log.info("Students already exist in database (count={}). Import skipped.", studentCount);
            return;
        }

        log.info("Import Started");

        try {
            PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            Resource[] csvFiles = resolver.getResources("classpath*:*.csv");

            Arrays.sort(csvFiles, Comparator.comparing(Resource::getFilename));

            if (csvFiles.length == 0) {
                log.warn("No CSV files found in classpath. Import aborted.");
                return;
            }

            log.info("Found {} CSV file(s)", csvFiles.length);

            int totalRecords = 0;
            int imported = 0;
            int skipped = 0;
            int failed = 0;
            Set<String> importedBatchNumbers = new TreeSet<>();

            for (Resource csvFile : csvFiles) {
                String filename = csvFile.getFilename();
                log.info("CSV File Found: {}", filename);

                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(csvFile.getInputStream(), StandardCharsets.UTF_8))) {

                    String line;
                    boolean headerSkipped = false;

                    while ((line = reader.readLine()) != null) {
                        if (!headerSkipped) {
                            headerSkipped = true;
                            String header = line.trim().toLowerCase();
                            if (header.startsWith("s.no")) {
                                log.info("Header detected with S.No column in {}", filename);
                            }
                            continue;
                        }

                        if (line.isBlank()) {
                            continue;
                        }

                        totalRecords++;

                        String[] parts = line.split(",", -1);

                        int batchNoIndex;
                        int nameIndex;
                        int passwordIndex;

                        if (parts.length >= 4) {
                            batchNoIndex = 1;
                            nameIndex = 2;
                            passwordIndex = 3;
                        } else if (parts.length >= 3) {
                            batchNoIndex = 0;
                            nameIndex = 1;
                            passwordIndex = 2;
                        } else {
                            log.warn("Invalid Row Skipped ({}): not enough columns - {}", filename, line);
                            skipped++;
                            continue;
                        }

                        String batchNumber = parts[batchNoIndex].trim();
                        String name = parts[nameIndex].trim();
                        String password = parts[passwordIndex].trim();

                        if (batchNumber.isEmpty() || name.isEmpty() || password.isEmpty()) {
                            log.warn("Invalid Row Skipped ({}): batchNumber='{}', name='{}'",
                                    filename, batchNumber, name);
                            skipped++;
                            continue;
                        }

                        try {
                            if (userRepository.existsByBatchNumber(batchNumber)
                                    || importedBatchNumbers.contains(batchNumber)) {
                                log.info("Duplicate Student Skipped: batchNumber={}, name={}",
                                        batchNumber, name);
                                skipped++;
                                continue;
                            }

                            User student = User.builder()
                                    .batchNumber(batchNumber)
                                    .fullName(name)
                                    .passwordHash(passwordEncoder.encode(password))
                                    .role(Role.STUDENT)
                                    .isActive(true)
                                    .firstLogin(true)
                                    .build();

                            userRepository.save(student);
                            importedBatchNumbers.add(batchNumber);

                            log.info("Student Imported: batchNumber={}, name={}", batchNumber, name);
                            imported++;
                        } catch (Exception e) {
                            log.error("Failed to import student: batchNumber={}, name={}",
                                    batchNumber, name, e);
                            failed++;
                        }
                    }

                    log.info("CSV Completed: {}", filename);

                } catch (Exception e) {
                    log.error("Error reading CSV file: {}", filename, e);
                }
            }

            userRepository.flush();

            log.info("Final Summary: Total CSV Files={}, Total Records={}, Imported={}, Skipped={}, Failed={}",
                    csvFiles.length, totalRecords, imported, skipped, failed);

            verifyImport(imported);

        } catch (Exception e) {
            log.error("Import failed with exception:", e);
        }
    }

    private void verifyImport(int expectedCount) {
        long actualCount = userRepository.countByRole(Role.STUDENT);

        if (actualCount == expectedCount) {
            log.info("Post-import verification PASSED: expected={}, actual={}", expectedCount, actualCount);
        } else {
            log.error("Post-import verification FAILED: expected={}, actual={}", expectedCount, actualCount);
        }
    }
}

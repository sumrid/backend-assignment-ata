package com.klinkanha.assignment.config;

import com.klinkanha.assignment.model.JobCsvBean;
import com.klinkanha.assignment.repository.JobRepository;
import com.klinkanha.assignment.repository.entity.JobEntity;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataLoader {

    private final JobRepository jobRepository;

    @EventListener(ApplicationReadyEvent.class)
    public void loadJobs() {
        // read csv to object
        var file = Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("data/survey.csv"));
        var jobs = parseCsv(file, JobCsvBean.class);
        log.info("Loaded {} jobs from CSV", jobs.size());

        for (JobCsvBean job : jobs) {
            BigDecimal salaryNumber = null;
            if (StringUtils.isNoneEmpty(job.getSalaryNumber())) {
                salaryNumber = new BigDecimal(job.getSalaryNumber());
            }
            jobRepository.save(new JobEntity()
                    .setEmployer(job.getEmployer())
                    .setLocation(job.getLocation())
                    .setJobTitle(job.getJobTitle())
                    .setYearsAtEmployer(job.getYearsAtEmployer())
                    .setYearsOfExperience(job.getYearsOfExperience())
                    .setSigningBonus(job.getSigningBonus())
                    .setAnnualBonus(job.getAnnualBonus())
                    .setSalaryRaw(job.getSalary())
                    .setSalary(salaryNumber)
                    .setGender(job.getGender())
                    .setCurrency(job.getCurrency())
            );
        }
        log.info("Loaded {} jobs into database", jobs.size());
    }

    private <T> List<T> parseCsv(InputStream inputStream, Class<T> type) {
        try (var reader = new BufferedReader(new InputStreamReader(inputStream))) {
            var csvParser = new CsvToBeanBuilder<T>(reader)
                    .withType(type)
                    .build();
            return csvParser.parse();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

package com.klinkanha.assignment.controller;

import com.klinkanha.assignment.model.JobRequest;
import com.klinkanha.assignment.model.JobResponse;
import com.klinkanha.assignment.service.JobService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;

    @GetMapping("/jobs")
    public List<JobResponse> getJobs(
            @Valid JobRequest request
    ) {
        return jobService.search(request);
    }
}

package com.klinkanha.assignment.model;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobRequest {
    private List<@Pattern(regexp = allowFieldPattern, message = errorMessage) String> fields;
    private List<@Pattern(regexp = allowFieldPattern, message = errorMessage) String> sortBy;
    private Boolean sortDescending = false;
    private int size = 20;

    private static final String allowFieldPattern = "(job_title|salary|gender)";
    private static final String errorMessage = "must be one of job_title, salary, gender";
}

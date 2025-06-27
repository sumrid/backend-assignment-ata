package com.klinkanha.assignment.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobRequest {
    @Valid
    private List<@Pattern(regexp = "") String> filterBy;

    private List<String> fields;
    private List<String> sortBy;
    private Boolean sortDescending;
    private int size = 20;
}

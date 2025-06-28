package com.klinkanha.assignment.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FilterBy {
    private String field;
    private String type;
    private String value;
}

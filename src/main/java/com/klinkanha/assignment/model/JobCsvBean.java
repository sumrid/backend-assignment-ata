package com.klinkanha.assignment.model;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;

@Data
public class JobCsvBean {
    @CsvBindByName(column = "timestamp")
    private String timestamp;

    @CsvBindByName(column = "employer")
    private String employer;

    @CsvBindByName(column = "location")
    private String location;

    @CsvBindByName(column = "job_title")
    private String jobTitle;

    @CsvBindByName(column = "years_at_employer")
    private String yearsAtEmployer;

    @CsvBindByName(column = "years_of_experience")
    private String yearsOfExperience;

    @CsvBindByName(column = "signing_bonus")
    private String signingBonus;

    @CsvBindByName(column = "annual_bonus")
    private String annualBonus;

    @CsvBindByName(column = "gender")
    private String gender;

    @CsvBindByName(column = "salary")
    private String salary;

    @CsvBindByName(column = "salary_number")
    private String salaryNumber;

    @CsvBindByName(column = "currency")
    private String currency;
}

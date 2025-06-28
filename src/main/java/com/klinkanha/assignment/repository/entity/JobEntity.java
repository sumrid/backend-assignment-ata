package com.klinkanha.assignment.repository.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "jobs")
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class JobEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "employer")
    private String employer;

    @Column(name = "location")
    private String location;

    @Column(name = "job_title")
    private String jobTitle;

    @Column(name = "years_at_employer")
    private String yearsAtEmployer;

    @Column(name = "years_of_experience")
    private String yearsOfExperience;

    @Column(name = "signing_bonus")
    private String signingBonus;

    @Column(name = "annual_bonus")
    private String annualBonus;

    @Column(name = "gender")
    private String gender;

    @Column(name = "salary_raw")
    private String salaryRaw;

    @Column(name = "salary")
    private BigDecimal salary;

    @Column(name = "currency")
    private String currency;
}

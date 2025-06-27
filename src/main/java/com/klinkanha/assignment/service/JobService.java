package com.klinkanha.assignment.service;

import com.klinkanha.assignment.model.JobRequest;
import com.klinkanha.assignment.model.JobResponse;
import com.klinkanha.assignment.repository.entity.JobEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class JobService {
    private final EntityManager entityManager;

    public List<JobResponse> search(JobRequest request) {
        var cb = entityManager.getCriteriaBuilder();
        var query = cb.createQuery(JobEntity.class);
        var jobs = query.from(JobEntity.class);

        if (CollectionUtils.isNotEmpty(request.getFilterBy())) {

        }

        if (CollectionUtils.isNotEmpty(request.getFields())) {
            // projection
        }

        if (CollectionUtils.isNotEmpty(request.getSortBy())) {
            var order = request.getSortBy().stream().map(field -> {
                if (request.getSortDescending()) {
                    return cb.desc(jobs.get(field));
                } else {
                    return cb.asc(jobs.get(field));
                }
            }).toList();
            query.orderBy(order);
        }

        var prediction = new ArrayList<Predicate>();
        var response = entityManager.createQuery(query)
                .setMaxResults(request.getSize())
                .getResultList();

        log.info("Found {} jobs", response.size());
        return response.stream().map(this::mapToResponse).toList();
    }

    private JobResponse mapToResponse(JobEntity entity) {
        return new JobResponse()
                .setEmployer(entity.getEmployer())
                .setLocation(entity.getLocation())
                .setJobTitle(entity.getJobTitle())
                .setYearsAtEmployer(entity.getYearsAtEmployer())
                .setYearsOfExperience(entity.getYearsOfExperience())
                .setSalary(entity.getSalary())
                .setAnnualBonus(entity.getAnnualBonus())
                .setGender(entity.getGender());
    }
}

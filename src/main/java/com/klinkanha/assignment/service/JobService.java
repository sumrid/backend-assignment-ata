package com.klinkanha.assignment.service;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.klinkanha.assignment.exception.InvalidInputException;
import com.klinkanha.assignment.model.FilterBy;
import com.klinkanha.assignment.model.JobRequest;
import com.klinkanha.assignment.model.JobResponse;
import com.klinkanha.assignment.repository.entity.JobEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Tuple;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.text.CaseUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class JobService {
    private final EntityManager entityManager;
    private static final Pattern PARAM_FILTER = Pattern.compile("(\\w+)\\[(gte|lte|eq|like)]", Pattern.CASE_INSENSITIVE);
    private static final Set<String> ALLOW_FILTER = Set.of("job_title", "salary", "gender");

    public List<JobResponse> search(JobRequest request, Map<String, String> filterBy) {
        var cb = entityManager.getCriteriaBuilder();
        var query = cb.createTupleQuery();
        var jobs = query.from(JobEntity.class);

        var filters = this.filterParser(filterBy);
        this.filterBy(filters, cb, jobs, query);
        this.projectionField(request, jobs, query);
        this.sortBy(request, cb, jobs, query);

        var response = entityManager.createQuery(query)
                .setMaxResults(request.getSize())
                .getResultList();

        return response.stream().map(this::mapToResponse).toList();
    }

    private List<FilterBy> filterParser(Map<String, String> filterBy) {
        var filters = new ArrayList<FilterBy>();
        for (var entry : filterBy.entrySet()) {
            var match = PARAM_FILTER.matcher(entry.getKey());
            if (match.find()) {
                var field = match.group(1);
                if (!ALLOW_FILTER.contains(field)) {
                    throw new InvalidInputException("Invalid filter parameter: " + entry.getKey());
                }
                filters.add(new FilterBy(field, match.group(2), entry.getValue()));
            }
        }
        return filters;
    }

    private void filterBy(List<FilterBy> filters, CriteriaBuilder cb, Root<JobEntity> jobs, CriteriaQuery<Tuple> query) {
        var prediction = new ArrayList<Predicate>();
        log.info("filter by: {}", filters);
        for (var filter : filters) {
            var fieldCamel = CaseUtils.toCamelCase(filter.getField(), false, '_');
            switch (filter.getType()) {
                case "gte":
                    prediction.add(cb.greaterThanOrEqualTo(jobs.get(fieldCamel), filter.getValue()));
                    break;
                case "lte":
                    prediction.add(cb.lessThanOrEqualTo(jobs.get(fieldCamel), filter.getValue()));
                    break;
                case "eq":
                    prediction.add(cb.equal(jobs.get(fieldCamel), filter.getValue()));
                    break;
                case "like":
                    prediction.add(cb.like(
                            cb.lower(jobs.get(fieldCamel)),
                            "%" + filter.getValue().toLowerCase() + "%"
                    ));
            }
        }
        query.where(prediction.toArray(new Predicate[0]));
    }

    private void projectionField(JobRequest request, Root<JobEntity> jobs, CriteriaQuery<Tuple> query) {
        if (CollectionUtils.isNotEmpty(request.getFields())) {
            log.info("selected field {}", request.getFields());
            var selections = request.getFields().stream()
                    .map(field -> {
                        var fieldCamelCase = CaseUtils.toCamelCase(field, false, '_');
                        return jobs.get(fieldCamelCase).alias(field);
                    })
                    .toArray(Selection[]::new);

            query.multiselect(selections);
        } else {
            log.info("selected all field");
            var selections = Arrays
                    .stream(JobEntity.class.getDeclaredFields())
                    .map(field -> {
                        var fieldSnakeCase = PropertyNamingStrategies.SnakeCaseStrategy.INSTANCE.translate(field.getName());
                        return jobs.get(field.getName()).alias(fieldSnakeCase);
                    })
                    .toArray(Selection[]::new);
            query.multiselect(selections);
        }
    }

    private void sortBy(JobRequest request, CriteriaBuilder cb, Root<JobEntity> jobs, CriteriaQuery<Tuple> query) {
        if (CollectionUtils.isNotEmpty(request.getSortBy())) {
            var order = request.getSortBy().stream().map(field -> {
                var fieldCamelCase = CaseUtils.toCamelCase(field, false, '_');
                if (request.getSortDescending()) {
                    return cb.desc(jobs.get(fieldCamelCase));
                } else {
                    return cb.asc(jobs.get(fieldCamelCase));
                }
            }).toList();
            query.orderBy(order);
        }
    }

    private JobResponse mapToResponse(Tuple entity) {
        var mapResponse = new JobResponse();
        for (var e : entity.getElements()) {
            var field = entity.get(e);
            mapResponse.put(e.getAlias(), field);
        }
        return mapResponse;
    }
}

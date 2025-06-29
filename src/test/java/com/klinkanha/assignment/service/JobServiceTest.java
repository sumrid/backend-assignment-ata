package com.klinkanha.assignment.service;

import com.klinkanha.assignment.exception.InvalidInputException;
import com.klinkanha.assignment.model.JobRequest;
import com.klinkanha.assignment.repository.entity.JobEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Tuple;
import jakarta.persistence.TupleElement;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JobServiceTest {

    @InjectMocks
    private JobService jobService;

    @Mock
    private EntityManager entityManager;
    @Mock
    private CriteriaQuery<Tuple> query;
    @Mock
    private Root<JobEntity> jobEntity;
    @Mock
    private Path<Object> path;
    @Mock
    private TypedQuery<Tuple> typedQuery;

    @ParameterizedTest
    @CsvSource({
            "salary[gte]",
            "salary[lte]",
            "salary[eq]",
            "job_title[like]"
    })
    public void searchSuccess(String filterBy) {
        // mock
        mockQuery();

        var request = new JobRequest()
                .setFields(List.of("salary"))
                .setSortBy(List.of("salary"));
        var filter = new HashMap<String, String>();
        filter.put(filterBy, "1000");

        var result = jobService.search(request, filter);

        assertEquals(1, result.size());
    }

    @Test
    public void searchSuccessSelectAllFields() {
        // mock
        mockQuery();

        var request = new JobRequest()
                .setFields(List.of())
                .setSortBy(List.of("salary"));
        var filter = new HashMap<String, String>();

        var result = jobService.search(request, filter);

        assertEquals(1, result.size());
    }

    @Test
    public void searchErrorWhenFieldNotAllow() {
        // mock
        mockQuery();

        var request = new JobRequest()
                .setFields(List.of("salary"))
                .setSortBy(List.of("salary"));
        var filter = new HashMap<String, String>();
        filter.put("invalid[eq]", "test");

        var exception = assertThrows(InvalidInputException.class, () -> jobService.search(request, filter));
        assertEquals("Invalid filter parameter: invalid[eq]", exception.getMessage());
    }

    private void mockQuery() {
        var tuple = mock(Tuple.class);
        var tupleElement = mock(TupleElement.class);
        lenient().when(tuple.getElements()).thenReturn(List.of(tupleElement));
        lenient().when(jobEntity.get(anyString())).thenReturn(path);
        lenient().when(query.from(JobEntity.class)).thenReturn(jobEntity);
        lenient().when(typedQuery.getResultList()).thenReturn(List.of(tuple));
        lenient().when(typedQuery.setMaxResults(anyInt())).thenReturn(typedQuery);

        CriteriaBuilder cb = mock(CriteriaBuilder.class);
        when(cb.createTupleQuery()).thenReturn(query);
        when(entityManager.getCriteriaBuilder()).thenReturn(cb);
        lenient().when(entityManager.createQuery(query)).thenReturn(typedQuery);
    }
}
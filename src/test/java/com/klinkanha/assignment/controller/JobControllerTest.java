package com.klinkanha.assignment.controller;

import com.klinkanha.assignment.model.JobResponse;
import com.klinkanha.assignment.service.JobService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(JobController.class)
class JobControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private JobService jobService;

    @Test
    public void getJobsSuccess() throws Exception {
        // mock
        var response = new JobResponse();
        response.put("job_title", "dev");
        when(jobService.search(any(), any())).thenReturn(List.of(response));

        var request = get("/api/jobs")
                .param("fields", "salary")
                .param("sortBy", "salary")
                .param("sortDescending", "true")
                .param("size", "10");

        // execute
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("[0].job_title").value("dev"));
    }

    @ParameterizedTest
    @CsvSource({
            "fields,invalid,'fields[0]: must be one of job_title, salary, gender'",
            "sortBy,invalid,'sortBy[0]: must be one of job_title, salary, gender'"
    })
    public void getJobsInputValidationFail(String field, String value, String expectedMessage) throws Exception {
        // mock
        var response = new JobResponse();
        response.put("job_title", "dev");
        when(jobService.search(any(), any())).thenReturn(List.of(response));

        var request = get("/api/jobs").param(field, value);

        // execute
        mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("message").value(expectedMessage));
    }
}
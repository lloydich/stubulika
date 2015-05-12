package com.stubulika.resource;


import com.google.gson.Gson;
import com.stubulika.Application;
import com.stubulika.domain.StubRequest;
import com.stubulika.domain.StubResponse;
import com.stubulika.repository.StubAdminRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.HashMap;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsMapContaining.hasEntry;
import static org.hamcrest.core.IsEqual.equalTo;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class StubEndpointControllerTest {
    Logger logger = LoggerFactory.getLogger(StubAdminController.class);


    final String BASE_URL = "http://localhost:8080/endpoint/";

    @Autowired
    private StubAdminRepository stubAdminRepository;


    @Test
    public void shouldReturnStubResponseForRetrieve() throws Exception {

        //given
        final String RESOURCE_URL = "aUrl";
        final String METHOD = "GET";

        StubRequest stubRequest = new StubRequest();
        stubRequest.setUrl(RESOURCE_URL);
        stubRequest.setMethod(METHOD);

        StubAdminRequest stubAdminRequest = new StubAdminRequest();

        stubAdminRequest.setRequest(stubRequest);
        StubResponse stubResponse = new StubResponse();
        int status = 200;
        stubResponse.setStatus(status);
        HashMap<String, Object> body = new HashMap<>();
        body.put("foo","bar");
        Gson gson = new Gson();
        stubResponse.setBody(gson.toJson(body));
        HttpHeaders headers = new HttpHeaders();
        String headerKey = "Content-Type";
        String headerValue = "application/json; charset=UTF-8";
        headers.put(headerKey, Arrays.asList(headerValue));
        stubResponse.setHeaders(headers);

        stubAdminRepository.save(stubRequest,stubResponse);

        RestTemplate restTemplate = new TestRestTemplate();

        //when
        ResponseEntity<String> response = restTemplate.getForEntity(
                BASE_URL+RESOURCE_URL,
                String.class);


        //then
        String expectedBody = gson.toJson(body);
        assertThat(response.getBody(), equalTo(expectedBody));
        assertThat(response.getStatusCode().value(), equalTo(status));
        assertThat(response.getHeaders(), hasEntry(headerKey, Arrays.asList(headerValue)));
    }

    @Test
    public void shouldReturnNotFoundForRetrieve(){
        //given

        RestTemplate restTemplate = new TestRestTemplate();


        //when
        ResponseEntity<String> response = restTemplate.getForEntity(
                BASE_URL+"madeUpNameOfResource",
                String.class);

        logger.debug("shouldReturnNotFoundForRetrieve() response"+response);

        //then
        assertThat(response.getStatusCode().value(), equalTo(404));


    }


}
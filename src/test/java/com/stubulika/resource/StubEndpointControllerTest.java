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
    public static final int ERROR_STATUS = 500;
    public static final String POST_METHOD = "POST";
    Logger logger = LoggerFactory.getLogger(StubAdminController.class);

    final String BASE_URL = "http://localhost:8080/endpoint/";

    @Autowired
    private StubAdminRepository stubAdminRepository;

    private static final int OK_STATUS = 200;
    private HashMap<String, Object> body = new HashMap<>();;
    private static final String HEADER_KEY = "Content-Type";
    private static final String HEADER_VALUE = "application/json; charset=UTF-8";
    private static final String RESOURCE_URL = "aUrl";
    private static final String GET_METHOD = "GET";
    private  Gson gson  = new Gson();

    @Test
    public void shouldReturnStubResponseForGetFromStubEndpoint() throws Exception {

        //given
        body.put("foo", "bar");
        StubRequest stubRequest = new StubRequest();
        stubRequest.setUrl(RESOURCE_URL);
        stubRequest.setMethod(GET_METHOD);
        StubResponse stubResponse = createStubResponse(OK_STATUS);

        stubAdminRepository.save(stubRequest,stubResponse);

        RestTemplate restTemplate = new TestRestTemplate();

        //when
        ResponseEntity<String> response = restTemplate.getForEntity(BASE_URL+ RESOURCE_URL, String.class);


        //then
        assertThat(response.getBody(), equalTo(gson.toJson(body)));
        assertThat(response.getStatusCode().value(), equalTo(OK_STATUS));
        assertThat(response.getHeaders(), hasEntry(HEADER_KEY, Arrays.asList(HEADER_VALUE)));
    }

    @Test
    public void shouldReturnStubResponseForPostToStubEndpoint() throws Exception {

        //given
        body.put("foo", "bar");
        StubRequest stubRequest = new StubRequest();
        stubRequest.setUrl(RESOURCE_URL);
        stubRequest.setBody(gson.toJson(body));
        stubRequest.setMethod(POST_METHOD);
        StubResponse stubResponse = createStubResponse(ERROR_STATUS);

        stubAdminRepository.save(stubRequest,stubResponse);

        RestTemplate restTemplate = new TestRestTemplate();

        //when
        ResponseEntity<String> response = restTemplate.postForEntity(BASE_URL + RESOURCE_URL, String.class, null);


        //then
        System.out.println("response:"+response);
        assertThat(response.getStatusCode().value(), equalTo(ERROR_STATUS));
    }



    @Test
    public void shouldReturnNotFoundForGetFromStubEndpoint(){
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


    private StubResponse createStubResponse(int status) {
        StubResponse stubResponse = new StubResponse();
        stubResponse.setStatus(status);
        stubResponse.setBody(gson.toJson(body));
        HttpHeaders headers = new HttpHeaders();
        headers.put(HEADER_KEY, Arrays.asList(HEADER_VALUE));
        stubResponse.setHeaders(headers);
        return stubResponse;
    }

}
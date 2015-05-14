package com.stubulika.resource;


import com.google.gson.Gson;
import com.stubulika.Application;
import com.stubulika.domain.StubAdminService;
import com.stubulika.domain.StubRequest;
import com.stubulika.domain.StubResponse;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
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
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)

@IntegrationTest
public class StubEndpointControllerTest {
    public static final int ERROR_STATUS = 500;
    public static final String POST_METHOD = "POST";
    public static final String NO_BODY = null;
    public static final String PUT_METHOD = "PUT";
    public static final String DELETE_METHOD = "DELETE";
    public static final int ACCEPTED_STATUS = 202;
    private Logger logger = LoggerFactory.getLogger(StubEndpointControllerTest.class);

    private final String BASE_URL = "http://localhost:8080/endpoint/";

    @Autowired
    private StubAdminService stubAdminService;

    private static final int OK_STATUS = 200;

    private static final String HEADER_KEY = "Content-Type";
    private static final String HEADER_VALUE = "application/json; charset=UTF-8";
    private static final String RESOURCE_URL = "aUrl";
    private static final String GET_METHOD = "GET";
    private Gson gson = new Gson();

    @Test
    public void shouldReturnStubResponseForGetFromStubEndpoint() throws Exception {
        //given
        String body = createJsonString("foo", "bar");
        StubRequest stubRequest = createStubRequest(RESOURCE_URL, GET_METHOD);
        StubResponse stubResponse = createStubResponse(OK_STATUS, body);

        stubAdminService.save(stubRequest, stubResponse);

        RestTemplate restTemplate = new TestRestTemplate();

        //when
        ResponseEntity<String> response = restTemplate.getForEntity(BASE_URL + RESOURCE_URL, String.class);

        //then
        logger.debug("shouldReturnStubResponseForGetFromStubEndpoint() response:" + response);
        assertThat(response.getBody(), equalTo(body));
        assertThat(response.getStatusCode().value(), equalTo(OK_STATUS));
        assertThat(response.getHeaders(), hasEntry(HEADER_KEY, Arrays.asList(HEADER_VALUE)));
    }

    @Test
    public void shouldReturnStubResponseForGetWithHeadersFromStubEndpoint() throws Exception {
        //given
        String body = createJsonString("foo", "bar");
        StubRequest stubRequest = createStubRequest(RESOURCE_URL, GET_METHOD);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept","application/json");
        headers.set("User-Agent","Foo/bar");
        headers.set("Connection","keep-alive");
        headers.set("Host","localhost:8080");
        stubRequest.setHeaders(headers);
        StubResponse stubResponse = createStubResponse(OK_STATUS, body);

        stubAdminService.save(stubRequest, stubResponse);

        RestTemplate restTemplate = new TestRestTemplate();
        HttpEntity<?> requestEntity = new HttpEntity<Object>(headers);

        //when
        ResponseEntity<String> response = restTemplate.exchange(BASE_URL + RESOURCE_URL,HttpMethod.GET, requestEntity, String.class);


        //then
        assertThat(response.getStatusCode().value(), equalTo(OK_STATUS));
    }

    @Test
    public void shouldReturnErrorStatusStubResponseForPostToStubEndpoint() throws Exception {
        //given
        String body = createJsonString("foo", "bar");
        StubRequest stubRequest = createStubRequest(RESOURCE_URL, POST_METHOD, body);
        StubResponse stubResponse = createStubResponse(ERROR_STATUS, NO_BODY);

        stubAdminService.save(stubRequest, stubResponse);

        RestTemplate restTemplate = new TestRestTemplate();
        HttpEntity<?> requestEntity = new HttpEntity<Object>(body);

        //when
        ResponseEntity<String> response = restTemplate.postForEntity(BASE_URL + RESOURCE_URL, requestEntity, String.class);
        logger.debug("shouldReturnErrorStatusStubResponseForPostToStubEndpoint() response:" + response);

        //then
        assertThat(response.getStatusCode().value(), equalTo(ERROR_STATUS));
    }

    @Test
    public void shouldReturnAStatusStubResponseForDeleteOnStubEndpoint() throws Exception {
        //given
        String url = RESOURCE_URL + "/1";
        StubRequest stubRequest = createStubRequest(url, DELETE_METHOD);
        StubResponse stubResponse = createStubResponse(ACCEPTED_STATUS, NO_BODY);

        stubAdminService.save(stubRequest, stubResponse);

        RestTemplate restTemplate = new TestRestTemplate();

        //when
        ResponseEntity<String> response = restTemplate.exchange(BASE_URL + url, HttpMethod.DELETE, null, String.class);
        logger.debug("shouldReturnAcceptedStatusStubResponseForDeleteOnStubEndpoint() response:" + response);

        //then
        assertThat(response.getStatusCode().value(), equalTo(ACCEPTED_STATUS));
    }

    @Test
    @Ignore
    public void shouldReturnCreatedStatusStubResponseForPutToStubEndpoint() throws Exception {
        //given
        String body = createJsonString("foo", "bar");
        StubRequest stubRequest = createStubRequest(RESOURCE_URL + "/2", PUT_METHOD, body);
        StubResponse stubResponse = createStubResponse(OK_STATUS, NO_BODY);

        stubAdminService.save(stubRequest, stubResponse);

        RestTemplate restTemplate = new TestRestTemplate();

        Long id = 2l;
        HttpEntity<?> requestEntity = new HttpEntity<Object>(body);

        //when
        ResponseEntity<String> response = restTemplate.exchange(BASE_URL + RESOURCE_URL + "/{id}", HttpMethod.PUT, requestEntity, String.class, id);


        //then
        logger.debug("shouldReturnCreatedStatusStubResponseForPutToStubEndpoint() response:" + response);
        assertThat(response.getStatusCode().value(), equalTo(OK_STATUS));
    }


    @Test
    public void shouldReturnNotFoundForGetFromStubEndpoint() {
        //given
        RestTemplate restTemplate = new TestRestTemplate();

        //when
        ResponseEntity<String> response = restTemplate.getForEntity(
                BASE_URL + "madeUpNameOfResource",
                String.class);

        logger.debug("shouldReturnNotFoundForRetrieve() response" + response);

        //then
        assertThat(response.getStatusCode().value(), equalTo(404));
    }


    private StubResponse createStubResponse(int status, String body) {
        StubResponse stubResponse = new StubResponse();
        stubResponse.setStatus(status);
        stubResponse.setBody(body);
        HttpHeaders headers = new HttpHeaders();
        headers.put(HEADER_KEY, Arrays.asList(HEADER_VALUE));
        stubResponse.setHeaders(headers);
        return stubResponse;
    }


    private StubRequest createStubRequest(String url, String method, String body) {
        StubRequest stubRequest = createStubRequest(url, method);
        stubRequest.setBody(body);
        return stubRequest;
    }

    private StubRequest createStubRequest(String url, String method) {
        StubRequest stubRequest = new StubRequest();
        stubRequest.setUrl(url);
        stubRequest.setMethod(method);
        return stubRequest;
    }

    private String createJsonString(String key, String value) {
        HashMap<String, Object> bodyMap = new HashMap<>();
        bodyMap.put(key, value);
        return gson.toJson(bodyMap);
    }

}
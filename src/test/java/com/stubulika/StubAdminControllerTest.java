package com.stubulika;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.stubulika.domain.StubRequest;
import com.stubulika.domain.StubResponse;
import com.stubulika.resource.StubWrapper;
import com.stubulika.resource.View;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.util.MatcherAssertionErrors.assertThat;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration (classes = Application.class)
@WebAppConfiguration
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@IntegrationTest
public class StubAdminControllerTest {

    public static final int STATUS_CODE = 200;
    private final String BASE_URL = "http://localhost:8080";
    private final String ADMIN_URL = BASE_URL+"/admin/";
    private Logger logger = LoggerFactory.getLogger(StubAdminControllerTest.class);


    @Test
    public void shouldSaveAStubAdminRequest() throws Exception {
        //given
        final String requestUrl = "aUrl";
        final String requestMethod = "POST";

        StubWrapper stubWrapper = createStubAdminRequest(requestUrl, requestMethod, 200);
        RestTemplate rest = new TestRestTemplate();

        //when
        ResponseEntity<?> response = rest.postForEntity(ADMIN_URL, stubWrapper,null);

        //then
        assertThat( response.getStatusCode() , equalTo(HttpStatus.CREATED));
    }

    @Test
    public void shouldNotSaveAStubAdminRequestWhenNoRequestUrlSupplied() throws Exception {
        //given
        final String requestUrl = null;
        final String requestMethod = "POST";

        StubWrapper stubWrapper = createStubAdminRequest(requestUrl, requestMethod, 200);
        RestTemplate rest = new TestRestTemplate();

        //when
        ResponseEntity<?> response = rest.postForEntity(ADMIN_URL, stubWrapper, null);

        //then
        assertThat( response.getStatusCode() , equalTo(HttpStatus.BAD_REQUEST));
    }

    @Test
    public void shouldNotSaveAStubAdminRequestWhenNoRequestMethodSupplied() throws Exception {
        //given
        final String requestUrl = "url";
        final String requestMethod = null;

        StubWrapper stubWrapper = createStubAdminRequest(requestUrl, requestMethod, 200);
        RestTemplate rest = new TestRestTemplate();

        //when
        ResponseEntity<?> response = rest.postForEntity(ADMIN_URL, stubWrapper, null);

        //then
        assertThat( response.getStatusCode() , equalTo(HttpStatus.BAD_REQUEST));
    }


    @Test
    public void shouldNotSaveAStubAdminRequestWhenNoResponseStatusCodeSupplied() throws Exception {
        //given
        final String requestUrl = "url";
        final String requestMethod = "POST";

        Integer statusCode = null;
        StubWrapper stubWrapper = createStubAdminRequest(requestUrl, requestMethod, statusCode);
        RestTemplate rest = new TestRestTemplate();

        //when
        ResponseEntity<?> response = rest.postForEntity(ADMIN_URL, stubWrapper, null);

        //then
        assertThat( response.getStatusCode() , equalTo(HttpStatus.BAD_REQUEST));
    }



    @Test
    public void shouldRetrieveAStubAdminRequests() throws Exception{
        //given
        StubWrapper stubAdminRequest1 = createStubAdminRequest("test1", "GET", 200);
        StubWrapper stubAdminRequest2 = createStubAdminRequest("test2", "GET", 200);
        StubWrapper stubAdminRequest3 = createStubAdminRequest("test3", "POST", 200);

        RestTemplate rest = new TestRestTemplate();

        rest.postForEntity(ADMIN_URL, stubAdminRequest1, null);
        rest.postForEntity(ADMIN_URL, stubAdminRequest2, null);
        rest.postForEntity(ADMIN_URL, stubAdminRequest3, null);

        LinkedList<StubWrapper> expectedStubsList = new LinkedList(Arrays.asList(stubAdminRequest1, stubAdminRequest2, stubAdminRequest3));
        String expectedJson = createExpectedJson(expectedStubsList);

        //when
        ResponseEntity<String> response = rest.getForEntity(ADMIN_URL, String.class);
        logger.debug("response:"+response);

        //then
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(response.getBody(), equalTo(expectedJson));

    }

    private String createExpectedJson(LinkedList<StubWrapper> expectedStubsList) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper.writeValueAsString(expectedStubsList);
    }

    @Test
    public void shouldRetrieveAStubAdminRequestsSummary() throws Exception {
        //given
        StubWrapper stubAdminRequest1 = createStubAdminRequest("test1", "GET", 200);
        StubWrapper stubAdminRequest2 = createStubAdminRequest("test2", "PUT", 200);
        StubWrapper stubAdminRequest3 = createStubAdminRequest("test3", "POST", 200);

        RestTemplate rest = new TestRestTemplate();

        rest.postForEntity(ADMIN_URL, stubAdminRequest1, null);
        rest.postForEntity(ADMIN_URL, stubAdminRequest2, null);
        rest.postForEntity(ADMIN_URL, stubAdminRequest3, null);

        LinkedList<StubWrapper> expectedStubsList = new LinkedList(Arrays.asList(stubAdminRequest1, stubAdminRequest2, stubAdminRequest3));
        String expectedJson = createExpectedSummaryJson(expectedStubsList);

        //when
        ResponseEntity<String> response = rest.getForEntity(ADMIN_URL + "/summary", String.class);
        logger.debug("response:" + response);

        //then
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(response.getBody(), equalTo(expectedJson));

    }

    private String createExpectedSummaryJson(LinkedList<StubWrapper> expectedStubsList) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(MapperFeature.DEFAULT_VIEW_INCLUSION);
        mapper.setConfig(mapper.getSerializationConfig()
                .withView(View.Summary.class));
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper.writeValueAsString(expectedStubsList);
    }

    @Test
    public void shouldDeleteStubAdminRequest() throws Exception{
        //given
        StubWrapper stubWrapper = createStubAdminRequest("test1", "GET", STATUS_CODE);
        RestTemplate rest = new TestRestTemplate();
        rest.postForEntity(ADMIN_URL, stubWrapper, null);

        //when
        HttpEntity<?> requestEntity = new HttpEntity<Object>(stubWrapper, new HttpHeaders());

        //when
        ResponseEntity<String> deleteResponse = rest.exchange(ADMIN_URL, HttpMethod.DELETE, requestEntity, String.class);
        logger.debug("shouldDeleteStubAdminRequest() delete response:"+deleteResponse);


        rest.delete(ADMIN_URL);
        ResponseEntity<List> response = rest.getForEntity(ADMIN_URL, List.class);
        logger.debug("shouldDeleteStubAdminRequest() get response:"+response);

        //then
        assertThat(response.getBody().size(), equalTo(0));

    }

    private StubWrapper createStubAdminRequest(String url, String method, Integer statusCode) {
        StubRequest stubRequest = new StubRequest();
        stubRequest.setUrl(url);
        stubRequest.setMethod(method);
        stubRequest.setHeaders(new HttpHeaders());

        StubResponse stubResponse = new StubResponse();
        stubResponse.setStatus(statusCode);

        HashMap<String, Object> responseBody = new HashMap<>();
        responseBody.put("foo", "bar");
        Gson gson = new Gson();
        stubResponse.setBody(gson.toJson(responseBody));

        HttpHeaders headers = new HttpHeaders();
        headers.put("key", Arrays.asList("value"));
        stubResponse.setHeaders(headers);


        StubWrapper stubWrapper = new StubWrapper();
        stubWrapper.setRequest(stubRequest);
        stubWrapper.setResponse(stubResponse);
        return stubWrapper;
    }
}
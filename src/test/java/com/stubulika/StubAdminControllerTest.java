package com.stubulika;

import com.google.gson.Gson;
import com.stubulika.domain.StubRequest;
import com.stubulika.domain.StubResponse;
import com.stubulika.resource.StubAdminRequest;
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

        StubAdminRequest stubAdminRequest = createStubAdminRequest(requestUrl, requestMethod, 200);
        RestTemplate rest = new TestRestTemplate();

        //when
        ResponseEntity<?> response = rest.postForEntity(ADMIN_URL, stubAdminRequest,null);

        //then
        assertThat( response.getStatusCode() , equalTo(HttpStatus.CREATED));
    }

    @Test
    public void shouldNotSaveAStubAdminRequestWhenNoRequestUrlSupplied() throws Exception {
        //given
        final String requestUrl = null;
        final String requestMethod = "POST";

        StubAdminRequest stubAdminRequest = createStubAdminRequest(requestUrl, requestMethod, 200);
        RestTemplate rest = new TestRestTemplate();

        //when
        ResponseEntity<?> response = rest.postForEntity(ADMIN_URL, stubAdminRequest, null);

        //then
        assertThat( response.getStatusCode() , equalTo(HttpStatus.BAD_REQUEST));
    }

    @Test
    public void shouldNotSaveAStubAdminRequestWhenNoRequestMethodSupplied() throws Exception {
        //given
        final String requestUrl = "url";
        final String requestMethod = null;

        StubAdminRequest stubAdminRequest = createStubAdminRequest(requestUrl, requestMethod, 200);
        RestTemplate rest = new TestRestTemplate();

        //when
        ResponseEntity<?> response = rest.postForEntity(ADMIN_URL, stubAdminRequest, null);

        //then
        assertThat( response.getStatusCode() , equalTo(HttpStatus.BAD_REQUEST));
    }


    @Test
    public void shouldNotSaveAStubAdminRequestWhenNoResponseStatusCodeSupplied() throws Exception {
        //given
        final String requestUrl = "url";
        final String requestMethod = "POST";

        Integer statusCode = null;
        StubAdminRequest stubAdminRequest = createStubAdminRequest(requestUrl, requestMethod, statusCode);
        RestTemplate rest = new TestRestTemplate();

        //when
        ResponseEntity<?> response = rest.postForEntity(ADMIN_URL, stubAdminRequest, null);

        //then
        assertThat( response.getStatusCode() , equalTo(HttpStatus.BAD_REQUEST));
    }



    @Test
    public void shouldRetrieveAStubAdminRequests() throws Exception{
        //given
        StubAdminRequest stubAdminRequest1 = createStubAdminRequest("test1", "GET", 200);
        StubAdminRequest stubAdminRequest2 = createStubAdminRequest("test2", "GET", 200);
        StubAdminRequest stubAdminRequest3 = createStubAdminRequest("test3", "POST", 200);

        RestTemplate rest = new TestRestTemplate();

        ResponseEntity<?> response1 = rest.postForEntity(ADMIN_URL, stubAdminRequest1, null);
        ResponseEntity<?> response2 = rest.postForEntity(ADMIN_URL, stubAdminRequest2, null);
        ResponseEntity<?> response3 = rest.postForEntity(ADMIN_URL, stubAdminRequest3, null);

        //when
        ResponseEntity<List> response = rest.getForEntity(ADMIN_URL, List.class);
        logger.debug("response:"+response);

        //then
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(response.getBody().size(), equalTo(3));

    }

    @Test
    public void shouldDeleteStubAdminRequest() throws Exception{
        //given
        StubAdminRequest stubAdminRequest = createStubAdminRequest("test1", "GET", STATUS_CODE);
        RestTemplate rest = new TestRestTemplate();
        rest.postForEntity(ADMIN_URL, stubAdminRequest, null);

        //when
        HttpEntity<?> requestEntity = new HttpEntity<Object>(stubAdminRequest, new HttpHeaders());

        //when
        ResponseEntity<String> deleteResponse = rest.exchange(ADMIN_URL, HttpMethod.DELETE, requestEntity, String.class);
        logger.debug("shouldDeleteStubAdminRequest() delete response:"+deleteResponse);


        rest.delete(ADMIN_URL);
        ResponseEntity<List> response = rest.getForEntity(ADMIN_URL, List.class);
        logger.debug("shouldDeleteStubAdminRequest() get response:"+response);

        //then
        assertThat(response.getBody().size(), equalTo(0));

    }

    private StubAdminRequest createStubAdminRequest(String url, String method, Integer statusCode) {
        StubRequest stubRequest = new StubRequest();
        stubRequest.setUrl(url);
        stubRequest.setMethod(method);

        StubResponse stubResponse = new StubResponse();
        stubResponse.setStatus(statusCode);

        HashMap<String, Object> responseBody = new HashMap<>();
        responseBody.put("foo", "bar");
        Gson gson = new Gson();
        stubResponse.setBody(gson.toJson(responseBody));

        HttpHeaders headers = new HttpHeaders();
        headers.put("key", Arrays.asList("value"));
        stubResponse.setHeaders(headers);


        StubAdminRequest stubAdminRequest = new StubAdminRequest();
        stubAdminRequest.setRequest(stubRequest);
        stubAdminRequest.setResponse(stubResponse);
        return stubAdminRequest;
    }
}
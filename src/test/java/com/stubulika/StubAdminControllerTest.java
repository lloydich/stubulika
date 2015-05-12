package com.stubulika;

import com.google.gson.Gson;
import com.stubulika.domain.StubRequest;
import com.stubulika.domain.StubResponse;
import com.stubulika.resource.StubAdminRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    final String BASE_URL = "http://localhost:8080";
    final String ADMIN_URL = BASE_URL+"/admin/";


    @Test
    public void shouldSaveAStubAdminRequest() throws Exception {

        final String requestUrl = "aUrl";
        final String requestMethod = "POST";

        StubAdminRequest stubAdminRequest = createStubAdminRequest(requestUrl, requestMethod);
        RestTemplate rest = new TestRestTemplate();

        ResponseEntity<?> response =
                rest.postForEntity(ADMIN_URL, stubAdminRequest,null);
        assertThat( response.getStatusCode() , equalTo(HttpStatus.CREATED));
    }



    @Test
    public void shouldRetrieveAStubAdminRequests() throws Exception{
        StubAdminRequest stubAdminRequest1 = createStubAdminRequest("test1", "GET");
        StubAdminRequest stubAdminRequest2 = createStubAdminRequest("test2", "GET");
        StubAdminRequest stubAdminRequest3 = createStubAdminRequest("test3", "POST");

        RestTemplate rest = new TestRestTemplate();

        ResponseEntity<?> response1 = rest.postForEntity(ADMIN_URL, stubAdminRequest1,null);
        ResponseEntity<?> response2 = rest.postForEntity(ADMIN_URL, stubAdminRequest2,null);
        ResponseEntity<?> response3 = rest.postForEntity(ADMIN_URL, stubAdminRequest3,null);


        ResponseEntity<List> response = rest.getForEntity(ADMIN_URL, List.class);
        System.out.println("response:"+response);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(response.getBody().size(), equalTo(3));

    }

    private StubAdminRequest createStubAdminRequest(String url, String method) {
        StubRequest stubRequest = new StubRequest();
        stubRequest.setUrl(url);
        stubRequest.setMethod(method);

        StubResponse stubResponse = new StubResponse();
        stubResponse.setStatus(200);

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
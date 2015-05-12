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
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.HashMap;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.util.MatcherAssertionErrors.assertThat;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration (classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class StubAdminControllerTest {
    final String BASE_URL = "http://localhost:8080";
    final String ADMIN_URL = BASE_URL+"/admin/";


    @Test
    public void testPost() throws Exception {

        final String URL = "aUrl";
        final String METHOD = "POST";


        StubRequest stubRequest = new StubRequest();
        stubRequest.setUrl(URL);
        stubRequest.setMethod(METHOD);

        StubResponse  stubResponse = new StubResponse();

        HashMap<String, Object> body = new HashMap<>();
        body.put("foo", "bar");
        Gson gson = new Gson();
        stubResponse.setBody(gson.toJson(body));

        HttpHeaders headers = new HttpHeaders();
        headers.put("key", Arrays.asList("value"));
        stubResponse.setHeaders(headers);


        StubAdminRequest stubAdminRequest = new StubAdminRequest();
        stubAdminRequest.setRequest(stubRequest);
        stubAdminRequest.setResponse(stubResponse);

        RestTemplate rest = new TestRestTemplate();

        ResponseEntity<?> response =
                rest.postForEntity(ADMIN_URL, stubAdminRequest,null);
        assertThat( response.getStatusCode() , equalTo(HttpStatus.CREATED));
    }



}
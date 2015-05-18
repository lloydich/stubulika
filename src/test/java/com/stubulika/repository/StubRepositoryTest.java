package com.stubulika.repository;

import com.google.gson.Gson;
import com.stubulika.Application;
import com.stubulika.domain.StubRequest;
import com.stubulika.domain.StubRequestBuilder;
import com.stubulika.domain.StubResponse;
import com.stubulika.resource.StubWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationContextLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

@RunWith(SpringJUnit4ClassRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ContextConfiguration(classes = Application.class, loader = SpringApplicationContextLoader.class)
@IntegrationTest
public class StubRepositoryTest {

    public static final String TEST_URL = "testUrl";
    public static final String TEST_METHOD = "testMethod";
    private Logger logger = LoggerFactory.getLogger(StubRepositoryTest.class);

    @Resource
    private Map<StubRequest, StubResponse> storeMap;


    @Autowired
    private StubRepository stubRepo;


    @Test
    public void shouldSaveStubRequestResponse() throws Exception {
        //given
        StubRequest stubRequest = new StubRequestBuilder()
                .withUrl(TEST_URL)
                .withMethod(TEST_METHOD)
                .build();
        StubResponse stubResponse = createStubResponse("foo", "bar");

        //when
        stubRepo.save(stubRequest, stubResponse);


        //then
        StubResponse actualResponse = storeMap.get(stubRequest);
        assertThat(actualResponse, equalTo(stubResponse));
    }


    @Test
    public void shouldRetrieveStubResponse() {
        //given
        StubRequest stubRequest = new StubRequestBuilder()
                .withUrl(TEST_URL)
                .withMethod(TEST_METHOD)
                .build();

        StubResponse stubResponse = createStubResponse("foo", "bar");
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.put("Content-Type", Arrays.asList("application/json"));
        stubResponse.setHeaders(responseHeaders);
        storeMap.put(stubRequest, stubResponse);


        //when
        StubResponse actual = stubRepo.find(stubRequest);


        //then
        assertThat(actual, equalTo(stubResponse));
    }

    @Test
    public void shouldRetrieveStubResponseIfHeadersInSavedStubRequestExistInQueriedStubRequest() {
        //given
        HttpHeaders headers = new HttpHeaders() {{
            put("Accept", Arrays.asList("application/json"));
        }};
        StubRequest savedStubRequest = new StubRequestBuilder()
                .withUrl(TEST_URL)
                .withMethod(TEST_METHOD)
                .withHeaders(headers)
                .build();
        StubResponse stubResponse = createStubResponse("foo", "bar");

        storeMap.put(savedStubRequest, stubResponse);

        HttpHeaders queryHeaders = new HttpHeaders() {{
            put("Accept", Arrays.asList("application/json"));
            put("Connection", Arrays.asList("keep-alive"));
        }};
        StubRequest queryStubRequest = new StubRequestBuilder()
                .withUrl(TEST_URL)
                .withMethod(TEST_METHOD)
                .withHeaders(queryHeaders)
                .build();


        //when
        StubResponse actual = stubRepo.find(queryStubRequest);


        //then
        assertThat(actual, equalTo(stubResponse));
    }

    @Test
    public void shouldNotRetrieveStubResponseIfHeadersInSavedStubRequestDoNotExistInQueriedStubRequest() {
        //given
        HttpHeaders headers = new HttpHeaders() {{
            put("Accept", Arrays.asList("application/xml"));
        }};
        StubRequest savedStubRequest = new StubRequestBuilder()
                .withUrl(TEST_URL)
                .withMethod(TEST_METHOD)
                .withHeaders(headers)
                .build();
        StubResponse stubResponse = createStubResponse("foo", "bar");

        storeMap.put(savedStubRequest, stubResponse);

        HttpHeaders queryHeaders = new HttpHeaders(){{
                put("Accept", Arrays.asList("application/json"));
                put("Connection", Arrays.asList("keep-alive"));
            }};
        StubRequest queryStubRequest = new StubRequestBuilder()
                .withUrl(TEST_URL)
                .withMethod(TEST_METHOD)
                .withHeaders(queryHeaders)
                .build();


        //when
        StubResponse actual = stubRepo.find(queryStubRequest);


        //then
        assertThat(actual, equalTo(null));
    }

    @Test
    public void shouldNotRetrieveStubResponseIfHeadersInQueriedStubRequestDoNotExistInSavedStubRequest() {
        //given
        HttpHeaders headers = new HttpHeaders() {{
            put("Accept", Arrays.asList("application/json"));
            put("Connection", Arrays.asList("keep-alive"));
        }};
        StubRequest savedStubRequest = new StubRequestBuilder()
                .withUrl(TEST_URL)
                .withMethod(TEST_METHOD)
                .withHeaders(headers)
                .build();
        StubResponse stubResponse = createStubResponse("foo", "bar");

        storeMap.put(savedStubRequest, stubResponse);

        HttpHeaders queryHeaders = new HttpHeaders() {{
            put("Accept", Arrays.asList("application/xml"));
        }};

        StubRequest queryStubRequest = new StubRequestBuilder()
                .withUrl(TEST_URL)
                .withMethod(TEST_METHOD)
                .withHeaders(queryHeaders)
                .build();


        //when
        StubResponse actual = stubRepo.find(queryStubRequest);


        //then
        assertThat(actual, equalTo(null));
    }

    @Test
    public void shouldNotRetrieveStubResponseIfHeadersInSavedStubRequestButNoneInQueriedStubRequest() {
        //given
        HttpHeaders headers = new HttpHeaders() {{
            put("Accept", Arrays.asList("application/xml"));
        }};
        StubRequest savedStubRequest = new StubRequestBuilder()
                .withUrl(TEST_URL)
                .withMethod(TEST_METHOD)
                .withHeaders(headers)
                .build();
        StubResponse stubResponse = createStubResponse("foo", "bar");

        storeMap.put(savedStubRequest, stubResponse);

        StubRequest queryStubRequest = new StubRequestBuilder()
                .withUrl(TEST_URL)
                .withMethod(TEST_METHOD)
                .withHeaders(null)
                .build();


        //when
        StubResponse actual = stubRepo.find(queryStubRequest);


        //then
        assertThat(actual, equalTo(null));
    }

    @Test
    public void shouldNotSaveAgainSameStubRequestResponseWhenSavedPreviously() throws Exception {
        //given
        StubRequest stubRequest = new StubRequestBuilder()
                .withUrl(TEST_URL)
                .withMethod(TEST_METHOD)
                .withHeaders(null)
                .build();
        StubResponse stubResponse = createStubResponse("foo", "bar");

        //when
        stubRepo.save(stubRequest, stubResponse);
        stubRepo.save(stubRequest, stubResponse);

        //then
        assertThat(storeMap.size(), equalTo(1));
    }

    @Test
    public void shouldOverwriteStubResponseWhenSameStubRequestSavedPreviously() throws Exception {
        //given
        StubRequest stubRequest = new StubRequestBuilder()
                .withUrl(TEST_URL)
                .withMethod(TEST_METHOD)
                .withHeaders(null)
                .build();
        StubResponse stubResponse = createStubResponse("foo", "aBody");
        StubResponse stubResponse2 = createStubResponse("foo", "aBody2", 500);

        //when
        stubRepo.save(stubRequest, stubResponse);
        stubRepo.save(stubRequest, stubResponse2);


        //then
        assertThat(storeMap.size(), equalTo(1));

        StubResponse actual = storeMap.get(stubRequest);
        assertThat(actual, equalTo(stubResponse2));

    }


    @Test
    public void shouldSaveMoreThanOneStubRequestResponse() throws Exception {
        //given
        StubRequest stubRequest1 = new StubRequestBuilder()
                .withUrl("aUrl1")
                .withMethod("GET")
                .withHeaders(null)
                .build();
        StubRequest stubRequest2 = new StubRequestBuilder()
                .withUrl("aUrl2")
                .withMethod("GET")
                .withHeaders(null)
                .build();
        StubResponse stubResponse1 = createStubResponse("foo", "aBody1");
        StubResponse stubResponse2 = createStubResponse("foo", "aBody2");


        //when
        stubRepo.save(stubRequest1, stubResponse1);
        stubRepo.save(stubRequest2, stubResponse2);


        //then
        assertThat(storeMap.size(), equalTo(2));
    }

    @Test
    public void shouldRetrieveAllStubAdminRequests() {
        //given
        StubRequest stubRequest1 = new StubRequestBuilder()
                .withUrl("aUrl1")
                .withMethod("aMethod")
                .withCreated(LocalDateTime.now())
                .build();

        StubResponse stubResponse1 = createStubResponse("responseKey1", "responseValue1");
        storeMap.put(stubRequest1, stubResponse1);

        StubRequest stubRequest2 = new StubRequestBuilder()
                .withUrl("aUrl2")
                .withMethod("aMethod")
                .withCreated(LocalDateTime.now())
                .build();

        StubResponse stubResponse2 = createStubResponse("responseKey2", "responseValue2");
        storeMap.put(stubRequest2, stubResponse2);

        StubRequest stubRequest3 = new StubRequestBuilder()
                .withUrl("aUrl3")
                .withMethod("aMethod")
                .withCreated(LocalDateTime.now())
                .build();
        StubResponse stubResponse3 = createStubResponse("responseKey3", "responseValue3");
        storeMap.put(stubRequest3, stubResponse3);

        //when
        List<StubWrapper> actual = stubRepo.findAll();


        //then
        assertThat(actual.size(), equalTo(3));
        assertThat(actual.contains(new StubWrapper(stubRequest1, stubResponse1)), equalTo(true));
        assertThat(actual.contains(new StubWrapper(stubRequest2, stubResponse2)), equalTo(true));
        assertThat(actual.contains(new StubWrapper(stubRequest3, stubResponse3)), equalTo(true));
//        List<StubAdminRequest> stubAdminRequests = new ArrayList<>();
//        stubAdminRequests.add(new StubAdminRequest(stubRequest1, stubResponse1));
//        assertThat(actual, containsInAnyOrder(stubAdminRequests));

    }

    private StubResponse createStubResponse(String bodyJsonKey, String bodyJsonValue) {
        return createStubResponse(bodyJsonKey, bodyJsonValue, 200);
    }

    private StubResponse createStubResponse(String bodyJsonKey, String bodyJsonValue, int status) {
        StubResponse stubResponse = new StubResponse();
        HashMap<String, Object> body = new HashMap<>();
        body.put(bodyJsonKey, bodyJsonValue);
        Gson gson = new Gson();
        stubResponse.setBody(gson.toJson(body));
        stubResponse.setStatus(status);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.put("Content-Type", Arrays.asList("application/json"));
        stubResponse.setHeaders(responseHeaders);
        return stubResponse;
    }

//    private StubRequest createStubRequest(String url, String method, HttpHeaders headers) {
//        StubRequest stubRequest = new StubRequest();
//        stubRequest.setUrl(url);
//        stubRequest.setMethod(method);
//        stubRequest.setHeaders(headers);
//        stubRequest.setCreated(LocalDateTime.now());
//        return stubRequest;
//    }

}
package com.stubulika.repository;

import com.google.gson.Gson;
import com.stubulika.Application;
import com.stubulika.domain.StubRequest;
import com.stubulika.domain.StubResponse;
import com.stubulika.resource.StubAdminRequest;
import org.hamcrest.Matchers;
import org.junit.Ignore;
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
import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.core.IsEqual.equalTo;

@RunWith(SpringJUnit4ClassRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ContextConfiguration(classes = Application.class, loader = SpringApplicationContextLoader.class)
@IntegrationTest
public class StubAdminRepositoryTest {
    @Resource
    private Map<StubRequest, StubResponse> storeMap;


    @Autowired
    private StubAdminRepository stubRepo;


    @Test
    public void shouldSaveStubRequestResponse() throws Exception {
        //given
        StubRequest stubRequest = createStubRequest("testUrl", "testMethod");
        StubResponse stubResponse = createStubResponse("foo", "bar");

        //when
        stubRepo.save(stubRequest, stubResponse);


        //then
        StubResponse actualResponse = storeMap.get(stubRequest);
        assertThat(actualResponse, equalTo(stubResponse));
    }




    @Test
    public void shouldRetrieve(){
        //given
        StubRequest stubRequest = createStubRequest("testUrl", "testMethod");

        StubResponse stubResponse = createStubResponse("foo", "bar");
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.put("Content-Type", Arrays.asList("application/json"));
        stubResponse.setHeaders(responseHeaders);
        storeMap.put(stubRequest,stubResponse);
        System.out.println("foobar:"+stubResponse);


        //when
        StubResponse actual = stubRepo.find(stubRequest);


        //then
        assertThat(actual, equalTo(stubResponse));
    }

    @Test
    public void shouldNotSaveAgainSameStubRequestResponseWhenSavedPreviously() throws Exception {
        //given
        StubRequest stubRequest = createStubRequest("testUrl", "testMethod");
        StubResponse stubResponse = createStubResponse("foo", "bar");

        //when
        stubRepo.save(stubRequest, stubResponse);
        stubRepo.save(stubRequest, stubResponse);

        //then
        assertThat(storeMap.size(),equalTo(1));
    }

    @Test
    public void shouldOverwriteStubResponseWhenSameStubRequestSavedPreviously() throws Exception {
        //given
        StubRequest stubRequest = createStubRequest("testUrl", "testMethod");
        StubResponse stubResponse =  createStubResponse("foo", "aBody");
        StubResponse stubResponse2 =  createStubResponse("foo", "aBody2", 500);

        //when
        stubRepo.save(stubRequest, stubResponse);
        stubRepo.save(stubRequest, stubResponse2);


        //then
        assertThat(storeMap.size(),equalTo(1));

        StubResponse actual = storeMap.get(stubRequest);
        assertThat(actual, equalTo(stubResponse2));

    }



    @Test
    public void shouldSaveMoreThanOneStubRequestResponse() throws Exception {
        //given
        StubRequest stubRequest1 = createStubRequest("aUrl1", "GET");
        StubRequest stubRequest2 = createStubRequest("aUrl2", "GET");
        StubResponse stubResponse1 = createStubResponse("foo", "aBody1");
        StubResponse stubResponse2 = createStubResponse("foo", "aBody2");


        //when
        stubRepo.save(stubRequest1, stubResponse1);
        stubRepo.save(stubRequest2, stubResponse2);


        //then
        assertThat(storeMap.size(),equalTo(2));
    }

    @Test
    public void shouldRetrieveAll(){
        //given
        StubRequest stubRequest1 = createStubRequest("aUrl1","aMethod" );
        StubResponse stubResponse1 = createStubResponse("responseKey1", "responseValue1");
        storeMap.put(stubRequest1,stubResponse1);

        StubRequest stubRequest2 = createStubRequest("aUrl2","aMethod" );
        StubResponse stubResponse2 = createStubResponse("responseKey2", "responseValue2");
        storeMap.put(stubRequest2,stubResponse2);

        StubRequest stubRequest3 = createStubRequest("aUrl3","aMethod" );
        StubResponse stubResponse3 = createStubResponse("responseKey3", "responseValue3");
        storeMap.put(stubRequest3,stubResponse3);

        //when
        List<StubAdminRequest> actual = stubRepo.findAll();


        //then
        assertThat(actual.size(), equalTo(3));
        assertThat(actual.contains(new StubAdminRequest(stubRequest1, stubResponse1)), equalTo(true));
        assertThat(actual.contains(new StubAdminRequest(stubRequest2, stubResponse2)), equalTo(true));
        assertThat(actual.contains(new StubAdminRequest(stubRequest3, stubResponse3)), equalTo(true));
//        List<StubAdminRequest> stubAdminRequests = new ArrayList<>();
//        stubAdminRequests.add(new StubAdminRequest(stubRequest1, stubResponse1));
//        assertThat(actual, containsInAnyOrder(stubAdminRequests));

    }

    private StubResponse createStubResponse(String bodyJsonKey, String bodyJsonValue){
           return createStubResponse( bodyJsonKey,  bodyJsonValue, 200);
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

    private StubRequest createStubRequest(String url, String method) {
        StubRequest stubRequest = new StubRequest();
        stubRequest.setUrl(url);
        stubRequest.setMethod(method);
        return stubRequest;
    }

}
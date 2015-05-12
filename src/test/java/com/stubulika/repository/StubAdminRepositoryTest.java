package com.stubulika.repository;

import com.google.gson.Gson;
import com.stubulika.Application;
import com.stubulika.domain.StubRequest;
import com.stubulika.domain.StubResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationContextLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

@RunWith(SpringJUnit4ClassRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ContextConfiguration(classes = Application.class, loader = SpringApplicationContextLoader.class)
public class StubAdminRepositoryTest {
    @Resource
    private Map<StubRequest, StubResponse> storeMap;


    @Autowired
    private StubAdminRepository stubRepo;


    @Test
    public void shouldSaveStubRequestResponse() throws Exception {
        //given
        StubRequest stubRequest = new StubRequest();
        stubRequest.setUrl("aUrl");
        stubRequest.setMethod("GET");

        StubResponse stubResponse = new StubResponse();
        HashMap<String, Object> body = new HashMap<>();
        body.put("foo","bar");
        Gson gson = new Gson();
        stubResponse.setBody(gson.toJson(body));
        stubResponse.setStatus(200);

        //when
        stubRepo.save(stubRequest, stubResponse);


        //then
        StubResponse actualResponse = storeMap.get(stubRequest);
        assertThat(actualResponse, equalTo(stubResponse));
    }



    @Test
    public void shouldRetrieve(){
        //given
        StubRequest stubRequest = new StubRequest();
        stubRequest.setUrl("aUrl");
        stubRequest.setMethod("GET");

        StubResponse stubResponse = new StubResponse();
        HashMap<String, Object> body = new HashMap<>();
        body.put("foo","bar");
        Gson gson = new Gson();
        stubResponse.setBody(gson.toJson(body));
        stubResponse.setStatus(200);
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
        StubRequest stubRequest = new StubRequest();
        stubRequest.setUrl("aUrl");
        stubRequest.setMethod("GET");

        StubResponse stubResponse = new StubResponse();
        HashMap<String, Object> body = new HashMap<>();
        body.put("foo","bar");
        Gson gson = new Gson();
        stubResponse.setBody(gson.toJson(body));
        stubResponse.setStatus(200);

        //when
        stubRepo.save(stubRequest, stubResponse);
        stubRepo.save(stubRequest, stubResponse);


        //then
        assertThat(storeMap.size(),equalTo(1));
    }

    @Test
    public void shouldOverwriteStubResponseWhenSameStubRequestSavedPreviously() throws Exception {
        //given
        StubRequest stubRequest = new StubRequest();
        stubRequest.setUrl("aUrl");
        stubRequest.setMethod("GET");

        StubResponse stubResponse = new StubResponse();
        HashMap<String, Object> body = new HashMap<>();
        body.put("foo","aBody");
        Gson gson = new Gson();
        stubResponse.setBody(gson.toJson(body));
        stubResponse.setStatus(200);

        StubResponse stubResponse2 = new StubResponse();
        HashMap<String, Object> body2 = new HashMap<>();
        body2.put("foo","aBody2");
        stubResponse2.setBody(gson.toJson(body2));
        stubResponse2.setStatus(500);


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
        StubRequest stubRequest1 = new StubRequest();
        stubRequest1.setUrl("aUrl1");
        stubRequest1.setMethod("GET");

        StubRequest stubRequest2 = new StubRequest();
        stubRequest2.setUrl("aUrl2");
        stubRequest2.setMethod("GET");

        StubResponse stubResponse1 = new StubResponse();
        HashMap<String, Object> body = new HashMap<>();
        body.put("foo","aBody1");
        stubResponse1.setStatus(200);

        StubResponse stubResponse2 = new StubResponse();
        HashMap<String, Object> body2 = new HashMap<>();
        body2.put("foo","bar");
        stubResponse2.setStatus(200);


        //when
        stubRepo.save(stubRequest1, stubResponse1);
        stubRepo.save(stubRequest2, stubResponse2);


        //then
        assertThat(storeMap.size(),equalTo(2));
    }

}
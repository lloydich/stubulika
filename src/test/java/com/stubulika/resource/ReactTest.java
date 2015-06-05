package com.stubulika.resource;

import com.google.gson.Gson;
import com.stubulika.domain.StubRequest;
import com.stubulika.domain.StubRequestBuilder;
import com.stubulika.domain.StubResponse;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;
import org.springframework.http.HttpHeaders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertThat;

public class ReactTest {

    private final String BASE_URL = "http://localhost:8080";
    private final String ADMIN_URL = BASE_URL+"/admin/";
    private static final String HEADER_KEY = "Content-Type";
    private static final String HEADER_VALUE = "application/json; charset=UTF-8";
    public static final String NO_BODY = null;
    public static final String PUT_METHOD = "PUT";
    private static final String RESOURCE_URL = "aUrl";
    private static final int OK_STATUS = 200;
    private Gson gson = new Gson();


    @Test
    public void testRenderStubBox() throws Exception {
        //given
        String body = createJsonString("foo", "bar");
        StubRequest stubRequest = new StubRequestBuilder()
                .withUrl(RESOURCE_URL + "/2")
                .withMethod(PUT_METHOD)
                .withBody(body)
                .build();
        StubResponse stubResponse = createStubResponse(OK_STATUS, NO_BODY);

        List<StubWrapper> stubs = new ArrayList<>();
        stubs.add(new StubWrapper(stubRequest, stubResponse));

        React react = new React();
        String html = react.renderStubBox(stubs);

        assertThat(html, startsWith("<div"));

        Document doc = Jsoup.parse(html);
        assertThat(doc.select("div.stub").size(), is(1));
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

    private String createJsonString(String key, String value) {
        HashMap<String, Object> bodyMap = new HashMap<>();
        bodyMap.put(key, value);
        return gson.toJson(bodyMap);
    }

}
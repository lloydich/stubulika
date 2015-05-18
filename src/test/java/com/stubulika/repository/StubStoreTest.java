package com.stubulika.repository;

import com.stubulika.domain.StubRequest;
import com.stubulika.domain.StubRequestBuilder;
import org.junit.Test;
import org.springframework.http.HttpHeaders;

import java.util.Arrays;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class StubStoreTest {

    public static final String URL = "TESTURL";
    public static final String METHOD = "GET";


    @Test
    public void shouldNotFindSavedHeadersIsNullWhenQueryIsNotNull() throws Exception {
        StubRequest queryStubRequest = new StubRequestBuilder()
                .withUrl(URL)
                .withMethod(METHOD)
                .withHeaders(null)
                .build();

        HttpHeaders headers2 = new HttpHeaders() {{
            put("AnotherHeader", Arrays.asList("application/json"));
        }};
        StubRequest savedStubRequest = new StubRequestBuilder()
                .withUrl(URL)
                .withMethod(METHOD)
                .withHeaders(headers2)
                .build();

        assertTrue(StubStore.queryHeadersNullSavedNot(queryStubRequest.getHeaders()).test(savedStubRequest));
    }


    @Test
    public void shouldShowHeadersDoNotMatch() throws Exception {
        HttpHeaders headers = new HttpHeaders() {{
            put("AHeader", Arrays.asList("application/xml"));
        }};
        StubRequest queryStubRequest = new StubRequestBuilder()
                .withUrl(URL)
                .withMethod(METHOD)
                .withHeaders(headers)
                .build();

        HttpHeaders headers2 = new HttpHeaders() {{
            put("AnotherHeader", Arrays.asList("application/json"));
        }};
        StubRequest savedStubRequest = new StubRequestBuilder()
                .withUrl(URL)
                .withMethod(METHOD)
                .withHeaders(headers2)
                .build();

        assertFalse("Headers should not match ", StubStore.headersMatch(queryStubRequest.getHeaders()).test(savedStubRequest));
    }

    @Test
    public void shouldShowHeadersDoNotMatchWhenQueryIsNullAndSavedIsNot() throws Exception {
        StubRequest queryStubRequest = new StubRequestBuilder()
                .withUrl(URL)
                .withMethod(METHOD)
                .withHeaders(null)
                .build();

        HttpHeaders headers2 = new HttpHeaders() {{
            put("AnotherHeader", Arrays.asList("application/json"));
        }};
        StubRequest savedStubRequest = new StubRequestBuilder()
                .withUrl(URL)
                .withMethod(METHOD)
                .withHeaders(headers2)
                .build();

        assertFalse("Headers should not match ", StubStore.headersMatch(queryStubRequest.getHeaders()).test(savedStubRequest));
    }

    @Test
    public void shouldShowHeadersMatchWhenSavedIsNullAndTheQueryNot() throws Exception {
        HttpHeaders headers = new HttpHeaders() {{
            put("AnotherHeader", Arrays.asList("application/json"));
        }};
        StubRequest queryStubRequest = new StubRequestBuilder()
                .withUrl(URL)
                .withMethod(METHOD)
                .withHeaders(headers)
                .build();

        StubRequest savedStubRequest = new StubRequestBuilder()
                .withUrl(URL)
                .withMethod(METHOD)
                .withHeaders(null)
                .build();

        assertTrue("Headers should match due to Saved's empty headers is a subset of Query Headers", StubStore.headersMatch(queryStubRequest.getHeaders()).test(savedStubRequest));
    }

    @Test
    public void shouldShowHeadersMatch() throws Exception {
        HttpHeaders headers = new HttpHeaders() {{
            put("AHeader", Arrays.asList("aValue"));
        }};
        StubRequest queryStubRequest = new StubRequestBuilder()
                .withUrl(URL)
                .withMethod(METHOD)
                .withHeaders(headers)
                .build();

        HttpHeaders headers2 = new HttpHeaders() {{
            put("AHeader", Arrays.asList("aValue"));
        }};
        StubRequest savedStubRequest = new StubRequestBuilder()
                .withUrl(URL)
                .withMethod(METHOD)
                .withHeaders(headers2)
                .build();

        assertTrue("Headers should  match ", StubStore.headersMatch(queryStubRequest.getHeaders()).test(savedStubRequest));
    }

    @Test
    public void shouldShowHeadersMatchWhenBothNull() throws Exception {
        StubRequest queryStubRequest = new StubRequestBuilder()
                .withUrl(URL)
                .withMethod(METHOD)
                .withHeaders(null)
                .build();

        StubRequest savedStubRequest = new StubRequestBuilder()
                .withUrl(URL)
                .withMethod(METHOD)
                .withHeaders(null)
                .build();

        assertTrue("Headers should  match", StubStore.headersMatch(queryStubRequest.getHeaders()).test(savedStubRequest));
    }

    @Test
    public void shouldShowHeadersMatchOnSubset() throws Exception {
        HttpHeaders headers = new HttpHeaders() {{
            put("AHeader", Arrays.asList("aValue"));
            put("AHeader2", Arrays.asList("aValue2"));
        }};
        StubRequest queryStubRequest = new StubRequestBuilder()
                .withUrl(URL)
                .withMethod(METHOD)
                .withHeaders(headers)
                .build();

        HttpHeaders headers2 = new HttpHeaders() {{
            put("AHeader", Arrays.asList("aValue"));
        }};
        StubRequest savedStubRequest = new StubRequestBuilder()
                .withUrl(URL)
                .withMethod(METHOD)
                .withHeaders(headers2)
                .build();


        assertTrue("Subset of headers should  match", StubStore.headersMatch(queryStubRequest.getHeaders()).test(savedStubRequest));
    }
}
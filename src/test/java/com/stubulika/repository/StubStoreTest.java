package com.stubulika.repository;

import com.stubulika.domain.StubRequest;
import org.junit.Test;
import org.springframework.http.HttpHeaders;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class StubStoreTest {

    public static final String URL = "TESTURL";
    public static final String METHOD = "GET";


    @Test
    public void shouldNotFindSavedHeadersIsNullWhenQueryIsNotNull() throws Exception {
        HttpHeaders headers = null;
        StubRequest queryStubRequest = createStubRequest(URL, METHOD, headers);

        HttpHeaders headers2 = new HttpHeaders();
        headers2.put("AnotherHeader", Arrays.asList("application/json"));
        StubRequest savedStubRequest = createStubRequest(URL, METHOD, headers2);

        assertTrue(StubStore.queryHeadersNullSavedNot(queryStubRequest.getHeaders()).test(savedStubRequest));
    }


    @Test
    public void shouldShowHeadersDoNotMatch() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.put("AHeader", Arrays.asList("application/xml"));
        StubRequest queryStubRequest = createStubRequest(URL, METHOD, headers);

        HttpHeaders headers2 = new HttpHeaders();
        headers2.put("AnotherHeader", Arrays.asList("application/json"));
        StubRequest savedStubRequest = createStubRequest(URL, METHOD, headers2);

        assertFalse("Headers should not match ", StubStore.headersMatch(queryStubRequest.getHeaders()).test(savedStubRequest));
    }

    @Test
    public void shouldShowHeadersDoNotMatchWhenQueryIsNullAndSavedIsNot() throws Exception {
        HttpHeaders headers = null;
        StubRequest queryStubRequest = createStubRequest(URL, METHOD, headers);

        HttpHeaders headers2 = new HttpHeaders();
        headers2.put("AnotherHeader", Arrays.asList("application/json"));
        StubRequest savedStubRequest = createStubRequest(URL, METHOD, headers2);

        assertFalse("Headers should not match ", StubStore.headersMatch(queryStubRequest.getHeaders()).test(savedStubRequest));
    }

    @Test
    public void shouldShowHeadersMatchWhenSavedIsNullAndTheQueryNot() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.put("AnotherHeader", Arrays.asList("application/json"));
        StubRequest queryStubRequest = createStubRequest(URL, METHOD, headers);

        HttpHeaders headers2 = null;
        StubRequest savedStubRequest = createStubRequest(URL, METHOD, headers2);

        assertTrue("Headers should match due to Saved's empty headers is a subset of Query Headers", StubStore.headersMatch(queryStubRequest.getHeaders()).test(savedStubRequest));
    }

    @Test
    public void shouldShowHeadersMatch() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.put("AHeader", Arrays.asList("aValue"));
        StubRequest queryStubRequest = createStubRequest(URL, METHOD, headers);

        HttpHeaders headers2 = new HttpHeaders();
        headers2.put("AHeader", Arrays.asList("aValue"));
        StubRequest savedStubRequest = createStubRequest(URL, METHOD, headers2);

        assertTrue("Headers should  match ", StubStore.headersMatch(queryStubRequest.getHeaders()).test(savedStubRequest));
    }

    @Test
    public void shouldShowHeadersMatchWhenBothNull() throws Exception {
        HttpHeaders headers = null;
        StubRequest queryStubRequest = createStubRequest(URL, METHOD, headers);

        HttpHeaders headers2 = null;
        StubRequest savedStubRequest = createStubRequest(URL, METHOD, headers2);

        assertTrue("Headers should  match", StubStore.headersMatch(queryStubRequest.getHeaders()).test(savedStubRequest));
    }

    @Test
    public void shouldShowHeadersMatchOnSubset() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.put("AHeader", Arrays.asList("aValue"));
        headers.put("AHeader2", Arrays.asList("aValue2"));
        StubRequest queryStubRequest = createStubRequest(URL, METHOD, headers);

        HttpHeaders headers2 = new HttpHeaders();
        headers2.put("AHeader", Arrays.asList("aValue"));
        StubRequest savedStubRequest = createStubRequest(URL, METHOD, headers2);

        assertTrue("Subset of headers should  match ", StubStore.headersMatch(queryStubRequest.getHeaders()).test(savedStubRequest));
    }

    private StubRequest createStubRequest(String url, String method, HttpHeaders headers) {
        StubRequest stubRequest = new StubRequest();
        stubRequest.setUrl(url);
        stubRequest.setMethod(method);
        stubRequest.setHeaders(headers);
        stubRequest.setCreated(LocalDateTime.now());
        return stubRequest;
    }
}
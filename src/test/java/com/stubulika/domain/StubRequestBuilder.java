package com.stubulika.domain;

import org.springframework.http.HttpHeaders;

import java.time.LocalDateTime;

public class StubRequestBuilder {

    private HttpHeaders headers;
    private String body;
    private String url;
    private String method;
    private LocalDateTime created;

    public StubRequestBuilder() {
    }

    public StubRequestBuilder withUrl(String url) {
        this.url = url;
        return this;
    }

    public StubRequestBuilder withMethod(String method) {
        this.method = method;
        return this;
    }

    public StubRequestBuilder withHeaders(HttpHeaders headers) {
        this.headers = headers;
        return this;
    }

    public StubRequestBuilder withBody(String body) {
        this.body = body;
        return this;
    }

    public StubRequestBuilder withCreated(LocalDateTime created) {
        this.created = created;
        return this;
    }

    public StubRequest build() {
        return new StubRequest(url, method, body, headers, created);
    }
}


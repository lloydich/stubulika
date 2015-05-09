package com.stubulika.resource;

import com.stubulika.domain.StubRequest;
import com.stubulika.domain.StubResponse;

public class StubAdminRequest {
    private StubRequest request;
    private StubResponse response;

    public StubRequest getRequest() {
        return request;
    }

    public void setRequest(StubRequest request) {
        this.request = request;
    }

    public StubResponse getResponse() {
        return response;
    }

    public void setResponse(StubResponse response) {
        this.response = response;
    }

    @Override
    public String toString() {
        return "StubAdminRequest{" +
                "request=" + request +
                ", response=" + response +
                '}';
    }
}

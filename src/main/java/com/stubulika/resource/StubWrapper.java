package com.stubulika.resource;

import com.stubulika.domain.StubRequest;
import com.stubulika.domain.StubResponse;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class StubWrapper {

    @Valid
    @NotNull
    private StubRequest request;

    @Valid
    @NotNull
    private StubResponse response;

    public StubWrapper() {
    }

    public StubWrapper(StubRequest request, StubResponse response) {
        this.request = request;
        this.response = response;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StubWrapper)) return false;

        StubWrapper that = (StubWrapper) o;

        if (request != null ? !request.equals(that.request) : that.request != null) return false;
        if (response != null ? !response.equals(that.response) : that.response != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = request != null ? request.hashCode() : 0;
        result = 31 * result + (response != null ? response.hashCode() : 0);
        return result;
    }
}

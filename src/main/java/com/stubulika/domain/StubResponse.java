package com.stubulika.domain;

import org.springframework.http.HttpHeaders;

import java.util.Map;

public class StubResponse {

    private int status;
    private Map<String, Object> body;
    private HttpHeaders headers;

   // private HashMap headersMap;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Map<String, Object> getBody() {
        return body;
    }

    public void setBody(Map<String, Object> body) {
        this.body = body;
    }

//      @JsonAnyGetter
//    public HashMap getHeadersMap() {
//        return headersMap;
//    }
//
//    @JsonAnySetter
//    public void setHeadersMap(HashMap headersMap) {
//        this.headersMap = headersMap;
//    }

    //latency

    //file?
    public void setHeaders(HttpHeaders headers) {
        this.headers = headers;
    }

    public HttpHeaders getHeaders() {
        return headers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StubResponse)) return false;

        StubResponse that = (StubResponse) o;

        if (status != that.status) return false;
        if (body != null ? !body.equals(that.body) : that.body != null) return false;
        if (headers != null ? !headers.equals(that.headers) : that.headers != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = status;
        result = 31 * result + (body != null ? body.hashCode() : 0);
        result = 31 * result + (headers != null ? headers.hashCode() : 0);
        return result;

    }

    @Override
    public String toString() {
        return "StubResponse{" +
                "status=" + status +
                ", body='" + body + '\'' +
                ", headers=" + headers +
                '}';
    }
}

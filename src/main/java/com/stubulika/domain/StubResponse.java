package com.stubulika.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.stubulika.resource.View;
import org.springframework.http.HttpHeaders;

import javax.validation.constraints.NotNull;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class StubResponse {

    @JsonView(View.Summary.class)
    @NotNull(message = "{error.response.status.null}")
    private Integer status;
    private String body;
    private HttpHeaders headers;


    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }



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

        if (body != null ? !body.equals(that.body) : that.body != null) return false;
        if (headers != null ? !headers.equals(that.headers) : that.headers != null) return false;
        if (status != null ? !status.equals(that.status) : that.status != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = status != null ? status.hashCode() : 0;
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

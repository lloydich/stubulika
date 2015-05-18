package com.stubulika.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.stubulika.resource.View;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.http.HttpHeaders;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class StubRequest {


    @JsonView(View.Summary.class)
    @NotEmpty
    private String url;

    @JsonView(View.Summary.class)
    @NotEmpty
    private String method;

    private String body;

    private HttpHeaders headers;

    @JsonIgnore
    private LocalDateTime created;

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setHeaders(HttpHeaders headers) {
        this.headers = headers;
    }

    public HttpHeaders getHeaders() {
        return headers;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StubRequest)) return false;

        StubRequest that = (StubRequest) o;

        if (body != null ? !body.equals(that.body) : that.body != null) return false;
        if (method != null ? !method.equals(that.method) : that.method != null) return false;
        if (url != null ? !url.equals(that.url) : that.url != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = url != null ? url.hashCode() : 0;
        result = 31 * result + (method != null ? method.hashCode() : 0);
        result = 31 * result + (body != null ? body.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "StubRequest{" +
                "url='" + url + '\'' +
                ", method='" + method + '\'' +
                ", body='" + body + '\'' +
                ", headers=" + headers +
                ", created=" + created +
                '}';
    }
}

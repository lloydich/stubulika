package com.stubulika.resource;

import com.stubulika.domain.StubRequest;
import com.stubulika.domain.StubResponse;
import com.stubulika.repository.StubRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.util.Arrays;
import java.util.Enumeration;

@RestController
@RequestMapping("/endpoint")
public class StubEndpointController {

    Logger logger = LoggerFactory.getLogger(StubEndpointController.class);


    private final StubRepository stubRepository;

    public
    @Autowired
    StubEndpointController(StubRepository stubRepository) {
        this.stubRepository = stubRepository;
    }


    @RequestMapping(value = "/**")
    public ResponseEntity<?> resolve(HttpServletRequest request) {

        String resourcePath = extractPathWithQueryParams(request);
        logger.debug("resolve() resourcePath:" + resourcePath);
        StubRequest stubRequest = new StubRequest();
        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        logger.debug("resolve() requestMethod:" + requestMethod);
        stubRequest.setMethod(requestMethod.name());
        stubRequest.setUrl(resourcePath);
        stubRequest.setBody(extractBody(request));
        stubRequest.setHeaders(extractHeaders(request));

        StubResponse stubResponse = stubRepository.find(stubRequest);
        logger.debug("resolve() stubResponse:" + stubResponse);
        if (stubResponse == null) {
            return new ResponseEntity<>(null, null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(stubResponse.getBody(), stubResponse.getHeaders(), HttpStatus.valueOf(stubResponse.getStatus()));
    }

    private HttpHeaders extractHeaders(HttpServletRequest request) {
        HttpHeaders headers = new HttpHeaders();

        Enumeration headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            String value = request.getHeader(key);

            headers.put(key, Arrays.asList(value));
        }

        return headers;
    }

    private String extractBody(HttpServletRequest request) {
        String body = null;
        StringBuffer jb = new StringBuffer();
        String line = null;
        try {
            BufferedReader reader = request.getReader();
            while ((line = reader.readLine()) != null)
                jb.append(line);
        } catch (Exception e) {
            logger.error("extractBody() " + e);
        }
        if (jb.length() > 0) {
            body = jb.toString();
        }
        logger.debug("extractBody() " + body);
        return body;
    }

    private String extractPathWithQueryParams(HttpServletRequest request) {
        String path = (String) request.getAttribute(
                HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
        String bestMatchPattern = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);

        AntPathMatcher apm = new AntPathMatcher();
        // on "/properties/foo/bar", resourcePath contains "foo/bar"
        String endpointPath = apm.extractPathWithinPattern(bestMatchPattern, path);
        if(!StringUtils.isEmpty(request.getQueryString())){
            endpointPath = endpointPath + "?" + request.getQueryString();
        }
        return endpointPath;
    }

}

package com.stubulika.resource;

import com.stubulika.domain.StubRequest;
import com.stubulika.domain.StubResponse;
import com.stubulika.repository.StubAdminRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/endpoint")
public class StubEndpointController {

    Logger logger = LoggerFactory.getLogger(StubEndpointController.class);


    private final StubAdminRepository stubAdminRepository;

    public
    @Autowired
    StubEndpointController(StubAdminRepository stubAdminRepository) {
        this.stubAdminRepository = stubAdminRepository;
    }


    @RequestMapping(value="/**")
    public ResponseEntity<?> resolve(HttpServletRequest request) {

        String resourcePath = getPath(request);

        logger.debug("resolve() resourcePath:"+resourcePath);
        StubRequest stubRequest = new StubRequest();
        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        logger.debug("resolve() requestMethod:"+requestMethod);
        stubRequest.setMethod(requestMethod.name());
        stubRequest.setUrl(resourcePath);
        StubResponse stubResponse = stubAdminRepository.find(stubRequest);
        logger.debug("resolve() stubResponse:"+stubResponse);
        if(stubResponse==null){
            return new ResponseEntity<>(null, null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(stubResponse.getBody(), stubResponse.getHeaders(), HttpStatus.valueOf(stubResponse.getStatus()));
    }

    private String getPath(HttpServletRequest request) {
        String path = (String) request.getAttribute(
                HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
        String bestMatchPattern = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);

        AntPathMatcher apm = new AntPathMatcher();
        // on "/properties/foo/bar", resourcePath contains "foo/bar"
        return apm.extractPathWithinPattern(bestMatchPattern, path);
    }

}

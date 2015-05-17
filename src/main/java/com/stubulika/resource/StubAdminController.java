package com.stubulika.resource;

import com.stubulika.domain.StubAdminService;
import com.stubulika.domain.StubRequest;
import com.stubulika.domain.StubResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class StubAdminController {

    Logger logger = LoggerFactory.getLogger(StubAdminController.class);

    @Autowired
    private StubAdminService stubAdminService;
    
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity <List<StubWrapper>> get() {
        List<StubWrapper> stubs = stubAdminService.findAll();
        ResponseEntity<List<StubWrapper>>  response = new ResponseEntity<>(stubs, HttpStatus.OK);
        logger.debug("get() actual response:"+response);
        return response;
    }


    @RequestMapping(method = RequestMethod.POST)
    ResponseEntity<?> add (@Valid @RequestBody StubWrapper stubWrapper)   {
        logger.debug("add()  stubAdminRequest:"+ stubWrapper);

        StubRequest stubRequest = stubWrapper.getRequest();
        StubResponse stubResponse = stubWrapper.getResponse();
        stubAdminService.save(stubRequest, stubResponse);

        ResponseEntity<Object> response = new ResponseEntity<>(null, null, HttpStatus.CREATED);
        logger.debug("add() actual response:"+response);
        return response;

    }


    @RequestMapping(method = RequestMethod.DELETE)
    ResponseEntity<?> delete ( @RequestBody StubWrapper stubWrapper)   {
        logger.debug("delete()  stubAdminRequest:"+ stubWrapper);

        StubRequest stubRequest = stubWrapper.getRequest();
        stubAdminService.delete(stubRequest);

        ResponseEntity<Object> response = new ResponseEntity<>(null, null, HttpStatus.ACCEPTED);
        logger.debug("delete() actual response:"+response);
        return response;

    }
}

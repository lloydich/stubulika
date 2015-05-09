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

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/admin")
public class StubAdminController {

    Logger logger = LoggerFactory.getLogger(StubAdminController.class);

    @Autowired
    private StubAdminService stubAdminService;
    
    @RequestMapping(method = RequestMethod.GET)
    public String get() {
        return "Greetings from Stubulika! \n Here is a list of configured endpoints:";
    }


    @RequestMapping(method = RequestMethod.POST)
    ResponseEntity<?> add ( HttpServletRequest request, @RequestBody StubAdminRequest input )   {
        logger.debug("add() POST! input:"+input);

        StubRequest stubRequest = input.getRequest();

        StubResponse stubResponse = input.getResponse();
        logger.debug("add() POST! stubresponse:"+input.getResponse());

        stubAdminService.save(stubRequest, stubResponse);

        ResponseEntity<Object> response = new ResponseEntity<>(null, null, HttpStatus.CREATED);
        logger.debug("add() response:"+response);
        return response;

    }



    //delete

    //put?

    
}

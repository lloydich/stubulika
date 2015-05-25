package com.stubulika.resource;

import com.fasterxml.jackson.annotation.JsonView;
import com.stubulika.domain.StubAdminService;
import com.stubulika.domain.StubRequest;
import com.stubulika.domain.StubResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class StubAdminController {

    Logger logger = LoggerFactory.getLogger(StubAdminController.class);

    @Autowired
    private StubAdminService stubAdminService;

    @JsonView(View.Summary.class)
    @RequestMapping(method = RequestMethod.GET, value = "/summary")
    public ResponseEntity<List<StubWrapper>> getAllAsSummary() {
        return getAll();
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<StubWrapper>> getAll() {
        List<StubWrapper> stubs = stubAdminService.findAll();
        ResponseEntity<List<StubWrapper>> response = new ResponseEntity<>(stubs, HttpStatus.OK);
        logger.debug("get() actual response:" + response);
        return response;
    }

    @RequestMapping(method = RequestMethod.POST)
    ResponseEntity<Message> add(@Valid @RequestBody StubWrapper stubWrapper) {
        logger.debug("add()  stubAdminRequest:" + stubWrapper);

        StubRequest stubRequest = stubWrapper.getRequest();
        StubResponse stubResponse = stubWrapper.getResponse();
        stubAdminService.save(stubRequest, stubResponse);

        Message success = new Message(MessageType.SUCCESS, "Your stub has been added");
        ResponseEntity<Message> response = new ResponseEntity<>(success, null, HttpStatus.CREATED);
        logger.debug("add() actual response:" + response);
        return response;

    }


    @RequestMapping(method = RequestMethod.DELETE)
    ResponseEntity<?> delete(@RequestBody StubWrapper stubWrapper) {
        logger.debug("delete()  stubAdminRequest:" + stubWrapper);

        StubRequest stubRequest = stubWrapper.getRequest();
        stubAdminService.delete(stubRequest);

        Message success = new Message(MessageType.SUCCESS, "Your stub will be deleted");
        ResponseEntity<Message> response = new ResponseEntity<>(success, null, HttpStatus.ACCEPTED);
        logger.debug("delete() actual response:" + response);
        return response;

    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Message handleValidationException(MethodArgumentNotValidException e) {
        List<ObjectError> errors = e.getBindingResult().getAllErrors();
        logger.error("handleValidationException errors:" + errors);
        BindingResult result = e.getBindingResult();
        FieldError error = result.getFieldError();
        return processFieldError(error);
    }

    private Message processFieldError(FieldError error) {
        Message message = null;
        if (error != null) {
            message = new Message(MessageType.ERROR, error.getDefaultMessage());
        }
        return message;
    }
}

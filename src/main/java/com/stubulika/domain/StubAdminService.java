package com.stubulika.domain;

import com.stubulika.repository.StubRepository;
import com.stubulika.resource.StubAdminRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class StubAdminService {
    private Logger logger = LoggerFactory.getLogger(StubAdminService.class);


    private StubRepository stubRepository;

    public
    @Autowired
    StubAdminService(StubRepository stubRepository) {
        this.stubRepository = stubRepository;
    }

    public void save(StubRequest request, StubResponse response) {
        if (request.getHeaders() == null) {
            request.setHeaders(new HttpHeaders());
        }
        logger.debug("save()  stubresponse:" + request);
        logger.debug("save()  stubresponse:" + response);
        stubRepository.save(request, response);
    }

    public List<StubAdminRequest> findAll() {
        return stubRepository.findAll();
    }
}

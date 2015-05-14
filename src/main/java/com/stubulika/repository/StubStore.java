package com.stubulika.repository;

import com.stubulika.domain.StubRequest;
import com.stubulika.domain.StubResponse;
import com.stubulika.resource.StubAdminRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

@Repository
public class StubStore {

    private Logger logger = LoggerFactory.getLogger(StubStore.class);

    @Resource
    private Map<StubRequest, StubResponse> storeMap;


    public static Predicate<StubRequest> headersMatch(HttpHeaders headers) {
        return p -> (p.getHeaders() == null && headers == null) ||
                ((headers!=null && p.getHeaders()!=null) && headers.entrySet().containsAll(p.getHeaders().entrySet()));
    }


    public  void save(StubRequest request, StubResponse response) {
        storeMap.put(request, response);
        logger.debug("save() after - entrySet:" + storeMap.entrySet());
    }

    public StubResponse find(StubRequest stubRequest) {
        logger.debug("find() entrySet:" + storeMap.entrySet());
        logger.debug("find() key exists:"+ storeMap.containsKey(stubRequest) + ", key:"+stubRequest);

        Optional<StubRequest> storedStubRequest = getStubRequestOptional(stubRequest);

        logger.debug("find() "+storedStubRequest);
        if(storedStubRequest.isPresent()){
            return storeMap.get(storedStubRequest.get());
        }
        return null;
    }

    private Optional<StubRequest> getStubRequestOptional(StubRequest stubRequest) {
        return storeMap.keySet().stream()
                .filter(t -> t.equals(stubRequest))
                .filter(headersMatch(stubRequest.getHeaders()))
                .findFirst();
    }

    public List<StubAdminRequest> findAll() {
        List<StubAdminRequest> stubs = new ArrayList<>();
        for (StubRequest stubRequest : storeMap.keySet()) {
            StubAdminRequest stubAdminRequest = new StubAdminRequest();
            stubAdminRequest.setRequest(stubRequest);
            stubAdminRequest.setResponse(storeMap.get(stubRequest));
            stubs.add(stubAdminRequest);
        }
        return stubs;
    }

    public void delete(StubRequest stubRequest) {
        logger.debug("delete() entrySet BEFORE:" + storeMap.entrySet());
        logger.debug("delete() key exists:"+ storeMap.containsKey(stubRequest) + ", key:"+stubRequest);
        Optional<StubRequest> storedStubRequest = getStubRequestOptional(stubRequest);
        if(storedStubRequest.isPresent()){
            storeMap.remove(storedStubRequest.get());
        }
        logger.debug("delete() entrySet AFTER:" + storeMap.entrySet());
    }
}

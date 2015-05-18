package com.stubulika.repository;

import com.stubulika.domain.StubRequest;
import com.stubulika.domain.StubResponse;
import com.stubulika.resource.StubWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Predicate;

@Repository
public class StubStore {

    private Logger logger = LoggerFactory.getLogger(StubStore.class);

    @Resource
    private Map<StubRequest, StubResponse> storeMap;

    public static Predicate<StubRequest> queryHeadersNullSavedNot(HttpHeaders queryHeaders) {
        return p -> (p.getHeaders()!=null && queryHeaders ==null);
    }

    public static Predicate<StubRequest> headersMatch(HttpHeaders queryHeaders) {
        Predicate<StubRequest> headersAreEqualPredicate = p ->
                (p.getHeaders() == null && queryHeaders == null) ||
                (p.getHeaders() == null && queryHeaders != null) ||
                ((queryHeaders != null && p.getHeaders() != null) && queryHeaders.entrySet().containsAll(p.getHeaders().entrySet()));
       System.out.println("headersAreEqualPredicate:"+headersAreEqualPredicate);
       return queryHeadersNullSavedNot(queryHeaders).negate().and(headersAreEqualPredicate);
    }


    public  void save(StubRequest request, StubResponse response) {
        request.setCreated(LocalDateTime.now());
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

    public List<StubWrapper> findAll() {
        List<StubWrapper> stubs = new ArrayList<>();
        for (StubRequest stubRequest : storeMap.keySet()) {
            StubWrapper stubWrapper = new StubWrapper();
            stubWrapper.setRequest(stubRequest);
            stubWrapper.setResponse(storeMap.get(stubRequest));
            stubs.add(stubWrapper);
        }
        Collections.sort(stubs, (StubWrapper s1, StubWrapper s2) -> s1.getRequest().getCreated().compareTo(s2.getRequest().getCreated()));
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

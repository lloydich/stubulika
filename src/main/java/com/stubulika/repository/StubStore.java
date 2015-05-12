package com.stubulika.repository;

import com.stubulika.domain.StubRequest;
import com.stubulika.domain.StubResponse;
import com.stubulika.resource.StubAdminRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class StubStore {

    Logger logger = LoggerFactory.getLogger(StubStore.class);

    @Resource
    private Map<StubRequest, StubResponse> storeMap;


    public  void save(StubRequest request, StubResponse response) {
        storeMap.put(request, response);
        logger.debug("save() after - entrySet:" + storeMap.entrySet());
    }

    public StubResponse find(StubRequest stubRequest) {
        logger.debug("find() entrySet:" + storeMap.entrySet());
        logger.debug("find() key exists:"+ storeMap.containsKey(stubRequest) + ", key:"+stubRequest);
        return storeMap.get(stubRequest);
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
}

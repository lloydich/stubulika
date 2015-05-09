package com.stubulika.repository;

import com.stubulika.domain.StubRequest;
import com.stubulika.domain.StubResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
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
}

package com.stubulika.repository;

import com.stubulika.domain.StubRequest;
import com.stubulika.domain.StubResponse;
import com.stubulika.resource.StubAdminController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class StubAdminRepository {
    Logger logger = LoggerFactory.getLogger(StubAdminController.class);

    private  StubStore stubStore;



    public @Autowired
    StubAdminRepository(StubStore stubStore) {
        this.stubStore = stubStore;
    }

    public void save(StubRequest request, StubResponse response) {
            stubStore.save(request,response);


    }


    public StubResponse find(StubRequest stubRequest) {

       return  stubStore.find(stubRequest);
    }
}

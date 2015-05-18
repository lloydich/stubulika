package com.stubulika.repository;

import com.stubulika.domain.StubRequest;
import com.stubulika.domain.StubResponse;
import com.stubulika.resource.StubAdminController;
import com.stubulika.resource.StubWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class StubRepository {
    private Logger logger = LoggerFactory.getLogger(StubAdminController.class);

    private StubStore stubStore;


    public
    @Autowired
    StubRepository(StubStore stubStore) {
        this.stubStore = stubStore;
    }

    public void save(StubRequest request, StubResponse response) {
        stubStore.save(request, response);
    }


    public StubResponse find(StubRequest stubRequest) {
        return stubStore.find(stubRequest);
    }


    public List<StubWrapper> findAll() {
        return stubStore.findAll();
    }

    public void delete(StubRequest stubRequest) {
        stubStore.delete(stubRequest);
    }
}

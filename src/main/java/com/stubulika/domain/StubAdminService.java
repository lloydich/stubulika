package com.stubulika.domain;

import com.stubulika.repository.StubAdminRepository;
import com.stubulika.resource.StubAdminRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class StubAdminService {

    private StubAdminRepository stubAdminRepository;

    public  @Autowired
    StubAdminService(StubAdminRepository stubAdminRepository) {
        this.stubAdminRepository = stubAdminRepository;
    }

    public void save(StubRequest request, StubResponse response) {
          stubAdminRepository.save(request, response);
    }

    public List<StubAdminRequest> findAll() {
        return stubAdminRepository.findAll();
    }
}

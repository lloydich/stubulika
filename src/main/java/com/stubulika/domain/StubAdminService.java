package com.stubulika.domain;

import com.stubulika.repository.StubAdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



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
}

package com.recruting.spit.dao;

import com.recruting.spit.pojo.Spit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SpitDao extends MongoRepository<Spit,String> {

    Page<Spit> findByParentid(String parentId, Pageable pageable);

    Page<Spit> findByStateAndParentidNull(String state, Pageable pageable);
}

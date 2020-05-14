package com.tensquare.spit.dao;

import com.tensquare.spit.po.Spit;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.domain.Pageable;


/**
 * 吐槽数据访问层
 */
public interface SpitRepository extends MongoRepository<Spit,String> {
    /**
     * 根据上级ID查询吐槽列表（分页）
     * @param parentid
     * @param pageable
     * @return
     */
    Page<Spit> findByParentid(String parentid, Pageable pageable);
}

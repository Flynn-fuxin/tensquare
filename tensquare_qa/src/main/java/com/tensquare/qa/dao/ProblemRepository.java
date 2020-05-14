package com.tensquare.qa.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.tensquare.qa.po.Problem;
import org.springframework.data.jpa.repository.Query;

/**
 * problem数据访问接口
 * @author Administrator
 *
 */
public interface ProblemRepository extends JpaRepository<Problem,String>,JpaSpecificationExecutor<Problem>{
    /**
     * 根据标签ID查询最新回答的问题分页列表
     * @param labelid
     * @param pageable
     * @return
     */
    @Query("select p from Problem p where exists (select problemid from Pl where labelid = ?1 and problemid=p.id) order by p.replytime desc")
    public Page<Problem> findByLabelidOrderByReplytimeDesc(String labelid, Pageable pageable);

}

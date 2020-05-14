package com.tensquare.article.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.tensquare.article.po.Article;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * article数据访问接口
 * @author Administrator
 *
 */
public interface ArticleRepository extends JpaRepository<Article,String>,JpaSpecificationExecutor<Article>{
    /**
     *  根据id修改状态
     * @param id
     * @param state
     */
    //JPQL
    //目标：根据id修改状态
    //反射机制：默认有个只读事务的实现
    @Query("update Article set state = ?2 where id  = ?1 ")
    //在增删改语句的时候，必须添加该注解。
    @Modifying
//该注解会让Query注解的反射代码中，不执行事务代码。即没事务，因此，必须手动加事务
    void updateStateById(String id,String state);
	
}

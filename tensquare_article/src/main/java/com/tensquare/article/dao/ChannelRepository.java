package com.tensquare.article.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.tensquare.article.po.Channel;
/**
 * channel数据访问接口
 * @author Administrator
 *
 */
public interface ChannelRepository extends JpaRepository<Channel,String>,JpaSpecificationExecutor<Channel>{
	
}

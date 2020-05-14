package base.dao;

import base.po.Label;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 标签数据访问接口
 *  
 */
public interface LabelRepository extends JpaRepository<Label, String>,JpaSpecificationExecutor<Label>{
}
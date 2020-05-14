package base.service;

import base.dao.LabelRepository;
import base.po.Label;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;
import utils.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * \标签业务逻辑类
 */
@Service
public class LabelService {
    @Autowired
    private LabelRepository labelRepository;
    @Autowired
    private IdWorker idWorker;

    /**
     * 保存一个标签
     * @param label
     */
    public void saveLabel(Label label){
        //设置ID
        label.setId(idWorker.nextId()+"");
        labelRepository.save(label);
    }
    /**
     * 更新一个标签
     * @param label
     */
    public void updateLabel(Label label){
        labelRepository.save(label);
    }

    /**
     * 删除一个标签
     * @param id
     */
    public void deleteLabelById(String id){
        labelRepository.deleteById(id);
    }

    /**
     * 查询全部标签
     *
     * @return
     */
    public List<Label> findLabelList() {
        return labelRepository.findAll();
    }

    /**
     * 按照条件查询
     * @param searchMap
     * @return
     */
    public List<Label> findLabelList(Map<String,Object> searchMap) {
        //构建Spec业务条件对象
        Specification<Label> spec = (root, criteriaQuery, criteriaBuilder) -> {
            //创建一个临时的list
            List<Predicate> andPredicateList = new ArrayList<>();
            //条件封装
            //标签
            if(!StringUtils.isEmpty(searchMap.get("labelname"))){
                andPredicateList.add(criteriaBuilder.like(root.get("labelname").as(String.class),"%"+searchMap.get("labelname")+"%"));
            }
            //状态
            if(!StringUtils.isEmpty(searchMap.get("state"))){
                andPredicateList.add(criteriaBuilder.equal(root.get("state").as(String.class),searchMap.get("state")));
            }
            //是否推荐
            if(!StringUtils.isEmpty(searchMap.get("recommend"))){
                andPredicateList.add(criteriaBuilder.equal(root.get("recommend").as(String.class),searchMap.get("recommend")));
            }
            //组合条件
            return criteriaBuilder.and(andPredicateList.toArray(new Predicate[andPredicateList.size()]));
        };
        return labelRepository.findAll(spec);
    }

    /**
     * 根据ID查询标签
     *
     * @return
     */
    public Label findLabelById(String id) {
        return labelRepository.findById(id).get();
    }

    /**
     * 根据参数searchMap 获取spec 条件对象
     * @Param searchMap
     * @Return
     */
    public Specification<Label> getLabelSpecification(Map<String,Object> searchMap){
        Specification<Label> spec = (root, criteriaQuery, cb) -> {
            //临时存放条件的集合
            List<Predicate> predicateList = new ArrayList<>();
            //标签名字
            if(!StringUtils.isEmpty(searchMap.get("labelname"))){
                predicateList.add(
                        cb.like(root.get("labelname").as(String.class),"%"+(String)searchMap.get("labelname")+"%")
                );
            }

            //状态
            if(!StringUtils.isEmpty(searchMap.get("state"))){
                predicateList.add(
                        cb.equal(root.get("state").as(String.class),(String)searchMap.get("state"))
                );
            }

            //是否推荐
            if(!StringUtils.isEmpty(searchMap.get("recommend"))){
                predicateList.add(
                        cb.equal(root.get("recommend").as(String.class),(String)searchMap.get("recommend"))
                );
            }
            //多个条件组合成and关系
            return cb.and(predicateList.toArray(new Predicate[predicateList.size()]));
        };
        return spec;
    }
    /**
     * 组合条件分页查询
     * @param searchMap
     * @param page
     * @param size
     * @return
     */
    public Page<Label> findLabelList(Map<String,Object> searchMap,int page,int size){
        //构建spec 对象
        Specification<Label> spec = getLabelSpecification(searchMap);
        //构建请求的分页对象
        Pageable pageRequest = PageRequest.of(page-1,size);
        return labelRepository.findAll(spec,pageRequest);
    }

}

package com.recruiting.base.service;

import com.recruiting.base.dao.LabelDao;
import com.recruiting.base.pojo.Label;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import util.IdWorker;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class LabelService {

    @Autowired
    private LabelDao labelDao;
    @Autowired
    private IdWorker idWorker;

    public List<Label> findAll(){
        return labelDao.findAll();
    }

    public Label findById(String id){
        return labelDao.findById(id).get();
    }

    public void save(Label label){
        label.setId(idWorker.nextId()+"");
        labelDao.save(label);
    }

    public void update(Label label){
        labelDao.save(label);
    }

    public void deleteById(String id){
        labelDao.deleteById(id);
    }

    public List<Label> findSearch(Label label) {
        return labelDao.findAll(getSpecification(label));
    }

    public Page<Label> pageQuery(Label label, int page, int size) {
        Pageable pageable = PageRequest.of(page-1,size);
        return labelDao.findAll(getSpecification(label),pageable);
    }

    private Specification<Label> getSpecification(Label label){
        return new Specification<Label>() {
            /**
             *
             * @param root 跟对象,把条件被封装的对象  where 类名 = 类名.get属性
             * @param criteriaQuery 封装的查询关键字,group by order by等
             * @param criteriaBuilder 封装条件对象的
             * @return
             */
            @Override
            public Predicate toPredicate(Root<Label> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                //存放所有的条件
                List<Predicate> list = new ArrayList<>();
                if (label.getLabelname() != null && !"".equals(label.getLabelname())){
                    //labelname like %小明%
                    Predicate predicate = criteriaBuilder.like(root.get("labelname").as(String.class),"%"+label.getLabelname()+"%");
                    list.add(predicate);
                }
                if (label.getState() != null && !"".equals(label.getState())){
                    //stats = "1"
                    Predicate predicate = criteriaBuilder.equal(root.get("state").as(String.class),label.getState());
                    list.add(predicate);
                }
                //根据条件长度创建Predicate数组
                Predicate[] predicates = new Predicate[list.size()];
                list.toArray(predicates);
                return criteriaBuilder.and(predicates);//where labelname like %小明% and stats = "1"
            }
        };
    }
}

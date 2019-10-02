package com.tensquare.recruit.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.tensquare.recruit.pojo.Recruit;

import java.util.List;

/**
 * 数据访问接口
 * @author Administrator
 *
 */
public interface RecruitDao extends JpaRepository<Recruit,String>,JpaSpecificationExecutor<Recruit>{
    //where state=? order by createtime   按顺序查6个
	List<Recruit> findTop6ByStateOrderByCreatetimeDesc(String state);
    //where state!=? order by createtime   按顺序查5个
	List<Recruit> findTop5ByStateNotOrderByCreatetimeDesc(String state);
}

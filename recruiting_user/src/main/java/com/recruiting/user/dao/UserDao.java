package com.recruiting.user.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.recruiting.user.pojo.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * 数据访问接口
 * @author Administrator
 *
 */
public interface UserDao extends JpaRepository<User,String>,JpaSpecificationExecutor<User>{
	User findByMobile(String mobile);

	@Transactional
    @Modifying
	@Query(value = "update tb_user set fanscount = fanscount+? where id=?",nativeQuery = true)
    void updatefanscount(int x, String friendid);


    @Transactional
    @Modifying
    @Query(value = "update tb_user set followcount = followcount+? where id=?",nativeQuery = true)
    void updatefollowcount(int x, String userid);
}

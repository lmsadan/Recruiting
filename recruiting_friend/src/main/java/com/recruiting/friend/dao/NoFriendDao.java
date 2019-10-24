package com.recruiting.friend.dao;

import com.recruiting.friend.pojo.NoFriend;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoFriendDao extends JpaRepository<NoFriend,String> {

    NoFriend findByUseridAndNofriendid(String userid, String nofriendid);
}

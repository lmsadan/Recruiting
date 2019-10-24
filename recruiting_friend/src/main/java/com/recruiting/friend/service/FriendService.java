package com.recruiting.friend.service;

import com.recruiting.friend.dao.FriendDao;
import com.recruiting.friend.dao.NoFriendDao;
import com.recruiting.friend.pojo.Friend;
import com.recruiting.friend.pojo.NoFriend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class FriendService {

    @Autowired
    private FriendDao friendDao;
    @Autowired
    private NoFriendDao noFriendDao;

    public int addFriend(String userid, String friendid) {
        //判断user当friend是否有数据,有:重复添加,返回0
        Friend friend = friendDao.findByUseridAndFriendid(userid, friendid);
        if (friend != null){
            return 0;
        }
        //直接添加好友,让当前user添加friend的type为0
        friend = new Friend();
        friend.setUserid(userid);
        friend.setFriendid(friendid);
        friend.setIslike("0");
        friendDao.save(friend);
        //判断friend到user是否有数据,有:双方状态都改为1
        if (friendDao.findByUseridAndFriendid(friendid,userid) != null){
            friendDao.updateIslike("1",userid,friendid);
            friendDao.updateIslike("1",friendid,userid);
        }
        return 1;
    }

    public int addNotFriend(String userid, String nofriendid) {
        //先判断是否已经是非好友
        NoFriend noFriend = noFriendDao.findByUseridAndNofriendid(userid,nofriendid);
        if (noFriend != null){
            return 0;
        }
        noFriend = new NoFriend();
        noFriend.setUserid(userid);
        noFriend.setNofriendid(nofriendid);
        noFriendDao.save(noFriend);
        return 1;
    }

    public void deleteFriend(String userid, String friendid) {
        //删除好友表中userid到friendid的数据
        friendDao.deletefriend(userid,friendid);
        //更新friendid到userid的islike为0
        friendDao.updateIslike("0",friendid,userid);
        //非好友表中添加数据
        NoFriend noFriend = new NoFriend();
        noFriend.setUserid(userid);
        noFriend.setNofriendid(friendid);
        noFriendDao.save(noFriend);
    }
}

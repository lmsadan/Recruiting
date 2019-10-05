package com.recruit.friend.pojo;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "tb_nofriend")
@IdClass(NoFriend.class)
public class NoFriend implements Serializable {

    @Id
    private String userid;
    @Id
    private String nofriendid;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getNofriendid() {
        return nofriendid;
    }

    public void setNofriendid(String nofriendid) {
        this.nofriendid = nofriendid;
    }
}

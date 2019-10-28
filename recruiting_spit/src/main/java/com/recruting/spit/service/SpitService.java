package com.recruting.spit.service;

import com.recruting.spit.dao.SpitDao;
import com.recruting.spit.pojo.Spit;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import util.IdWorker;
import util.JwtUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class SpitService {

    @Autowired
    private SpitDao spitDao;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private IdWorker idWorker;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private JwtUtil jwtUtil;

    public List<Spit> findAll(){
        return spitDao.findAll();
    }

    public Spit findById(String id){
        return spitDao.findById(id).get();
    }

    public void save(Spit spit){
        String header = request.getHeader("Authorization");
        String token = header.substring(7);
        Claims claims = jwtUtil.parseJWT(token);
        String userid = (String) claims.get("userid");
        String nickname = (String) claims.get("nickname");
        spit.setUserid(userid);
        spit.setNickname(nickname);
        spit.set_id(idWorker.nextId()+"");
        spit.setPublishtime(new Date());//发布日期
        spit.setVisits(0);//浏览量
        spit.setShare(0);//分享数
        spit.setThumbup(0);//点赞数
        spit.setComment(0);//回复数
        spit.setState("1");//状态
        if (spit.getParentid() != null && !"".equals(spit.getParentid())){
            Query query = new Query();
            query.addCriteria(Criteria.where("_id").is(spit.getParentid()));
            Update update = new Update();
            update.inc("comment",1);
            mongoTemplate.updateFirst(query,update,"spit");
        }

        spitDao.save(spit);
    }

    public void update(Spit spit){
        spitDao.save(spit);
    }

    public void deleteById(String id){
        spitDao.deleteById(id);
    }


    public Page<Spit> findByParentid(String parentid,int page,int size){
        Pageable pageable = PageRequest.of(page-1,size);
        return spitDao.findByParentid(parentid,pageable);
    }

    public Page<Spit> findSearch(Map searchMap,int page,int size){
        Pageable pageable = PageRequest.of(page-1,size);
        return spitDao.findByStateAndParentidNull((String) searchMap.get("state"),pageable);
    }

    public void thumbup(String spitId,boolean flag) {

//        Spit spit = spitDao.findById(spitId).get();
//        spit.setThumbup((spit.getThumbup()==null ? 0 : spit.getThumbup()) + 1);
//        spitDao.save(spit);
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(spitId));
        Update update = new Update();
        if (flag) {
            update.inc("thumbup", 1);
        }else{
            update.inc("thumbup",-1);
        }
        mongoTemplate.updateFirst(query,update,"spit");
    }

}

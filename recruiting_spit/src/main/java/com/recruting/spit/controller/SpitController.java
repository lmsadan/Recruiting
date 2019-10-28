package com.recruting.spit.controller;

import com.recruting.spit.pojo.Spit;
import com.recruting.spit.service.SpitService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin
@RestController
@RefreshScope
@RequestMapping("/spit")
public class SpitController {

    @Autowired
    private SpitService spitService;

    @Autowired
    private RedisTemplate redisTemplate;

    @RequestMapping(method = RequestMethod.GET)
    public Result findAll(){
        return new Result(true, StatusCode.OK,"查询成功",spitService.findAll());
    }

    @RequestMapping(value="/search/{page}/{size}",method = RequestMethod.POST)
    public Result findSearch(@RequestBody Map searchMap,@PathVariable int page,@PathVariable int size){
        Page<Spit> searchPage = spitService.findSearch(searchMap, page, size);

        return new Result(true, StatusCode.OK,"查询成功",new PageResult<Spit>(searchPage.getTotalElements(),searchPage.getContent()));
    }

    @RequestMapping(value = "/{spitId}",method = RequestMethod.GET)
    public Result findById(@PathVariable String spitId){
        return new Result(true, StatusCode.OK,"查询成功",spitService.findById(spitId));
    }

    @RequestMapping(method = RequestMethod.POST)
    public Result save(@RequestBody Spit spit){
        spitService.save(spit);
        return new Result(true, StatusCode.OK,"保存成功");
    }

    @RequestMapping(value = "/{spitId}",method = RequestMethod.PUT)
    public Result update(@RequestBody Spit spit, @PathVariable String spitId){
        spit.set_id(spitId);
        spitService.update(spit);
        return new Result(true, StatusCode.OK,"更新成功");
    }

    @RequestMapping(value = "/{spitId}",method = RequestMethod.DELETE)
    public Result delete(@PathVariable String spitId){
        spitService.deleteById(spitId);
        return new Result(true, StatusCode.OK,"删除成功");
    }

    @RequestMapping(value = "/comment/{parentid}/{page}/{size}",method = RequestMethod.GET)
    public Result findByParentId(@PathVariable String parentid, @PathVariable int page, @PathVariable int size){
        Page<Spit> byParentId = spitService.findByParentid(parentid, page, size);
        return new Result(true, StatusCode.OK,"查询成功",new PageResult<Spit>(byParentId.getTotalElements(),byParentId.getContent()));
    }

    @RequestMapping(value = "/thumbup/{spitId}",method = RequestMethod.PUT)
    public Result thumbup(@PathVariable String spitId,@RequestBody String userId){
        if (redisTemplate.opsForValue().get("thumbup_"+spitId+userId) != null){
            redisTemplate.delete("thumbup_"+spitId+userId);
            spitService.thumbup(spitId,false);
            return new Result(false, StatusCode.OK,"取消点赞");
        }
        spitService.thumbup(spitId,true);
        redisTemplate.opsForValue().set("thumbup_"+spitId+userId,1);
        return new Result(true, StatusCode.OK,"点赞成功");
    }

}

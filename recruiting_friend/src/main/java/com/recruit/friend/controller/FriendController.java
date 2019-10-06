package com.recruit.friend.controller;

import com.recruit.friend.client.UserClient;
import com.recruit.friend.service.FriendService;
import entity.Result;
import entity.StatusCode;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/friend")
@RefreshScope
public class FriendController {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private FriendService friendService;

    @Autowired
    private UserClient userClient;

    /**
     * 添加好友或拉黑
     * @param friendid
     * @param type
     * @return
     */
    @RequestMapping(value = "/like/{friendid}/{type}",method = RequestMethod.POST)
    public Result addFriend(@PathVariable("friendid") String friendid, @PathVariable("type") String type){
        //验证是否登录,并拿到当前用户id
        Claims claims_user = (Claims) request.getAttribute("claims_user");
        if (claims_user == null || "".equals(claims_user)){
            //没有user权限
            return new Result(false, StatusCode.LOGINERROR,"权限不足");
        }
        //获取userId
        String userid = claims_user.getId();
        //判断添加好友还是拉黑
        if(type != null){
            if (type.equals("1")){
                //添加好友
                int flag = friendService.addFriend(userid,friendid);
                if(flag == 0){
                    return new Result(false, StatusCode.ERROR,"不能重复添加好友");
                }
                if(flag == 1){
                    userClient.updatefanscountandfollowcount(userid,friendid,1);
                    return new Result(true, StatusCode.OK,"添加成功");
                }
            }else if(type.equals("2")){
                //非好友
                int flag = friendService.addNotFriend(userid,friendid);
                if(flag == 0){
                    return new Result(false, StatusCode.ERROR,"不能重复非好友");
                }
                if(flag == 1){
                    return new Result(true, StatusCode.OK,"添加成功");
                }
            }else {
                return new Result(false, StatusCode.ERROR,"参数异常");
            }
        }else {
            return new Result(false, StatusCode.ERROR,"参数异常");
        }
        return new Result(true, StatusCode.OK,"添加成功");
    }

    @RequestMapping(value = "/{friendid}",method = RequestMethod.DELETE)
    public Result deleteFriend(@PathVariable String friendid){
        Claims claims_user = (Claims) request.getAttribute("claims_user");
        if (claims_user == null || "".equals(claims_user)){
            //没有user权限
            return new Result(false, StatusCode.LOGINERROR,"权限不足");
        }
        //获取userId
        String userid = claims_user.getId();
        friendService.deleteFriend(userid,friendid);
        userClient.updatefanscountandfollowcount(userid,friendid,-1);
        return new Result(true, StatusCode.OK,"删除成功");
    }
}

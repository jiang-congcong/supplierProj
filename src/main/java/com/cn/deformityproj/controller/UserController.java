package com.cn.deformityproj.controller;

import com.cn.deformityproj.iservice.IUserSV;
import com.cn.deformityproj.utils.CommonUtils;
import com.cn.deformityproj.utils.RedisOperationUtils;
import com.cn.deformityproj.utils.Result;
import com.xiaoleilu.hutool.util.StrUtil;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author jiangcongcong
 * @date 2022/2/23 11:40
 */
@RestController
@RequestMapping("")
public class UserController {

    @Autowired
    private CommonUtils commonUtils;

    @Autowired
    private RedisOperationUtils redisOperationUtils;

    @Autowired
    private IUserSV iUserSV;

    public static Logger logger = LoggerFactory.getLogger(UserController.class);

    @RequestMapping(method = RequestMethod.POST,value = "register")
    @ResponseBody
    @ApiOperation(value = "用户注册")
    public Result register(@RequestBody Map<String,Object> reqMap){
        Result result = new Result();
        String username = (String)reqMap.get("username");
        String password = (String)reqMap.get("password");
        String phoneNum = (String)reqMap.get("phone");
        if(StrUtil.hasEmpty(username)||StrUtil.hasEmpty(password)||StrUtil.hasEmpty(phoneNum)){
            result.setCode(400);
            result.setMessage("用户名或密码或手机号不能为空！");
            return result;
        }
        try{
            //undo用户名唯一性校验
            boolean isRegister = iUserSV.checkUsernameIsRegister(username);
            if(isRegister){
                result.setCode(400);
                result.setMessage("此用户名已被注册，请换用户名注册！");
                return result;
            }
            String userId = commonUtils.createAllId(); //生成用户id
            reqMap.put("userId",userId);
            iUserSV.register(reqMap);

            String token = commonUtils.createAndUpdateToken(userId); //生成token，放入redis中，并返回给前端
            Map<String,Object> resultMap = new HashMap<>();
            resultMap.put("token",token);
            resultMap.put("userId",userId);
            result.setData(resultMap);
            result.setCode(200);
            result.setMessage("注册成功");
        }catch (Exception e){
            logger.error("用户注册失败");
            result.setCode(400);
            result.setMessage("用户注册失败");
        }
        return result;
    }

    @RequestMapping(method = RequestMethod.POST,value = "/login")
    @ResponseBody
    @ApiOperation(value = "用户登录")
    public Result login(@RequestBody Map<String,Object> reqMap) throws Exception {
        Result result = new Result();
        String username = (String) reqMap.get("username");
        String password = (String) reqMap.get("password");
        if (StrUtil.hasEmpty(username) || StrUtil.hasEmpty(password)) {
            result.setCode(400);
            result.setMessage("用户名或密码不能为空！");
        }
        else{
            Map<String,Object> queryUserInfoMap = iUserSV.queryUserPassword(username);
            if(null!=queryUserInfoMap&&queryUserInfoMap.size()>0){
                String getPassword = queryUserInfoMap.get("password").toString();
                String userId = queryUserInfoMap.get("userId").toString();
                if(password.equals(getPassword)){
                    result.setCode(200);
                    Map<String,Object> resultMap = new HashMap<>();
                    String token = commonUtils.createAndUpdateToken(userId);
                    resultMap.put("adminId",userId);
                    resultMap.put("token",token);
                    resultMap.put("adminName",queryUserInfoMap.get("userName"));
                    result.setMessage("登陆成功！");
                    result.setData(resultMap);
                }
                else{
                    result.setCode(400);
                    result.setMessage("密码错误！");
                }
            }
            else{
                result.setCode(400);
                result.setMessage("用户未注册！");
            }
        }
        return result;
    }

    @RequestMapping(method = RequestMethod.POST,value = "/logout")
    @ResponseBody
    @ApiOperation(value = "用户退出登录")
    public Result logout(@RequestBody Map<String,Object> reqMap) throws Exception {
        Result result = new Result();
        String token = (String)reqMap.get("token");
        if(!StrUtil.hasEmpty(token)){
            try {
                redisOperationUtils.del(token);
                result.setCode(200);
                result.setMessage("退出登录成功！");
            }catch (Exception e){
                logger.error("退出登录失败！");
                result.setMessage("退出登录失败！");
                result.setCode(400);
            }
        }
        return result;
    }

    @RequestMapping(method = RequestMethod.POST,value = "/user/list")
    @ResponseBody
    @ApiOperation(value = "查询用户列表")
    public Result queryUserList(@RequestBody Map<String,Object> reqMap) throws Exception {
        Result result = new Result();
        String adminId = (String)reqMap.get("adminId");
        if (StrUtil.hasEmpty(adminId)) {
            result.setCode(400);
            result.setMessage("管理员id不能为空");
            return result;
        }
        if(null==reqMap.get("page")||null==reqMap.get("size")){
            result.setCode(400);
            result.setMessage("分页参数不能为空");
            return result;
        }
        int page = (int)reqMap.get("page");
        int size = (int)reqMap.get("size");
        reqMap.put("start",page*size);
        Map<String, Object> resultMap;
        try {
            resultMap = iUserSV.queryUserList(reqMap);
            result.setCode(200);
            result.setData(resultMap);
            result.setMessage("查询用户列表成功");
        }catch (Exception e){
            logger.error("查询用户列表失败"+e);
            result.setCode(400);
            result.setMessage("查询用户列表失败");
        }
        return result;
    }

    @RequestMapping(method = RequestMethod.POST,value = "/user/add")
    @ResponseBody
    @ApiOperation(value = "用户注册")
    public Result addUser(@RequestBody Map<String,Object> reqMap) throws Exception {
        Result result = new Result();
        String username = (String) reqMap.get("username");
        String userPhone = (String) reqMap.get("userPhone");
        String userAddress = (String) reqMap.get("userAddress");
        String userDescribe = (String) reqMap.get("userDescribe");
        if (StrUtil.hasEmpty(username) || StrUtil.hasEmpty(userPhone) || StrUtil.hasEmpty(userAddress) || StrUtil.hasEmpty(userDescribe)) {
            result.setCode(400);
            result.setMessage("用户名或用户地址或手机号或用户描述不能为空！");
            return result;
        }
        String userId = commonUtils.createAllId(); //生成用户id
        reqMap.put("userId",userId);
        try {
            iUserSV.addUser(reqMap);
            result.setCode(200);
            result.setMessage("保存用户数据成功");
        }catch (Exception e){
            logger.error("保存用户数据失败："+e);
            result.setCode(400);
            result.setMessage("保存用户数据失败");
        }
        return result;
    }

    @RequestMapping(method = RequestMethod.POST,value = "/user/delete")
    @ResponseBody
    @ApiOperation(value = "用户删除")
    public Result deleteUser(@RequestBody Map<String,Object> reqMap) throws Exception {
        Result result = new Result();
        String userId = (String) reqMap.get("userId");
        if (StrUtil.hasEmpty(userId)) {
            result.setCode(400);
            result.setMessage("用户名不能为空！");
            return result;
        }
        try {
            iUserSV.deleteUser(userId);
            result.setCode(200);
            result.setMessage("删除用户数据成功");
        }catch (Exception e){
            logger.error("删除用户数据失败："+e);
            result.setCode(400);
            result.setMessage("删除用户数据失败");
        }
        return result;
    }

    @RequestMapping(method = RequestMethod.POST,value = "/user/update")
    @ResponseBody
    @ApiOperation(value = "用户修改")
    public Result updateUser(@RequestBody Map<String,Object> reqMap) throws Exception {
        Result result = new Result();
        String username = (String) reqMap.get("username");
        String userPhone = (String) reqMap.get("userPhone");
        String userAddress = (String) reqMap.get("userAddress");
        String userDescribe = (String) reqMap.get("userDescribe");
        String userId = (String) reqMap.get("userId");
        if (StrUtil.hasEmpty(username) || StrUtil.hasEmpty(userPhone) || StrUtil.hasEmpty(userAddress) || StrUtil.hasEmpty(userDescribe) || StrUtil.hasEmpty(userId)) {
            result.setCode(400);
            result.setMessage("用户名或用户地址或手机号或用户描述或用户id不能为空！");
            return result;
        }
        try {
            iUserSV.updateUser(reqMap);
            result.setCode(200);
            result.setMessage("更新用户数据成功");
        } catch (Exception e) {
            logger.error("更新用户数据失败：" + e);
            result.setCode(400);
            result.setMessage("更新用户数据失败");
        }
        return result;
    }

}

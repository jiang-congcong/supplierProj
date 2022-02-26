package com.cn.deformityproj.serviceimpl;

import com.cn.deformityproj.dao.UserDAO;
import com.cn.deformityproj.iservice.IUserSV;
import com.xiaoleilu.hutool.util.StrUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author jiangcongcong
 * @date 2022/2/23 18:46
 */
@Service
public class UserSVImpl implements IUserSV {

    public static Logger logger = LoggerFactory.getLogger(UserSVImpl.class);

    @Autowired
    private UserDAO userDAO;

    public void register(Map<String,Object> reqMap) throws Exception{
        if(null!=reqMap&&reqMap.size()>0) {
            try {
                userDAO.register(reqMap);
            }catch (Exception e){
                logger.error("用户注册失败："+e.getMessage());
                throw new Exception("-9999",e);
            }
        }
    }


    public boolean checkUsernameIsRegister(String username) throws Exception{
        boolean result = true;
        if(!StrUtil.hasEmpty(username)){
            List<String> resultList = userDAO.checkUsernameIsRegister(username);
            if(null==resultList || resultList.size()==0){
                result = false;
            }
        }
        return result;
    }

    public Map<String,Object> queryUserPassword(String userId) throws Exception{
        Map<String,Object> resultMap = new HashMap<>();
        if(!StrUtil.hasEmpty(userId)){
            List<Map<String,Object>> resultList= userDAO.queryUserPassword(userId);
            if(null!=resultList&&resultList.size()>0){
                resultMap = resultList.get(0);
            }
        }
        return resultMap;
    }

    public Map<String,Object> queryUserList(Map<String,Object> reqMap) throws Exception{
        Map<String,Object> resultMap = new HashMap<>();
        int total = userDAO.queryUserListCount(reqMap);
        resultMap.put("total",total);
        if(total>0) {
            try {
                List<Map<String, Object>> resultList = userDAO.queryUserList(reqMap);
                resultMap.put("userList",resultList);
            }catch (Exception e){
                logger.error("查询用户列表失败+e");
                resultMap.put("userList",new ArrayList<>(2));
            }
        }
        return resultMap;
    }

    @Override
    public void addUser(Map<String,Object> reqMap) throws Exception{
        try {
            userDAO.addUser(reqMap);
        }catch (Exception e){
            logger.error("保存用户数据失败："+e);
            throw new Exception(e);
        }
    }

    @Override
    public void deleteUser(String userId) throws Exception{
        try {
            userDAO.deleteUser(userId);
        }catch (Exception e){
            logger.error("删除用户数据失败："+e);
            throw new Exception(e);
        }
    }

    @Override
    public void updateUser(Map<String,Object> reqMap) throws Exception{
        try {
            userDAO.updateUser(reqMap);
        }catch (Exception e){
            logger.error("更新用户数据失败："+e);
            throw new Exception(e);
        }
    }

}

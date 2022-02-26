package com.cn.deformityproj.iservice;

import java.util.List;
import java.util.Map;

/**
 * @author jiangcongcong
 * @date 2022/2/23 18:46
 */
public interface IUserSV {

    void register(Map<String,Object> reqMap) throws Exception;

    boolean checkUsernameIsRegister(String username) throws Exception;

    Map<String,Object> queryUserPassword(String userId) throws Exception;

    Map<String,Object> queryUserList(Map<String,Object> reqMap) throws Exception;

    void addUser(Map<String,Object> reqMap) throws Exception;

    void deleteUser(String userId) throws Exception;

    void updateUser(Map<String,Object> reqMap) throws Exception;
}

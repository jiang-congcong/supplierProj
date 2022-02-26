package com.cn.deformityproj.dao;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @author jiangcongcong
 * @date 2022/2/23 18:51
 */
@Mapper
public interface UserDAO {

    void register(Map<String,Object> reqMap) throws Exception;

    List<String> checkUsernameIsRegister(String username) throws Exception;

    List<Map<String,Object>> queryUserPassword(String userName) throws Exception;

    List<Map<String,Object>> queryUserList(Map<String,Object> reqMap) throws Exception;

    int queryUserListCount(Map<String,Object> reqMap) throws Exception;

    void addUser(Map<String,Object> reqMap) throws Exception;

    void deleteUser(String userId) throws Exception;

    void updateUser(Map<String,Object> reqMap) throws Exception;
}

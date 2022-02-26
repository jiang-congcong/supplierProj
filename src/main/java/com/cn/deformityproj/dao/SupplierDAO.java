package com.cn.deformityproj.dao;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @author jiangcongcong
 * @date 2022/2/25 18:39
 */
@Mapper
public interface SupplierDAO {

    void addSupplierInfo(Map<String,Object> reqMap) throws Exception;

    List<Map<String,Object>> querySupplierList(Map<String,Object> reqMap) throws Exception;

    int querySupplierListTotal(Map<String,Object> reqMap) throws Exception;

    void deleteSupplier(String userId) throws Exception;

    void updateSupplier(Map<String,Object> reqMap) throws Exception;

}

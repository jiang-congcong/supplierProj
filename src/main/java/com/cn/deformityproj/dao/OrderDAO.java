package com.cn.deformityproj.dao;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @author jiangcongcong
 * @date 2022/2/26 18:34
 */
@Mapper
public interface OrderDAO {

    List<Map<String,Object>> queryOrderList(Map<String,Object> reqMap) throws Exception;

    int queryOrderListCount(Map<String,Object> reqMap) throws Exception;

}

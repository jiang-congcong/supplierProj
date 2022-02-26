package com.cn.deformityproj.dao;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @author jiangcongcong
 * @date 2022/2/26 10:35
 */
@Mapper
public interface GoodsDAO {

    void addGoodsInfo(Map<String,Object> reqMap) throws Exception;

    List<Map<String,Object>> queryGoodsList(Map<String,Object> reqMap) throws Exception;

    int queryGoodsListCount(Map<String,Object> reqMap) throws Exception;

    void bugGoods(Map<String,Object> reqMap) throws Exception;

    Map<String,Object> queryGoodsInfoByGoodsId(String goodsId) throws Exception;

    void updateGoodsSku(Map<String,Object> reqMap) throws Exception;

    void updateGoodsInfo(Map<String,Object> reqMap) throws Exception;

    void deleteGoodsInfo(String goodsId) throws Exception;
}

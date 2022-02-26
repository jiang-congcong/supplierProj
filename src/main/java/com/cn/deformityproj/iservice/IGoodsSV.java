package com.cn.deformityproj.iservice;

import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

/**
 * @author jiangcongcong
 * @date 2022/2/26 10:34
 */
public interface IGoodsSV {

    void addGoodsInfo(Map<String,Object> reqMap) throws Exception;

    Map<String,Object> queryGoodsList(Map<String,Object> reqMap) throws Exception;

    void buyGoods(Map<String,Object> reqMap) throws Exception;

    void skuAdd(@RequestBody Map<String,Object> reqMap) throws Exception;

    void skuCut(@RequestBody Map<String,Object> reqMap) throws Exception;

    void updateGoods(@RequestBody Map<String,Object> reqMap) throws Exception;

    void deleteGoodsInfo(String goodsId) throws Exception;
}

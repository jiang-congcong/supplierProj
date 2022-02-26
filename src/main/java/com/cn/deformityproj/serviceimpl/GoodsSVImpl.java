package com.cn.deformityproj.serviceimpl;

import com.cn.deformityproj.controller.SupplierController;
import com.cn.deformityproj.dao.GoodsDAO;
import com.cn.deformityproj.iservice.IGoodsSV;
import com.cn.deformityproj.utils.CommonUtils;
import com.xiaoleilu.hutool.util.StrUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author jiangcongcong
 * @date 2022/2/26 10:34
 */
@Service
public class GoodsSVImpl implements IGoodsSV {

    @Autowired
    private GoodsDAO goodsDAO;

    @Autowired
    private CommonUtils commonUtils;

    public static Logger logger = LoggerFactory.getLogger(GoodsSVImpl.class);

    @Override
    public void addGoodsInfo(Map<String,Object> reqMap) throws Exception{
        try {
            goodsDAO.addGoodsInfo(reqMap);
        }catch (Exception e){
            logger.error("保存商品数据失败："+e);
            throw new Exception(e);
        }
    }

    @Override
    public Map<String, Object> queryGoodsList(Map<String, Object> reqMap) throws Exception {
        Map<String, Object> goodsIdToTypeName = new HashMap<>();
        goodsIdToTypeName.put("1", "轮椅");
        goodsIdToTypeName.put("2", "助听器");
        Map<String, Object> resultMap = new HashMap<>();
        List<Map<String, Object>> resultList = new ArrayList<>();
        try {
            int total = goodsDAO.queryGoodsListCount(reqMap);
            if (total > 0) {
                resultList = goodsDAO.queryGoodsList(reqMap);
                if (null != resultList && resultList.size() > 0) {
                    for (Map<String, Object> eachMap : resultList) {
                        String goodsCatalogId = (String) eachMap.get("goodsCatalogId");
                        if(null!=goodsIdToTypeName.get(goodsCatalogId)){
                            eachMap.put("goodsCatalogName",goodsIdToTypeName.get(goodsCatalogId));
                        }
                        else{
                            eachMap.put("goodsCatalogName","其他器械");
                        }
                        String goodsImage = (String)eachMap.get("goodsImage");
                        if(!StrUtil.hasEmpty(goodsImage)){
                            eachMap.put("goodsImage",commonUtils.dealImageTobase64(goodsImage));
                        }
                    }
                }

            }
            resultMap.put("total", total);
            resultMap.put("goodsList", resultList);
        } catch (Exception e) {
            logger.error("查询商品数据失败：" + e);
            throw new Exception(e);
        }
        return resultMap;
    }

    @Override
    public void buyGoods(Map<String,Object> reqMap) throws Exception{
        try {
            Map<String,Object> goodsInfo = goodsDAO.queryGoodsInfoByGoodsId((String)reqMap.get("goodsId"));
            int skuNum = (int)goodsInfo.get("skuNum");
            if((int)reqMap.get("goodsNum")>skuNum){
                throw new Exception("库存不足!");
            }
            goodsDAO.bugGoods(reqMap);//创建订单
            Map<String,Object> updateSku = new HashMap<>();
            updateSku.put("goodsId",reqMap.get("goodsId"));
            updateSku.put("goodsSku",skuNum-(int)reqMap.get("goodsNum"));
            goodsDAO.updateGoodsSku(updateSku);//更新库存
        }catch (Exception e){
            logger.error("下单失败："+e);
            throw new Exception(e);
        }
    }

    @Override
    public void skuAdd(@RequestBody Map<String,Object> reqMap) throws Exception{
        try {
            Map<String, Object> goodsInfo = goodsDAO.queryGoodsInfoByGoodsId((String) reqMap.get("goodsId"));
            int skuNum = (int) goodsInfo.get("skuNum");
            Map<String, Object> updateSku = new HashMap<>();
            updateSku.put("goodsId", reqMap.get("goodsId"));
            updateSku.put("goodsSku", skuNum + (int) reqMap.get("goodsNum"));
            goodsDAO.updateGoodsSku(updateSku);//入库
        }catch (Exception e){
            logger.error("入库失败："+e);
            throw new Exception(e);
        }
    }

    @Override
    public void skuCut(@RequestBody Map<String,Object> reqMap) throws Exception{
        try {
            Map<String, Object> goodsInfo = goodsDAO.queryGoodsInfoByGoodsId((String) reqMap.get("goodsId"));
            int skuNum = (int) goodsInfo.get("skuNum");
            if((int)reqMap.get("goodsNum")>skuNum){
                throw new Exception("库存不足!");
            }
            Map<String, Object> updateSku = new HashMap<>();
            updateSku.put("goodsId", reqMap.get("goodsId"));
            updateSku.put("goodsSku", skuNum - (int)reqMap.get("goodsNum"));
            goodsDAO.updateGoodsSku(updateSku);//出库
        }catch (Exception e){
            logger.error("出库失败："+e);
            throw new Exception(e);
        }
    }

    @Override
    public void updateGoods(@RequestBody Map<String,Object> reqMap) throws Exception{
        String goodsImage = (String) reqMap.get("goodsImage");
        if(!StrUtil.hasEmpty(goodsImage)) {
            reqMap.put("goodsImage", commonUtils.dealbase64ToImagePath(goodsImage));
        }
        reqMap.put("goodsPrice",new BigDecimal(Float.toString((float)reqMap.get("goodsPrice"))));
        try{
            goodsDAO.updateGoodsInfo(reqMap);
        }catch (Exception e){
            logger.error("修改商品失败："+e);
            throw new Exception(e);
        }
    }

    @Override
    public void deleteGoodsInfo(String goodsId) throws Exception{
        try{
            goodsDAO.deleteGoodsInfo(goodsId);
        }catch (Exception e){
            logger.error("删除商品失败："+e);
            throw new Exception(e);
        }
    }


}

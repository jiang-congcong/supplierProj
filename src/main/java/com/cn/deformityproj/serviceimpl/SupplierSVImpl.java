package com.cn.deformityproj.serviceimpl;

import com.cn.deformityproj.dao.SupplierDAO;
import com.cn.deformityproj.iservice.ISupplierSV;
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
 * @date 2022/2/25 18:39
 */
@Service
public class SupplierSVImpl implements ISupplierSV {

    public static Logger logger = LoggerFactory.getLogger(SupplierSVImpl.class);

    @Autowired
    private SupplierDAO supplierDAO;

    @Override
    public void addSupplierInfo(Map<String,Object> reqMap) throws Exception{
        try {
            supplierDAO.addSupplierInfo(reqMap);
        }catch (Exception e){
            logger.error("保存供应商数据失败："+e);
            throw new Exception(e);
        }
    }

    @Override
    public Map<String, Object> querySupplierList(Map<String, Object> reqMap) throws Exception {
        Map<String, Object> goodsIdToTypeName = new HashMap<>();
        goodsIdToTypeName.put("1", "轮椅");
        goodsIdToTypeName.put("2", "假肢");
        goodsIdToTypeName.put("3", "手杖");
        goodsIdToTypeName.put("4", "矫形器");
        goodsIdToTypeName.put("5", "盲人打字机");
        goodsIdToTypeName.put("6", "助听器");
        Map<String, Object> resultMap = new HashMap<>();
        List<Map<String, Object>> resultList = new ArrayList<>();
        try {
            int total = supplierDAO.querySupplierListTotal(reqMap);
            if (total > 0) {
                resultList = supplierDAO.querySupplierList(reqMap);
                if (null != resultList && resultList.size() > 0) {
                    for (Map<String, Object> eachMap : resultList) {
                        String supplierGoods = (String) eachMap.get("supplierGoods");
                        List<Map<String,Object>> goodsTypeList = new ArrayList<>();
                        if(!StrUtil.hasEmpty(supplierGoods)){
                            String[] strArr = supplierGoods.split(",");
                            for(String str:strArr){
                                Map<String,Object> goodsTypeMap = new HashMap<>();
                                goodsTypeMap.put("catalogId",str);
                                goodsTypeMap.put("catalogName",goodsIdToTypeName.get(str));
                                goodsTypeList.add(goodsTypeMap);
                            }
                            eachMap.put("supplierGoods", goodsTypeList);
                        }
                        else{
                            eachMap.put("supplierGoods", goodsTypeList);
                        }
                    }
                }

            }
            resultMap.put("total", total);
            resultMap.put("supplierList", resultList);
        } catch (Exception e) {
            logger.error("查询供应商数据失败：" + e);
            throw new Exception(e);
        }
        return resultMap;
    }

    @Override
    public void deleteSupplier(String supplierId) throws Exception{
        try {
            supplierDAO.deleteSupplier(supplierId);
        }catch (Exception e){
            logger.error("删除供应商数据失败："+e);
            throw new Exception(e);
        }
    }

    @Override
    public void updateSupplier(Map<String,Object> reqMap) throws Exception{
        try {
            supplierDAO.updateSupplier(reqMap);
        }catch (Exception e){
            logger.error("更新供应商数据失败："+e);
            throw new Exception(e);
        }
    }

}

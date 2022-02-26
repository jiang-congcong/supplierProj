package com.cn.deformityproj.serviceimpl;

import com.cn.deformityproj.dao.SupplierDAO;
import com.cn.deformityproj.iservice.ISupplierSV;
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
        goodsIdToTypeName.put("2", "助听器");
        Map<String, Object> resultMap = new HashMap<>();
        List<Map<String, Object>> resultList = new ArrayList<>();
        try {
            int total = supplierDAO.querySupplierListTotal(reqMap);
            if (total > 0) {
                resultList = supplierDAO.querySupplierList(reqMap);
                if (null != resultList && resultList.size() > 0) {
                    for (Map<String, Object> eachMap : resultList) {
                        Map<String, Object> goodType = new HashMap<>();
                        String supplierGoods = (String) eachMap.get("supplierGoods");

                        eachMap.put("supplierGoods", goodType);
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

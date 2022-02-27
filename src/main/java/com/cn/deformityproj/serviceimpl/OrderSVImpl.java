package com.cn.deformityproj.serviceimpl;

import com.cn.deformityproj.controller.SupplierController;
import com.cn.deformityproj.dao.OrderDAO;
import com.cn.deformityproj.iservice.IOrderSV;
import com.cn.deformityproj.utils.CommonUtils;
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
 * @date 2022/2/26 18:34
 */
@Service
public class OrderSVImpl implements IOrderSV {

    @Autowired
    private OrderDAO orderDAO;

    @Autowired
    private CommonUtils commonUtils;

    public static Logger logger = LoggerFactory.getLogger(OrderSVImpl.class);

    @Override
    public Map<String,Object> queryOrder(Map<String,Object> reqMap) throws Exception{
        Map<String,Object> resultMap = new HashMap<>();
        try{
            int total = orderDAO.queryOrderListCount(reqMap);
            List<Map<String,Object>> resultList = new ArrayList<>();
            if(total>0){
                resultList = orderDAO.queryOrderList(reqMap);
                if(null!=resultList&&resultList.size()>0){
                    for(Map<String,Object> eachMap:resultList){
                        String imagePath = eachMap.get("goodsImage").toString();
                        eachMap.put("goodsImage",commonUtils.dealImageTobase64(imagePath));
                    }
                }
            }
            resultMap.put("total",total);
            resultMap.put("orderList",resultList);
        }catch (Exception e){
            logger.error("查询订单失败:"+e);
            throw new Exception(e);
        }
        return resultMap;
    }
}

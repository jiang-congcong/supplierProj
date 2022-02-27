package com.cn.deformityproj.controller;

import com.cn.deformityproj.iservice.IOrderSV;
import com.cn.deformityproj.utils.Result;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author jiangcongcong
 * @date 2022/2/26 18:33
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private IOrderSV iOrderSV;

    public static Logger logger = LoggerFactory.getLogger(OrderController.class);

    @RequestMapping(method = RequestMethod.POST,value = "/list")
    @ResponseBody
    @ApiOperation(value = "订单列表")
    public Result queryOrderList(@RequestBody Map<String,Object> reqMap) throws Exception {
        Result result = new Result();
        if(null==reqMap.get("page")||null==reqMap.get("size")){
            result.setCode(400);
            result.setMessage("分页参数不能为空");
            return result;
        }
        int page = (int)reqMap.get("page")-1;
        int size = (int)reqMap.get("size");
        reqMap.put("start",page*size);
        try {
            Map<String,Object> resultMap = iOrderSV.queryOrder(reqMap);
            result.setData(resultMap);
            result.setCode(200);
            result.setMessage("查询订单数据成功");
        }catch (Exception e){
            logger.error("查询订单数据失败："+e);
            result.setCode(400);
            result.setMessage("查询订单数据失败");
        }
        return result;
    }

}

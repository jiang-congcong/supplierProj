package com.cn.deformityproj.controller;

import com.cn.deformityproj.iservice.IGoodsSV;
import com.cn.deformityproj.utils.CommonUtils;
import com.cn.deformityproj.utils.Result;
import com.xiaoleilu.hutool.util.StrUtil;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author jiangcongcong
 * @date 2022/2/26 10:34
 */
@RestController
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    private CommonUtils commonUtils;

    @Autowired
    private IGoodsSV iGoodsSV;

    public static Logger logger = LoggerFactory.getLogger(SupplierController.class);

    @RequestMapping(method = RequestMethod.POST,value = "/add")
    @ResponseBody
    @ApiOperation(value = "商品新增")
    public Result addGoods(@RequestBody Map<String,Object> reqMap) throws Exception {
        Result result = new Result();
        String goodsName = (String) reqMap.get("goodsName");
        String goodsImage = (String) reqMap.get("goodsImage");
        String goodsSupplierId = (String) reqMap.get("goodsSupplierId");
        String goodsCatalogId = (String) reqMap.get("goodsCatalogId");
        String goodsDescribe = (String) reqMap.get("goodsDescribe");
        if (StrUtil.hasEmpty(goodsName) || StrUtil.hasEmpty(goodsImage) || StrUtil.hasEmpty(goodsSupplierId) || StrUtil.hasEmpty(goodsCatalogId) || StrUtil.hasEmpty(goodsDescribe)) {
            result.setCode(400);
            result.setMessage("商品名称或商品图片或供应商id或商品类型id或商品描述不能为空！");
            return result;
        }
        if(null == reqMap.get("goodsPrice")||null == reqMap.get("goodsSku")|| (float)reqMap.get("goodsPrice")<0 || (int) reqMap.get("goodsSku")<0){
            result.setCode(400);
            result.setMessage("商品价格、库存均不能为空且大于0！");
            return result;
        }
        reqMap.put("goodsPrice",new BigDecimal(Float.toString((float)reqMap.get("goodsPrice"))));
        reqMap.put("goodsId",commonUtils.createAllId());
        reqMap.put("goodsImage",commonUtils.dealbase64ToImagePath(goodsImage));
        try {
            iGoodsSV.addGoodsInfo(reqMap);
            result.setCode(200);
            result.setMessage("保存商品数据成功");
        }catch (Exception e){
            logger.error("保存商品数据失败："+e);
            result.setCode(400);
            result.setMessage("保存商品数据失败");
        }
        return result;
    }

    @RequestMapping(method = RequestMethod.POST,value = "/list")
    @ResponseBody
    @ApiOperation(value = "商品列表")
    public Result queryGoodsList(@RequestBody Map<String,Object> reqMap) throws Exception {
        Result result = new Result();
        if(null==reqMap.get("page")||null==reqMap.get("size")){
            result.setCode(400);
            result.setMessage("分页参数不能为空");
            return result;
        }
        int page = (int)reqMap.get("page");
        int size = (int)reqMap.get("size");
        reqMap.put("start",page*size);
        try {
            Map<String,Object> resultMap = iGoodsSV.queryGoodsList(reqMap);
            result.setData(resultMap);
            result.setCode(200);
            result.setMessage("查询商品数据成功");
        }catch (Exception e){
            logger.error("查询商品数据失败："+e);
            result.setCode(400);
            result.setMessage("查询商品数据失败");
        }
        return result;
    }

    @RequestMapping(method = RequestMethod.POST,value = "/buy")
    @ResponseBody
    @ApiOperation(value = "商品下单")
    public Result buyGoods(@RequestBody Map<String,Object> reqMap) throws Exception {
        Result result = new Result();
        String goodsId = (String) reqMap.get("goodsId");
        String userId = (String) reqMap.get("userId");
        if (StrUtil.hasEmpty(goodsId) || StrUtil.hasEmpty(userId)) {
            result.setCode(400);
            result.setMessage("商品id或用户id不能为空！");
            return result;
        }
        if(null == reqMap.get("goodsNum")||(int) reqMap.get("goodsNum")<0){
            result.setCode(400);
            result.setMessage("下单数量不能为空且大于0！");
            return result;
        }
        String orderId = commonUtils.createAllId();
        reqMap.put("orderId",orderId);
        try {
            iGoodsSV.buyGoods(reqMap);
            result.setCode(200);
            result.setMessage("下单成功");
        }catch (Exception e){
            logger.error("下单失败："+e);
            result.setCode(400);
            result.setMessage("下单失败");
        }
        return result;
    }

    @RequestMapping(method = RequestMethod.POST,value = "/skuAdd")
    @ResponseBody
    @ApiOperation(value = "商品入库")
    public Result skuAdd(@RequestBody Map<String,Object> reqMap) throws Exception {
        Result result = new Result();
        String goodsId = (String) reqMap.get("goodsId");
        if (StrUtil.hasEmpty(goodsId)) {
            result.setCode(400);
            result.setMessage("商品id不能为空！");
            return result;
        }
        if (null == reqMap.get("goodsNum") || (int) reqMap.get("goodsNum") < 0) {
            result.setCode(400);
            result.setMessage("入库数量不能为空且大于0！");
            return result;
        }
        try {
            iGoodsSV.skuAdd(reqMap);
            result.setCode(200);
            result.setMessage("入库成功");
        }catch (Exception e){
            logger.error("入库失败："+e);
            result.setCode(400);
            result.setMessage("入库失败");
        }
        return result;
    }

    @RequestMapping(method = RequestMethod.POST,value = "/skuCut")
    @ResponseBody
    @ApiOperation(value = "商品出库")
    public Result skuCut(@RequestBody Map<String,Object> reqMap) throws Exception {
        Result result = new Result();
        String goodsId = (String) reqMap.get("goodsId");
        if (StrUtil.hasEmpty(goodsId)) {
            result.setCode(400);
            result.setMessage("商品id不能为空！");
            return result;
        }
        if (null == reqMap.get("goodsNum") || (int) reqMap.get("goodsNum") < 0) {
            result.setCode(400);
            result.setMessage("出库数量不能为空且大于0！");
            return result;
        }
        try {
            iGoodsSV.skuCut(reqMap);
            result.setCode(200);
            result.setMessage("出库成功");
        }catch (Exception e){
            logger.error("出库失败："+e);
            result.setCode(400);
            result.setMessage("出库失败");
        }
        return result;
    }

    @RequestMapping(method = RequestMethod.POST,value = "/update")
    @ResponseBody
    @ApiOperation(value = "商品修改")
    public Result updateGoods(@RequestBody Map<String,Object> reqMap) throws Exception {
        Result result = new Result();
        String goodsId = (String) reqMap.get("goodsId");
        String goodsName = (String) reqMap.get("goodsName");
        String goodsImage = (String) reqMap.get("goodsImage");
        String goodsSupplierId = (String) reqMap.get("goodsSupplierId");
        String goodsCatalogId = (String) reqMap.get("goodsCatalogId");
        String goodsDescribe = (String) reqMap.get("goodsDescribe");
        if (StrUtil.hasEmpty(goodsName) || StrUtil.hasEmpty(goodsImage) || StrUtil.hasEmpty(goodsSupplierId) || StrUtil.hasEmpty(goodsCatalogId) || StrUtil.hasEmpty(goodsDescribe) || StrUtil.hasEmpty(goodsId)) {
            result.setCode(400);
            result.setMessage("商品id或商品名称或商品图片或供应商id或商品类型id或商品描述不能为空！");
            return result;
        }
        if (null == reqMap.get("goodsPrice") || (float) reqMap.get("goodsPrice") < 0) {
            result.setCode(400);
            result.setMessage("商品价格不能为空且大于0！");
            return result;
        }
        try {
            iGoodsSV.updateGoods(reqMap);
            result.setCode(200);
            result.setMessage("修改商品成功");
        }catch (Exception e){
            logger.error("修改商品失败："+e);
            result.setCode(400);
            result.setMessage("修改商品失败");
        }
        return result;
    }

    @RequestMapping(method = RequestMethod.POST,value = "/delete")
    @ResponseBody
    @ApiOperation(value = "商品删除")
    public Result deleteGoods(@RequestBody Map<String,Object> reqMap) throws Exception {
        Result result = new Result();
        String goodsId = (String) reqMap.get("goodsId");
        if (StrUtil.hasEmpty(goodsId)) {
            result.setCode(400);
            result.setMessage("商品id不能为空！");
            return result;
        }
        try {
            iGoodsSV.deleteGoodsInfo(goodsId);
            result.setCode(200);
            result.setMessage("删除商品成功");
        }catch (Exception e){
            logger.error("删除商品失败："+e);
            result.setCode(400);
            result.setMessage("删除商品失败");
        }
        return result;
    }


}

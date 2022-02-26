package com.cn.deformityproj.controller;

import com.cn.deformityproj.iservice.ISupplierSV;
import com.cn.deformityproj.serviceimpl.SupplierSVImpl;
import com.cn.deformityproj.utils.CommonUtils;
import com.cn.deformityproj.utils.Result;
import com.xiaoleilu.hutool.util.StrUtil;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author jiangcongcong
 * @date 2022/2/25 18:14
 */
@RestController
@RequestMapping("/supplier")
public class SupplierController {

    @Autowired
    private CommonUtils commonUtils;

    @Autowired
    private ISupplierSV iSupplierSV;

    public static Logger logger = LoggerFactory.getLogger(SupplierController.class);

    @RequestMapping(method = RequestMethod.POST,value = "/add")
    @ResponseBody
    @ApiOperation(value = "供应商新增")
    public Result addSupplier(@RequestBody Map<String,Object> reqMap) throws Exception {
        Result result = new Result();
        String supplierName = (String) reqMap.get("supplierName");
        String supplierPhone = (String) reqMap.get("supplierPhone");
        String supplierAddress = (String) reqMap.get("supplierAddress");
        String supplierDescribe = (String) reqMap.get("supplierDescribe");
        List<String> supplierGoods = (List<String>) reqMap.get("supplierGoods");
        if (StrUtil.hasEmpty(supplierName) || StrUtil.hasEmpty(supplierPhone) || StrUtil.hasEmpty(supplierAddress) || StrUtil.hasEmpty(supplierDescribe) || (null==supplierGoods||supplierGoods.size()<1)) {
            result.setCode(400);
            result.setMessage("供应商名或供应商地址或手机号或供应商描述或供应商售卖商品类型不能为空！");
            return result;
        }
        String supplierGoodsStr = String.join(",",supplierGoods);
        reqMap.put("supplierGoods",supplierGoodsStr);
        String userId = commonUtils.createAllId(); //生成用户id
        reqMap.put("supplierId",userId);
        try {
            iSupplierSV.addSupplierInfo(reqMap);
            result.setCode(200);
            result.setMessage("保存供应商数据成功");
        }catch (Exception e){
            logger.error("保存供应商数据失败："+e);
            result.setCode(400);
            result.setMessage("保存供应商数据失败");
        }
        return result;
    }

    @RequestMapping(method = RequestMethod.POST,value = "/list")
    @ResponseBody
    @ApiOperation(value = "供应商列表查询")
    public Result querySupplierList(@RequestBody Map<String,Object> reqMap) throws Exception {
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
            Map<String,Object> resultMap = iSupplierSV.querySupplierList(reqMap);
            result.setData(resultMap);
            result.setCode(200);
            result.setMessage("查询供应商数据成功");
        }catch (Exception e){
            logger.error("查询供应商数据失败："+e);
            result.setCode(400);
            result.setMessage("查询供应商数据失败");
        }
        return result;
    }

    @RequestMapping(method = RequestMethod.POST,value = "/delete")
    @ResponseBody
    @ApiOperation(value = "供应商删除")
    public Result deleteSupplier(@RequestBody Map<String,Object> reqMap) throws Exception {
        Result result = new Result();
        String supplierId = (String) reqMap.get("supplierId");
        if (StrUtil.hasEmpty(supplierId)) {
            result.setCode(400);
            result.setMessage("供应商id不能为空！");
            return result;
        }
        try {
            iSupplierSV.deleteSupplier(supplierId);
            result.setCode(200);
            result.setMessage("删除供应商数据成功");
        }catch (Exception e){
            logger.error("删除供应商数据失败："+e);
            result.setCode(400);
            result.setMessage("删除供应商数据失败");
        }
        return result;
    }

    @RequestMapping(method = RequestMethod.POST,value = "/update")
    @ResponseBody
    @ApiOperation(value = "供应商修改")
    public Result updateSupplier(@RequestBody Map<String,Object> reqMap) throws Exception {
        Result result = new Result();
        String supplierName = (String) reqMap.get("supplierName");
        String supplierPhone = (String) reqMap.get("supplierPhone");
        String supplierAddress = (String) reqMap.get("supplierAddress");
        String supplierDescribe = (String) reqMap.get("supplierDescribe");
        List<String> supplierGoods = (List<String>) reqMap.get("supplierGoods");
        if (StrUtil.hasEmpty(supplierName) || StrUtil.hasEmpty(supplierPhone) || StrUtil.hasEmpty(supplierAddress) || StrUtil.hasEmpty(supplierDescribe) || (null == supplierGoods || supplierGoods.size() < 1)) {
            result.setCode(400);
            result.setMessage("供应商名或供应商地址或手机号或供应商描述或供应商售卖商品类型不能为空！");
            return result;
        }
        String supplierGoodsStr = String.join(",",supplierGoods);
        reqMap.put("supplierGoods",supplierGoodsStr);
        try {
            iSupplierSV.updateSupplier(reqMap);
            result.setCode(200);
            result.setMessage("修改供应商数据成功");
        }catch (Exception e){
            logger.error("修改供应商数据失败："+e);
            result.setCode(400);
            result.setMessage("修改供应商数据失败");
        }
        return result;
    }





}

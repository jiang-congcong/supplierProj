package com.cn.deformityproj.iservice;

import java.util.List;
import java.util.Map;

/**
 * @author jiangcongcong
 * @date 2022/2/25 18:38
 */
public interface ISupplierSV {

    void addSupplierInfo(Map<String,Object> reqMap) throws Exception;

    Map<String,Object> querySupplierList(Map<String,Object> reqMap) throws Exception;

    void deleteSupplier(String supplierId) throws Exception;

    void updateSupplier(Map<String,Object> reqMap) throws Exception;
}

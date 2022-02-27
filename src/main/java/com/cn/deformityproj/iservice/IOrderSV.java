package com.cn.deformityproj.iservice;

import java.util.Map;

/**
 * @author jiangcongcong
 * @date 2022/2/26 18:34
 */
public interface IOrderSV {

    Map<String,Object> queryOrder(Map<String,Object> reqMap) throws Exception;
}

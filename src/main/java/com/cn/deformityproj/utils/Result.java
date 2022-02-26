package com.cn.deformityproj.utils;

import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author jiangcongcong
 * @date 2022/2/23 17:41
 */
@Component
public class Result {
    private int code;
    private String message;
    private Map<String,Object> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Result{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}

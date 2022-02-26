package com.cn.deformityproj.utils;

import com.xiaoleilu.hutool.util.StrUtil;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * @author jiangcongcong
 * @date 2022/2/23 17:53
 */
@Component
public class CommonUtils {

    @Autowired
    private RedisOperationUtils redisOperationUtils;

    public static Logger logger = LoggerFactory.getLogger(CommonUtils.class);

    //生成id公共方法
    public String createAllId(){
        String dateString = new SimpleDateFormat("yyyyMMddHHmmssss").format(new Date());
        Object redisNum = redisOperationUtils.get("TABLEIDKEY");
        String resultStr = "";
        if(null==redisNum){
            int initStr = new Random().nextInt(100000);//产生随机数
            redisOperationUtils.set("TABLEIDKEY",String.valueOf(initStr));
        }
        else{
            resultStr = redisNum.toString();
            String putValue = String.valueOf(Double.valueOf(resultStr)+1);
            if(putValue.endsWith(".0")){
                putValue = putValue.substring(0,putValue.length()-2);
            }
            redisOperationUtils.set("TABLEIDKEY",putValue); //redis键值加1
        }
        return StrUtil.hasEmpty(resultStr) ? dateString : dateString+resultStr;
    }

    public String createAndUpdateToken(String userId){
        int salt = new Random().nextInt(1000);
        Md5Hash md5Hash = new Md5Hash(userId,String.valueOf(salt),1024);
        String token = md5Hash.toHex();
        int overTime = 24*60*60;//过期时间一天
        redisOperationUtils.set(token,userId,overTime);
        return token;
    }

}

package com.cgnpc.bbxpark.workbench.cudauth.utils;

import com.cgnpc.cud.cache.redis.RedisUtil;

import java.util.Set;

/**
 * @Description
 * @Author P629041
 * @Date 2023/5/24 16:28
 */
public class UserPermissionUtils {



    /**
     * @Author P629041
     * @Description 缓存用户数据
     * @Date 16:18 2023/5/24
     * @Param []
     * @return void
     **/
    public static void cachePut(String key,Set<String> paramSet){
        //存入redis 过期时间12 小时
        RedisUtil.set(key ,paramSet,12 * 60 * 60);
    }

    /**
     * @Author P629041
     * @Description 获取缓存数据
     * @Date 16:18 2023/5/24
     * @Param []
     * @return void
     **/
    public static Set<String> cacheGet(String key){
        //存入redis
        Set<String> result = (Set<String>)RedisUtil.get(key);
        return result;
    }


}

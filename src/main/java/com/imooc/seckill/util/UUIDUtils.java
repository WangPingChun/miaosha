package com.imooc.seckill.util;

import java.util.UUID;

/**
 * @author : chris
 * 2018-07-28
 */
public class UUIDUtils {
    public static String uuid() {
        return UUID.randomUUID().toString().replace("-","");
    }
}

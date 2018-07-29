package com.imooc.seckill.util;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * @author : chris
 * 2018-07-27
 */
public class MD5Utils {

    private static final String SALT = "1a2b3c4d";

    private static String md5(String src) {
        return DigestUtils.md5Hex(src);
    }


    private static String md5Password(String pass) {
        final String passSalt = SALT.charAt(0) + SALT.charAt(2) + pass + SALT.charAt(1) + SALT.charAt(3);
        return md5(passSalt);
    }

    private static String md5Password(String pass, String salt) {
        final String passSalt = salt.charAt(0) + salt.charAt(2) + pass + salt.charAt(1) + salt.charAt(3);
        return md5(passSalt);
    }

    public static String dbPassword(String pass, String salt) {
        return md5Password(md5Password(pass), salt);
    }

    public static void main(String[] args) {
        System.out.println(md5Password("123456","test"));
    }
}

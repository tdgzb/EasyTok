package com.utlis;
import org.apache.commons.codec.digest.DigestUtils;


public class Sha1Cipher {

    private static final String CONST_SALT = "dmSFL1bYfWEweaHtXYmMWEDxcx7Du8lJtuGcTX6i1bYtQAgy7K2XxOH9tFjDCB3R";
    private static String salt="yam1";

    /**
     * 使用SHA1算法加密
     *
     * @param plainText 明文
     * @return 返回加密后的密文
     */
    public static String encryptBySha1(String plainText) {
        return Sha1Cipher.encrypt(salt + plainText + Sha1Cipher.CONST_SALT);
    }


    private static String encrypt(String plainText) {
        return DigestUtils.sha1Hex(plainText);
    }
}

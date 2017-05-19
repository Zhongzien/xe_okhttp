package com.okhttplib.utils;

import java.security.MessageDigest;

/**
 * Created by Administrator on 2017/5/17 0017.
 */

public final class DigitalSignature {

    public static String signatureMD5(String msg) throws Exception {
        StringBuffer result = new StringBuffer();

        if (msg != null) {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytes = md.digest(msg.getBytes());
            for (int i = 0; i < bytes.length; i++) {
                String str = Integer.toHexString(bytes[i] & 0xFF);
                if (str.length() == 1)
                    str += "F";
                result.append(str);
            }
        }

        return result.toString();
    }

}

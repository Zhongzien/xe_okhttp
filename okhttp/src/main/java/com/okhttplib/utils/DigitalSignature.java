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


    public static String byteArrayToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte element : bytes) {
            int v = element & 0xFF;
            if (v < 16) {
                sb.append('0');
            }
            sb.append(Integer.toHexString(v));
        }
        return sb.toString();
    }

    public static byte[] hexStringToByteArray(String hex) {
        int length = hex.length();
        byte[] bytes = new byte[length / 2];
        for (int i = 0; i < length; i += 2) {
            bytes[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4) + Character.digit(hex.charAt(i + 1), 16));
        }
        return bytes;
    }
}

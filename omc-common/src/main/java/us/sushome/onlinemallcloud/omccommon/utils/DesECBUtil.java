package us.sushome.onlinemallcloud.omccommon.utils;


import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @Author xuehao
 * @Date 2023/12/14
 * @Description
 **/
public class DesECBUtil {

    /*
     * md5加密
     * */
    public static String encryptMD5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : messageDigest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * The key is less than 8 bits to fill
     *
     * @param
     */
    public static byte[] getKey(String keyRule) {
        Key key = null;
        byte[] keyByte = keyRule.getBytes();
        // Creates an empty octet array, 0 by default
        byte[] byteTemp = new byte[8];
        // Converts a user-specified rule to an octet array
        for (int i = 0; i < byteTemp.length && i < keyByte.length; i++) {
            byteTemp[i] = keyByte[i];
        }
        key = new SecretKeySpec(byteTemp, "DES");
        return key.getEncoded();
    }

    /***
     * decrypt data
     * @param decryptString
     * @param clientId
     * @param clientSecret
     * @return
     * @throws Exception
     */

    public static String decryptDES(String decryptString, String clientId,String clientSecret) throws Exception {
        //md5加密
        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append(clientId).append(":").append(clientSecret);
        System.out.println("stringBuilder.toString():  "+stringBuilder.toString());
        String decryptKey= encryptMD5(stringBuilder.toString());
        System.out.println("decryptKey:"+decryptKey);
        byte[] sourceBytes = Base64.decodeBase64(decryptString);
        System.out.println("sourceBytes:"+sourceBytes);
        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(getKey(decryptKey), "DES"));
        byte[] decoded = cipher.doFinal(sourceBytes);
        return new String(decoded, "UTF-8");
    }

    public static String getDecryptedDes(String token){
        String decryptedText = "";
        try{
            decryptedText = decryptDES(token,"HLFY","cde1746e-91d8-42d7-bf7c-bdfd83df7039");
            System.out.println("decryptedText"+ decryptedText);
            return decryptedText;
        }catch (Exception e){
            System.out.println("e:"+e.getMessage());
            return "";
        }
    }

    public static void main(String[] args) {
        getDecryptedDes("5a7wexbw8a0=");
    }
}

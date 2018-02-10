package org.ayo.codec;

import android.util.Base64;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by wanglei on 2016/11/28.
 */

public class Codec {
    public static class md5 {

        public static String encode(String buffer) {
            char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
            try {
                MessageDigest mdTemp = MessageDigest.getInstance(Algorithm.MD5.getType());
                mdTemp.update(buffer.getBytes());
                byte[] md = mdTemp.digest();
                int j = md.length;
                char[] str = new char[j * 2];
                int k = 0;
                for (int i = 0; i < j; i++) {
                    byte byte0 = md[i];
                    str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                    str[k++] = hexDigits[byte0 & 0xf];
                }
                return new String(str);
            } catch (Exception e) {
                return null;
            }
        }

        /**
         * 十六进制
         *
         * @param buffer
         * @return
         */
        public static String encode(byte[] buffer) {
            char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
            try {
                MessageDigest mdTemp = MessageDigest.getInstance(Algorithm.MD5.getType());
                mdTemp.update(buffer);
                byte[] md = mdTemp.digest();
                int j = md.length;
                char[] str = new char[j * 2];
                int k = 0;
                for (int i = 0; i < j; i++) {
                    byte byte0 = md[i];
                    str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                    str[k++] = hexDigits[byte0 & 0xf];
                }
                return new String(str);
            } catch (Exception e) {
                return null;
            }
        }

        /**
         * 返回最原始的md5编码值，没有变换成16进制的
         * @param buffer
         * @return
         */
        public static byte[] getRawDigest(byte[] buffer) {
            try {
                MessageDigest mdTemp = MessageDigest.getInstance(Algorithm.MD5.getType());
                mdTemp.update(buffer);
                return mdTemp.digest();

            } catch (Exception e) {
                return null;
            }
        }


        private static String encodeInputStream(final InputStream is, final int bufLen) {
            if (is == null || bufLen <= 0) {
                return null;
            }
            try {
                MessageDigest md = MessageDigest.getInstance(Algorithm.MD5.getType());
                StringBuilder md5Str = new StringBuilder(32);

                byte[] buf = new byte[bufLen];
                int readCount = 0;
                while ((readCount = is.read(buf)) != -1) {
                    md.update(buf, 0, readCount);
                }

                byte[] hashValue = md.digest();

                for (int i = 0; i < hashValue.length; i++) {
                    md5Str.append(Integer.toString((hashValue[i] & 0xff) + 0x100, 16).substring(1));
                }
                return md5Str.toString();
            } catch (Exception e) {
                return null;
            }
        }

        /**
         * 对文件进行md5
         *
         * @param filePath 文件路径
         * @return
         */
        public static String encodeFile(final String filePath) {
            if (filePath == null) {
                return null;
            }

            File f = new File(filePath);
            if (f.exists()) {
                return encodeFile(f, 1024 * 100);
            }
            return null;
        }

        /**
         * 文件md5
         *
         * @param file
         * @return
         */
        public static String encodeFile(final File file) {
            return encodeFile(file, 1024 * 100);
        }


        private static String encodeFile(final File file, final int bufLen) {
            if (file == null || bufLen <= 0 || !file.exists()) {
                return null;
            }

            FileInputStream fin = null;
            try {
                fin = new FileInputStream(file);
                String md5 = encodeInputStream(fin, (int) (bufLen <= file.length() ? bufLen : file.length()));
                fin.close();
                return md5;

            } catch (Exception e) {
                return null;

            } finally {
                try {
                    if (fin != null) {
                        fin.close();
                    }
                } catch (IOException e) {

                }
            }
        }

    }


    public static class base64 {

        public static byte[] encode(byte[] plain) {
            return Base64.encode(plain, Base64.DEFAULT);
        }

        public static String encodeToString(byte[] plain) {
            return Base64.encodeToString(plain, Base64.DEFAULT);
        }

        public static byte[] decode(String text) {
            return Base64.decode(text, Base64.DEFAULT);
        }

        public static byte[] decode(byte[] text) {
            return Base64.decode(text, Base64.DEFAULT);
        }
    }


    public static class sha {

        public static byte[] encrypt(byte[] data) throws Exception {
            MessageDigest sha = MessageDigest.getInstance(Algorithm.SHA.getType());
            sha.update(data);
            return sha.digest();
        }

    }


    public static class mac {
        /**
         * 初始化HMAC密钥
         *
         * @param algorithm 算法，可为空。默认为：Algorithm.Hmac_MD5
         * @return
         * @throws Exception
         */
        public static String initMacKey(Algorithm algorithm) throws Exception {
            if (algorithm == null) algorithm = Algorithm.Hmac_MD5;
            KeyGenerator keyGenerator = KeyGenerator.getInstance(algorithm.getType());
            SecretKey secretKey = keyGenerator.generateKey();

            return base64.encodeToString(secretKey.getEncoded());
        }

        /**
         * HMAC加密
         *
         * @param plain     明文
         * @param key       key
         * @param algorithm 算法，可为空。默认为：Algorithm.Hmac_MD5
         * @return
         * @throws Exception
         */
        public static byte[] encrypt(byte[] plain, String key, Algorithm algorithm) throws Exception {
            if (algorithm == null) algorithm = Algorithm.Hmac_MD5;
            SecretKey secretKey = new SecretKeySpec(base64.decode(key), algorithm.getType());
            Mac mac = Mac.getInstance(secretKey.getAlgorithm());
            mac.init(secretKey);

            return mac.doFinal(plain);
        }
    }


    public static class des {

        private static Key key;// 密钥的key值

        private static byte[] DESkey;

        private static byte[] DESIV;

        private static AlgorithmParameterSpec iv = null;// 加密算法的参数接口

        private static void init() {
            try {
                DESkey = "T3qAL3Mh".getBytes("UTF-8");// 设置密钥
                DESIV = "RCh2M8xE".getBytes("UTF-8");
                DESKeySpec keySpec = new DESKeySpec(DESkey);// 设置密钥参数
                iv = new IvParameterSpec(DESIV);// 设置向量
                SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");// 获得密钥工厂
                key = keyFactory.generateSecret(keySpec);// 得到密钥对象
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /**
         * 加密String 明文输入密文输出
         *
         * @param inputString 待加密的明文
         * @return 加密后的字符串
         */
        public static String encode(String inputString) {
            init();
            byte[] byteMi = null;
            byte[] byteMing = null;
            String outputString = "";
            try {
                byteMing = inputString.getBytes("UTF-8");
                byteMi = getEncCode(byteMing);
                byte[] temp = Base64.encode(byteMi, Base64.DEFAULT);
                outputString = new String(temp);
            } catch (Exception e) {
            } finally {
                byteMing = null;
                byteMi = null;
            }
            return outputString;
        }

        /**
         * 解密String 以密文输入明文输出
         *
         * @param inputString 需要解密的字符串
         * @return 解密后的字符串
         */
        public static String decode(String inputString) {
            init();
            byte[] byteMing = null;
            byte[] byteMi = null;
            String strMing = "";
            try {
                byteMi = Base64.decode(inputString.getBytes(), Base64.DEFAULT);
                byteMing = getDesCode(byteMi);
                strMing = new String(byteMing, "UTF8");
            } catch (Exception e) {
            } finally {
                byteMing = null;
                byteMi = null;
            }
            return strMing.replace("\n", "");
        }

        /**
         * 加密以byte[]明文输入,byte[]密文输出
         *
         * @param bt 待加密的字节码
         * @return 加密后的字节码
         */
        private static byte[] getEncCode(byte[] bt) {
            byte[] byteFina = null;
            Cipher cipher;
            try {
                // 得到Cipher实例
                cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
                cipher.init(Cipher.ENCRYPT_MODE, key, iv);
                byteFina = cipher.doFinal(bt);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                cipher = null;
            }
            return byteFina;
        }

        /**
         * 解密以byte[]密文输入,以byte[]明文输出
         *
         * @param bt 待解密的字节码
         * @return 解密后的字节码
         */
        private static byte[] getDesCode(byte[] bt) {
            Cipher cipher;
            byte[] byteFina = null;
            try {
                // 得到Cipher实例
                cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
                cipher.init(Cipher.DECRYPT_MODE, key, iv);
                byteFina = cipher.doFinal(bt);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                cipher = null;
            }
            return byteFina;
        }
    }


    public static class rsa {

        public static final String SIGNATURE_ALGORITHM = "MD5withRSA";

        private static final String PUBLIC_KEY = "RSAPublicKey";
        private static final String PRIVATE_KEY = "RSAPrivateKey";


        /**
         * 用私钥对信息生成数字签名
         *
         * @param data       加密数据
         * @param privateKey 私钥
         * @return
         * @throws Exception
         */
        public static String sign(byte[] data, String privateKey) throws Exception {
            byte[] keyBytes = base64.decode(privateKey);        // 解密由base64编码的私钥
            PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);   // 构造PKCS8EncodedKeySpec对象
            KeyFactory keyFactory = KeyFactory.getInstance(Algorithm.RSA.getType());    // KEY_ALGORITHM 指定的加密算法
            PrivateKey priKey = keyFactory.generatePrivate(pkcs8KeySpec);        // 取私钥匙对象
            Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);   // 用私钥对信息生成数字签名
            signature.initSign(priKey);
            signature.update(data);

            return base64.encodeToString(signature.sign());
        }

        /**
         * 校验数字签名
         *
         * @param data      加密数据
         * @param publicKey 公钥
         * @param sign      数字签名
         * @return
         * @throws Exception
         */
        public static boolean verify(byte[] data, String publicKey, String sign)
                throws Exception {

            byte[] keyBytes = base64.decode(publicKey); // 解密由base64编码的公钥
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);  // 构造X509EncodedKeySpec对象
            KeyFactory keyFactory = KeyFactory.getInstance(Algorithm.RSA.getType());  // KEY_ALGORITHM 指定的加密算法
            PublicKey pubKey = keyFactory.generatePublic(keySpec);   // 取公钥对象

            Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
            signature.initVerify(pubKey);
            signature.update(data);

            return signature.verify(base64.decode(sign));
        }

        /**
         * 用私钥解密
         *
         * @param data
         * @param key
         * @return
         * @throws Exception
         */
        public static byte[] decryptByPrivateKey(byte[] data, String key)
                throws Exception {
            byte[] keyBytes = base64.decode(key);   // 对密钥解密

            PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);   // 取得私钥
            KeyFactory keyFactory = KeyFactory.getInstance(Algorithm.RSA.getType());
            Key privateKey = keyFactory.generatePrivate(pkcs8KeySpec);

            // 对数据解密
            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
            cipher.init(Cipher.DECRYPT_MODE, privateKey);

            return cipher.doFinal(data);
        }

        /**
         * 用公钥解密
         *
         * @param data
         * @param key
         * @return
         * @throws Exception
         */
        public static byte[] decryptByPublicKey(byte[] data, String key)
                throws Exception {
            byte[] keyBytes = base64.decode(key);       // 对密钥解密

            // 取得公钥
            X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(Algorithm.RSA.getType());
            Key publicKey = keyFactory.generatePublic(x509KeySpec);

            // 对数据解密
            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
            cipher.init(Cipher.DECRYPT_MODE, publicKey);

            return cipher.doFinal(data);
        }

        /**
         * 用公钥加密
         *
         * @param data
         * @param key
         * @return
         * @throws Exception
         */
        public static byte[] encryptByPublicKey(byte[] data, String key)
                throws Exception {
            byte[] keyBytes = base64.decode(key);   // 对公钥解密

            // 取得公钥
            X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(Algorithm.RSA.getType());
            Key publicKey = keyFactory.generatePublic(x509KeySpec);

            // 对数据加密
            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);

            return cipher.doFinal(data);
        }

        /**
         * 用私钥加密
         *
         * @param data
         * @param key
         * @return
         * @throws Exception
         */
        public static byte[] encryptByPrivateKey(byte[] data, String key)
                throws Exception {

            byte[] keyBytes = base64.decode(key);   // 对密钥解密

            // 取得私钥
            PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(Algorithm.RSA.getType());
            Key privateKey = keyFactory.generatePrivate(pkcs8KeySpec);

            // 对数据加密
            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);

            return cipher.doFinal(data);
        }

        /**
         * 取得私钥
         *
         * @param keyMap
         * @return
         * @throws Exception
         */
        public static String getPrivateKey(Map<String, Object> keyMap)
                throws Exception {
            Key key = (Key) keyMap.get(PRIVATE_KEY);

            return base64.encodeToString(key.getEncoded());
        }

        /**
         * 取得公钥
         *
         * @param keyMap
         * @return
         * @throws Exception
         */
        public static String getPublicKey(Map<String, Object> keyMap)
                throws Exception {
            Key key = (Key) keyMap.get(PUBLIC_KEY);

            return base64.encodeToString(key.getEncoded());
        }

        /**
         * 初始化密钥
         *
         * @return
         * @throws Exception
         */
        public static Map<String, Object> generateKey() throws Exception {
            KeyPairGenerator keyPairGen = KeyPairGenerator
                    .getInstance(Algorithm.RSA.getType());
            keyPairGen.initialize(1024);

            KeyPair keyPair = keyPairGen.generateKeyPair();
            RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();    // 公钥
            RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();     // 私钥
            Map<String, Object> keyMap = new HashMap<String, Object>(2);

            keyMap.put(PUBLIC_KEY, publicKey);
            keyMap.put(PRIVATE_KEY, privateKey);
            return keyMap;
        }

    }


    public enum Algorithm {
        SHA("SHA"),
        MD5("MD5"),
        Hmac_MD5("HmacMD5"),
        Hmac_SHA1("HmacSHA1"),
        Hmac_SHA256("HmacSHA256"),
        Hmac_SHA384("HmacSHA384"),
        Hmac_SHA512("HmacSHA512"),
        DES("DES"),
        RSA("RSA");

        private String type;

        Algorithm(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }
    }
}

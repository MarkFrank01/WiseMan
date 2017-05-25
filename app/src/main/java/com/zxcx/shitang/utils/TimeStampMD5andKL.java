package com.zxcx.shitang.utils;

import java.security.MessageDigest;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * packageName  : com.bidekeji.util
 * Company      : 彼得科技
 * User         : zj
 * Date         : 16/12/3
 * Time         : 下午1:17
 * Description  :
 */
public class TimeStampMD5andKL {

    // 规定的超时时间界限
    private Long defaultTimeOut;

    // 密钥
    private final String secretKey = "ZXCX@LiuSan";

    /**
     * MD5加密，32位
     *
     * @param inStr
     *            要加密
     * @return
     */
    private String MD5(String inStr) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            //System.out.println(e.toString());
            e.printStackTrace();
            return "";
        }
        char[] charArray = inStr.toCharArray();
        byte[] byteArray = new byte[charArray.length];

        for (int i = 0; i < charArray.length; i++)
            byteArray[i] = (byte) charArray[i];

        byte[] md5Bytes = md5.digest(byteArray);

        StringBuffer hexValue = new StringBuffer();

        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16)
                hexValue.append("0");
            hexValue.append(Integer.toHexString(val));
        }

        return hexValue.toString();
    }

    /**
     * 可逆的位运算加密算法
     *
     * @param inStr
     *            要加密的内容
     * @return
     */
    private String KL(String inStr) {
        // String s = new String(inStr);
        char[] a = inStr.toCharArray();
        for (int i = 0; i < a.length; i++) {
            a[i] = (char) (a[i] ^ '*');
        }
        String s = new String(a);
        return s;
    }

    /**
     * 位运算加密算法
     *
     * @param inStr
     *            要解密的内容
     * @return
     */
    private String JM(String inStr) {
        char[] a = inStr.toCharArray();
        for (int i = 0; i < a.length; i++) {
            a[i] = (char) (a[i] ^ '*');
        }
        String k = new String(a);
        return k;
    }

    /**
     * 加密接口
     *
     * @param appLocalStartTime
     *            app相对于服务器时间的时间
     * @param serverStartTime
     *            服务器时间
     * @return
     */
    public String JiamiByMiYue(Long appLocalStartTime, Long serverStartTime) {
        // 模拟算出当前服务器时间戳
        // System.currentTimeMillis() -- 当前APP的本地时间戳
        // appLocalStartTime -- APP开启的本地时间戳
        // serverStartTime -- APP开启时服务器的时间戳
        String now = new String(String.valueOf(System.currentTimeMillis() - appLocalStartTime + serverStartTime));
        // 建立非线程安全的StringBuilder存储结果
        StringBuilder result = new StringBuilder();
        // 拿到密钥MD5加密后的值
        String key = MD5(secretKey);

        // 循环遍历，根据时间戳长度进行对位加密
        for (int i = 0, j = 0; i < now.length(); i++, j++) {
            // 假设时间戳长度大于32位，则要进行密文复位
            if (j == key.length()) {
                j = 0;
            }
            // 计算和
            int a = (Integer.valueOf(now.charAt(i)) + Integer.valueOf(key.charAt(j)));
            // 进行补位并传进结果集里面
            if (a >= 100 && a <= 999) {
                result.append(a);
            } else if (a >= 10 && a <= 99) {
                result.append("0" + a);
            } else if (a > 1) {
                result.append("00" + a);
            }
        }
        // 再次通过位运算加密
        return KL(result.toString());
    }

    /**
     * 服务端解密接口
     *
     * @param ciphertext
     *            需要解密的内容
     * @return
     */
    private Long JieMiByMiYue(String ciphertext) {
        // 拿到密钥MD5加密后的值
        String key = MD5(secretKey);

        // 创建要返回的对象
        StringBuilder result = new StringBuilder();

        // 位运算解密
        StringBuilder pppro = new StringBuilder(JM(ciphertext));

        // 得到解密半成品的长度，必须是3的倍数
        int lenth = pppro.length();
        // 如果解密出来的东西并不是3的倍数或者不是纯数字甚至长度为0组成
        if (lenth % 3 != 0 || lenth == 0 || IsNumeric(pppro.toString()) != true) {
            return 0L;
        }
        // 循环解析
        for (int i = 0, j = 0; i <= pppro.length() - 3; i = i + 3, j++) {
            // 假如密钥MD5后的值的长度不够
            if (j == key.length()) {
                j = 0;
            }
            // 每次取出半成品的3位组成数字
            int tmpNumeric = Integer.valueOf(pppro.subSequence(i, i + 3).toString());
            // 计算并转成char型
            char tmp = (char) (tmpNumeric - Integer.valueOf(key.charAt(j)));
            // 判断是不是数字类型,不是的话则返回0
            if (tmp < 48 || tmp > 57) {
                return 0L;
            }
            // 添加进结果
            result.append(tmp);
        }
        // 转换成long类型
        return Long.valueOf(result.toString());
    }

    /**
     * 超时返回true
     *
     * @param timeStamp
     *            客户端返回的参数
     * @return
     */
    public boolean CheckTimeStampIsLegitimate(String timeStamp) {
        // 获取当前时间
        long now = System.currentTimeMillis();
        // 解析时间戳
        Long requestTime = JieMiByMiYue(timeStamp);
        // 判断是否超时,毫秒
        if (now - requestTime <= -defaultTimeOut || now - requestTime >= defaultTimeOut) {
            // 超时
            // 封装json
            return true;
        }
        return false;
    }

    /**
     * 检验字符串是不是纯数字组成的,成功返回true
     *
     * @param str
     *            要检测的内容
     * @return
     */
    private boolean IsNumeric(String str) {
        // 通过正则表达式判断字符串是不是纯数字组成
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }

    // public static void main(String[] args) {
    // System.out.println(JieMiByMiYue(JM("151150102102109103101147104151099153154")));
    // }
}


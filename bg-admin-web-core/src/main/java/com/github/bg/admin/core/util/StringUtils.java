package com.github.bg.admin.core.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author linzf
 * @since 2019-04-25
 * 类描述：字符串工具类
 */
public class StringUtils {

    /**
     * 功能描述：比较两个字段是否相等
     * @param str1
     * @param str2
     * @return
     */
    public static boolean equals(String str1,String str2){
       if(str1==null){
           str1 = "";
       }
       if(str2==null){
           str2 ="";
       }
       return str1.equals(str2);
    }

    /**
     * 合并byte数组
     *
     * @param data1
     * @param data2
     * @return
     */
    public static byte[] copyarray(byte[] data1, byte[] data2) {
        byte[] data3 = new byte[data1.length + data2.length];
        System.arraycopy(data1, 0, data3, 0, data1.length);
        System.arraycopy(data2, 0, data3, data1.length, data2.length);

        return data3;
    }

    /**
     * 将16进制的字符串转换为字节数组,例如有16进制字符串"12345678"<br/>
     * 转换后的结果为：{18, 52 ,86 ,120 };
     *
     * @param hex 需要转换的16进制字符串
     * @return 以字节数组返回转换后的结果
     */
    public static byte[] hexStringToByte(String hex) {
        int len = (hex.length() / 2);
        byte[] result = new byte[len];
        char[] achar = hex.toCharArray();
        for (int i = 0; i < len; i++) {
            int pos = i * 2;
            result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
        }
        return result;
    }

    private static byte toByte(char c) {
        byte b = (byte) "0123456789ABCDEF".indexOf(c);
        if (b < 0) {
            b = (byte) "0123456789abcdef".indexOf(c);
        }
        return b;
    }

    /**
     * 把字节数组转换为十六进制字符串，例如有字节数组<br/>
     * byte [] data = new byte[]{18, 52 ,86 ,120 };转换之后的结果为："12 34 56 78"
     *
     * @param bArray 所要进行转换的数组内容
     * @return 返回转换后的结果，内容用空格隔开
     */
    public static final String bytesToHexString(byte[] bArray) {
        StringBuffer sb = new StringBuffer(bArray.length);
        String sTemp;
        //此处定义的j用于控制每行输出的数据个�?
        int j = 0;
        for (int i = 0; i < bArray.length; i++) {
            sTemp = Integer.toHexString(0xFF & bArray[i]);
            if (sTemp.length() < 2)
                sb.append(0);
            sb.append(sTemp.toUpperCase());
            j++;
        }
        return sb.toString();
    }

    /**
     * BCD码转换成阿拉伯数字，例如有数组byte[] data = new byte[]{0x12,0x34,0x56};<br/>
     * 转换为阿拉伯数字字符串后为"123456"
     *
     * @param bytes 所要转换的十进制BCD字节数组
     * @return BCD码表示的阿拉伯数组内容
     */
    public static String bcd2Str(byte[] bytes) {
        StringBuffer temp = new StringBuffer(bytes.length * 2);

        for (int i = 0; i < bytes.length; i++) {
            temp.append((byte) ((bytes[i] & 0xf0) >>> 4));
            temp.append((byte) (bytes[i] & 0x0f));
        }
        return temp.toString().substring(0, 1).equalsIgnoreCase("0") ? temp.toString().substring(1) : temp.toString();
    }

    /**
     * 10进制字符串转化为BCD压缩码，例如字符串str = "12345678",压缩之后的字节数组内容为{0x12,0x34,0x56,0x78}；
     *
     * @param asc 需要进行压缩的ASCII码表示的字符串
     * @return 以字节数组返回压缩后的内容
     */
    public static byte[] str2Bcd(String asc) {
        int len = asc.length();
        int mod = len % 2;

        if (mod != 0) {
            asc = "0" + asc;
            len = asc.length();
        }

        byte abt[] = new byte[len];
        if (len >= 2) {
            len = len / 2;
        }

        byte bbt[] = new byte[len];
        abt = asc.getBytes();
        int j, k;

        for (int p = 0; p < asc.length() / 2; p++) {
            if ((abt[2 * p] >= '0') && (abt[2 * p] <= '9')) {
                j = abt[2 * p] - '0';
            } else if ((abt[2 * p] >= 'a') && (abt[2 * p] <= 'z')) {
                j = abt[2 * p] - 'a' + 0x0a;
            } else {
                j = abt[2 * p] - 'A' + 0x0a;
            }
            if ((abt[2 * p + 1] >= '0') && (abt[2 * p + 1] <= '9')) {
                k = abt[2 * p + 1] - '0';
            } else if ((abt[2 * p + 1] >= 'a') && (abt[2 * p + 1] <= 'z')) {
                k = abt[2 * p + 1] - 'a' + 0x0a;
            } else {
                k = abt[2 * p + 1] - 'A' + 0x0a;
            }
            int a = (j << 4) + k;
            byte b = (byte) a;
            bbt[p] = b;
        }
        return bbt;
    }

    /**
     * 将整型数据转化为2字节的16进制bytes
     *
     * @param len
     * @return 2字节byte数组
     */
    public static byte[] intToHexBytes(int len) {
        byte[] buf = new byte[2];
        int pos = 0;
        buf[pos] = (byte) ((len & 0xff00) >> 8);
        pos++;
        buf[pos] = (byte) (len & 0xff);
        return buf;
    }

    /**
     * 把2个字节的字节数组转化为int
     *
     * @param lenData 需要进行转换的字节数组
     * @return int
     */
    public static int hexBytesToint(byte lenData[]) {
        if (lenData.length != 2) {
            return -1;
        }
        int outLen = 0;
        for (int i = 0; i < 2; i++) {
            outLen += ((int) lenData[i] & 0xFF) << 8 * (1 - i);
        }
        return outLen;
    }

    /**
     * 将obj转换为float
     *
     * @param obj
     * @return
     */
    public static float getObjFloat(Object obj) {
        return Float.parseFloat(obj == null ? "0" : obj + "");
    }

    /**
     * 将obj转换为BigDemical
     *
     * @param obj
     * @return
     */
    public static BigDecimal getObjBigDecimal(Object obj) {
        BigDecimal num;
        if (obj != null) {
            num = new BigDecimal(obj + "");
        } else {
            return null;
        }
        return num;
    }

    /**
     * 字符串左补空格操作
     *
     * @param str 待补空格的字符串
     * @param len 补空格之后的字符串长度
     * @return 补空格之后的字符串内容
     * @throws UnsupportedEncodingException
     */
    public static String addSpaceLeft(String str, int len) throws UnsupportedEncodingException {
        StringBuffer s = new StringBuffer();
        s.append(str);
        while (s.toString().getBytes("GBK").length < len) {
            s.insert(0, " ");
        }
        return s.toString();
    }

    /**
     * 字符串右补空格操作
     *
     * @param str 待补空格的字符串
     * @param len 补空格后字符串的长度
     * @return 补空格后的字符串
     */
    public static String addSpaceRight(String str, int len) throws UnsupportedEncodingException {
        StringBuffer s = new StringBuffer();
        if (str == null) {
            str = "";
        }
        s.append(str);
        while (s.toString().getBytes("GBK").length < len) {
            s.append(" ");
        }
        return s.toString();
    }

    /**
     * 去除字符串两端的空格，如果为null，则返回空字符串
     *
     * @param str 要去除空格的字符串
     * @return 去掉两端空格的字符串
     */
    public static String trim(String str) {
        return (str == null ? "" : str.trim());
    }

    /**
     * 判断是否为空字符串，空返回true,非空返回false
     *
     * @param str 判断是否为空字符串
     * @return 空返回true, 非空返回false
     */
    public static boolean isEmpty(Object str) {
        return ((str == null || str.toString().trim().equals("")) ? true : false);
    }

    /**
     * 判断是否为空字符串，空返回false,非空返回true
     *
     * @param str 判断是否为空字符串
     * @return 空返回false, 非空返回true
     */
    public static boolean isNotEmpty(String str) {
        return ((str == null || "".equals(str.trim())) ? false : true);
    }

    /**
     * 去掉输入字符串尾部的空格和制表符的字符串
     *
     * @param s 输入字符串
     * @return 返回去掉串尾部的空格和制表符的字符串
     */
    public static String trimRight(String s) {
        if (s == null)
            return null;
        char[] ch = s.toCharArray();
        int i = ch.length - 1;
        while (0 <= i && (32 == ch[i] || 9 == ch[i]))
            i--;
        return s.substring(0, i + 1);
    }

    /**
     * 去掉输入字符串头部的空格和制表符的字符串
     *
     * @param s 输入字符串
     * @return 返回去掉串头部的空格和制表符的字符串
     */
    public static String trimLeft(String s) {
        if (s == null)
            return null;
        char[] ch = s.toCharArray();
        int i = 0;
        while (i <= ch.length - 1 && (32 == ch[i] || 9 == ch[i]))
            i++;
        return s.substring(i);
    }


    /**
     * 字符串左补0操作
     *
     * @param str 待补空格的字符串
     * @param len 补空格之后的字符串长度
     * @return 补空格之后的字符串内容
     * @throws UnsupportedEncodingException
     */
    public static String addZeroLeft(String str, int len) {
        StringBuffer s = new StringBuffer();
        s.append(str);
        try {
            while (s.toString().getBytes("GBK").length < len) {
                s.insert(0, "0");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return s.toString();
    }

    /**
     * 将obj 转换为String
     *
     * @param obj
     * @return
     */
    public static String getObjStr(Object obj) {
        return obj == null ? "" : obj.toString().trim();
    }

    /**
     * 将obj转换为Boolean
     *
     * @param obj
     * @return
     */
    public static boolean getobjBoolean(Object obj) {
        return obj == null ? false : (boolean) obj;
    }

    /**
     * 将obj转换为double
     *
     * @param obj
     * @return
     */
    public static double getObjDouble(Object obj) {
        return Double.parseDouble(obj == null ? "0" : obj + "");
    }

    /**
     * 将obj转换为int
     *
     * @param obj
     * @return
     */
    public static int getObjInt(Object obj) {
        return Integer.parseInt(obj == null ? "0" : obj + "");
    }

    /**
     * oracle.sql.Clob类型转换成String类型
     *
     * @param clob
     * @return
     * @throws SQLException
     * @throws IOException
     */
    public static String clobToString(Clob clob) throws SQLException, IOException {
        String reString = "";
        if (clob != null) {
            Reader is = clob.getCharacterStream();// 得到流
            BufferedReader br = new BufferedReader(is);
            String s = br.readLine();
            StringBuffer sb = new StringBuffer();
            while (s != null) {
                sb.append(s);
                s = br.readLine();
            }
            reString = sb.toString();
        }
        return reString;
    }

    /**
     * 根据指定字符分隔字符串，
     *
     * @param str
     * @param split
     * @param idx
     * @return 并返回指定下标的数据, 下标超出长度则返回第0个
     */
    public static String getSplitData(String str, String split, int idx) {
        if (StringUtils.isEmpty(str)) {
            return "";
        }
        String resStr = "";
        String[] strList = str.split("\\" + split);
        if (idx < 0) {
            return resStr;
        }
        if (strList.length > idx) {
            return strList[idx];
        } else {
            return strList[0];
        }
    }

    /**
     * 字符串转16进制字符串
     *
     * @param s
     * @return
     */
    public static String str2Hex(String s) {
        if (s == null || "".equals(s)) {
            return null;
        }
        try {
            StringBuffer res = new StringBuffer();
            byte[] byteArray = s.getBytes("gbk");
            for (byte b : byteArray) {
                int v = b & 0xFF;
                String hv = Integer.toHexString(v);
                if (hv.length() < 2) {
                    res.append(0);
                }
                res.append(hv);
            }
            return res.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Convert byte[] to hex string.
     * 这里我们可以将byte转换成int，然后利用Integer.toHexString(int)来转换成16进制字符串。
     *
     * @param src byte[] data
     * @return hex string
     */

    public static String bytesToHexAllString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return "";
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    /**
     * Convert hex string to byte[]
     *
     * @param hexString the hex string
     * @return byte[]
     */
    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    /**
     * Convert char to byte
     *
     * @param c char
     * @return byte
     */
    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }


    /**
     * 根据传入的长度数据将其转换为Num个字节的参数偏移地址格式
     *
     * @param len 数据长度
     * @return
     */

    public static String toParaLen(int len) {
        String a = Integer.toHexString(len);
        //补0后进行高低位调换
        a = StringUtils.addZeroLeft(a, 8);
        a = a.substring(a.length() - 2, a.length()) + a.substring(a.length() - 4, a.length() - 2) + a.substring(a.length() - 6, a.length() - 4) + a.substring(0, 2);
        return a;
    }

    /**
     * 转换长度，将中文字符转换*2
     *
     * @param s
     * @return
     */
    public static int getWordCount(String s) {
        int length = 0;
        for (int i = 0; i < s.length(); i++) {
            int ascii = Character.codePointAt(s, i);
            if (ascii >= 0 && ascii <= 255)
                length++;
            else
                length += 2;
        }
        return length;
    }

    /**
     * 把所有元素按照“key=value”的模式用“&”字符拼接成字符串,并按照参数名ASCII字典序排序
     * @param params 需要参与字符拼接的参数组
     * @return
     */
    public static String createLinkString(Map<String, Object> params) {
        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);//排序
        String prestr = "";
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            if (null == params.get(key) || params.get(key).toString().isEmpty()){
                continue;
            }
            prestr = prestr + "&" + key + "=" + params.get(key).toString();
        }
        return prestr.substring(1);
    }

}

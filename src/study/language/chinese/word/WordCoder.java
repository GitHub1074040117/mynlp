package study.language.chinese.word;

import java.io.UnsupportedEncodingException;

public class WordCoder {

    public static void showCode(String word) {
        try {
            byte[] b = word.getBytes("GB2312");
            System.out.println(bytesToHex(b));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /*public static byte[] codeGB2312(char word) {
        try {
            String str = String.valueOf(word);
            return str.getBytes("GB2312");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }*/

    // 转化为GB2312
    public static String codeGB2312(char word) {
        try {
            String str = String.valueOf(word);
            return bytesToHex(str.getBytes("GB2312"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String codeGB2312(String word) {
        try {
            String str = String.valueOf(word);
            return bytesToHex(str.getBytes("GB2312"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    //将byte数组转成16进制字符串
    private static String bytesToHex(byte[] bytes) {
        char[] HEX_CHAR = {'0', '1', '2', '3', '4', '5',
                '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        char[] buf = new char[bytes.length * 2];
        int a;
        int index = 0;
        for(byte b : bytes) {
            if(b < 0) {
                a = 256 + b;
            } else {
                a = b;
            }
            buf[index++] = HEX_CHAR[a / 16];
            buf[index++] = HEX_CHAR[a % 16];
        }
        return new String(buf);
    }


    // 将gb2312编码转换成汉字
    public static String toGB2312(String code) throws Exception {
        byte[] bytes = new byte[code.length() / 2];
        for(int i = 0; i < bytes.length; i ++){
            byte high = Byte.parseByte(code.substring(i * 2, i * 2 + 1), 16);
            byte low = Byte.parseByte(code.substring(i * 2 + 1, i * 2 + 2), 16);
            bytes[i] = (byte) (high << 4 | low);
        }
        return new String(bytes, "gbk");
    }
}

package cn.lee.cplibrary.util;

import android.text.TextUtils;
import android.util.Log;
import android.util.SparseIntArray;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @TODO:[字符处理工具类] 1、基本类型和包装类型的相互转化 2、字符编码相关操作（html5编码等）
 * 3、字符串是否是中文、2个字符串是否相等、字符是否是汉字 4、数字相关操作（格式化、转换等）5、url的相关操作
 * 7、有关正则表达式的验证 8、进制相关转化
 * @author: ChrisLee at 2016-7-18
 */
public class StringUtil {
    public static final String TAG = "StringUtil:";

    public static final String HTTP = "http://";
    public static final String HTTPS = "https://";
    public static final String WWW_LOW = "www.";
    public static final String WWW_UPPER = "WWW.";
    public static final String FAVICON_URL_SUFFIX = "/favicon.ico";

    static SparseIntArray CHAR_MAP = new SparseIntArray(9);

    static {
        CHAR_MAP.put('一', 1);
        CHAR_MAP.put('二', 2);
        CHAR_MAP.put('三', 3);
        CHAR_MAP.put('四', 4);
        CHAR_MAP.put('五', 5);
        CHAR_MAP.put('六', 6);
        CHAR_MAP.put('七', 7);
        CHAR_MAP.put('八', 8);
        CHAR_MAP.put('九', 9);
    }

    /**
     * 从字符串转换成整形
     *
     * @param str 待转换字符串
     * @return
     */
    public static int String2Int(String str) {
        try {
            int value = Integer.valueOf(str);
            return value;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 判定输入字符是否是汉字
     *
     * @param c
     * @return
     */
    public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true;
        }
        return false;
    }

    /**
     * 检测String是否全是中文
     *
     * @param name
     * @return
     */
    public static boolean checkNameChese(String name) {
        boolean res = true;
        char[] cTemp = name.toCharArray();
        for (int i = 0; i < name.length(); i++) {
            if (!isChinese(cTemp[i])) {
                res = false;
                break;
            }
        }
        return res;
    }

    /**
     * @param list
     * @return
     * @function: 将List<Map<String,String>>集合转为[{"product_id":"1","count":"3"},{
     * "product_id" :"1","count":"3"}] 字符串
     * @author: ChrisLee at 2016-3-2 下午5:15:18
     */
    public static String getJsonStringByList(List<Map<String, String>> list) {
        Log.i("lee", TAG + "===list=" + list);
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < list.size(); i++) {
            Map<String, String> map = list.get(i);
            sb.append("{");
            int k = 0;
            for (Entry<String, String> entry : map.entrySet()) {
                sb.append("\"" + entry.getKey() + "\"" + ":" + "\""
                        + entry.getValue() + "\"");
                k++;
                if (k != map.size()) {
                    sb.append(",");
                }
            }
            if (i == list.size() - 1) {
                sb.append("}");
            } else {
                sb.append("},");
            }
        }
        sb.append("]");
        Log.i("lee", "====sb=" + sb.toString());

        return sb.toString();
    }

    private static int getIntFromMap(char ch) {
        Integer result = CHAR_MAP.get(ch);
        return result != null ? result : 0;
    }

    // -----------------------------------------------------------数字的相关操作---------------------------------------------------------------

    /**
     * 将0~9999以内的小写汉字转化为数字 例如：三千四百五十六转化为3456
     *
     * @param str
     * @return
     */
    public static int parseChineseNumber(String str) {
        if (str == null || str.length() <= 0) {
            return -1;
        }
        int result = 0;
        int index = -1;
        index = str.indexOf('千');
        if (index > 0) {
            result += getIntFromMap(str.charAt(index - 1)) * 1000;
        }
        index = str.indexOf('百');
        if (index > 0) {
            result += getIntFromMap(str.charAt(index - 1)) * 100;
        }
        index = str.indexOf('十');
        if (index > 0) {
            result += getIntFromMap(str.charAt(index - 1)) * 10;
        } else if (index == 0) {
            result += 10;
        }
        index = str.length();
        if (index > 0) {
            result += getIntFromMap(str.charAt(index - 1));
        }
        return result;
    }

    /**
     * Funtions : 格式化数字，eg：0.00 或者1.00 或1.21
     *
     * @param money 被格式化的金额
     * @return String
     * <p>
     * Author:ChrisLee Date: 2015-12-25
     */
    public static String formatMoney(Float money) {
        String formatMoney = "";
        if (!TextUtils.isEmpty(money + "")) {
            DecimalFormat decimalFormat = new DecimalFormat(".00");// 构造方法的字符格式这里如果小数不足2位,会以0补足.
            if (decimalFormat.format(money).substring(0, 1).equals(".")) {
                formatMoney = "0" + decimalFormat.format(money);
            } else {
                formatMoney = "" + decimalFormat.format(money);
            }
        }
        return formatMoney;
    }

    /**
     * Funtions : 格式化数字，eg：0.00 或者1.00 或1.21 或者.00 .09
     *
     * @param money 被格式化的金额
     * @return String
     * <p>
     * Author:ChrisLee Date: 2015-12-25
     */
    public static String formatMoneyWithoutZero(Float money) {
        String formatMoney = "";
        if ((money + "") != null && !(money + "").equals("")) {
            DecimalFormat decimalFormat = new DecimalFormat(".00");// 构造方法的字符格式这里如果小数不足2位,会以0补足.
            if (decimalFormat.format(money).substring(0, 1).equals(".")) {
                formatMoney = "0" + decimalFormat.format(money);
            } else {
                formatMoney = "" + decimalFormat.format(money);
            }
        }
        if ("0.00".equals(formatMoney)) {
            formatMoney = "";
        }
        return formatMoney;
    }

    /**
     * function:???
     *
     * @param source
     * @param key
     * @return
     * @author: ChrisLee at 2016-7-18
     */
    public static String OR(byte[] source, String key) {
        return OR(source, key, null);
    }

    /**
     * function:???
     *
     * @param source
     * @param key
     * @return
     * @author: ChrisLee at 2016-7-18
     */
    public static byte[] OR2Byte(byte[] source, String key) {
        if (source == null || source.length < 1 || key == null
                || key.length() < 1) {
            return null;
        }
        final byte[] sourceBuff = source;
        byte[] keyBuff = key.getBytes();
        int keyLength = keyBuff.length;
        int keyIndex = 0;
        Integer byteResult = 0;

        byte[] result = new byte[sourceBuff.length];

        for (int i = 0; i < sourceBuff.length; i++) {
            keyIndex = i % keyLength;
            byteResult = sourceBuff[i] ^ keyBuff[keyIndex];
            result[i] = byteResult.byteValue();
        }

        return result;
    }

    /**
     * function:???
     *
     * @param source
     * @param key
     * @param charsetName
     * @return
     * @author: ChrisLee at 2016-7-18
     */
    public static String OR(byte[] source, String key, String charsetName) {
        byte[] result = OR2Byte(source, key);
        if (result == null) {
            return null;
        }

        String resultString = null;
        if (charsetName != null && !charsetName.equals("")) {
            try {
                resultString = new String(result, charsetName);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else {
            resultString = new String(result);
        }
        return resultString;
    }

    // ------------------------------------------------------url相关--------------------------------------------------------------------------

    /**
     * function:判断url是否是一个正确的url地址
     *
     * @param url
     * @return
     * @author: ChrisLee at 2016-7-18
     */
    public static final boolean isHttpUrl(String url) {
        String regEx = "^(https|http://){0,1}([a-zA-Z0-9]{1,}[a-zA-Z0-9\\-]{0,}\\.){0,4}([a-zA-Z0-9]{1,}[a-zA-Z0-9\\-]{0,}\\.[a-zA-Z0-9]{1,})/{0,1}$";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(url);
        return m.find();
    }

    /**
     * function:如果url包含："www.",则去掉"www."
     *
     * @param url
     * @return
     * @author: ChrisLee at 2016-7-18
     */
    public static final String removeWWWHead(String url) {
        if (url == null) {
            return null;
        }

        String temp = url.toLowerCase();
        if (temp.startsWith(WWW_LOW)) {
            url = url.substring(WWW_LOW.length());
        }

        return url;
    }

    /**
     * function:如果url包含："http://"或者"https://",则去掉"http://"或者"https://"，返回剩余的部分
     *
     * @param url
     * @return
     * @author: ChrisLee at 2016-7-18
     */
    public static final String removeHttpHead(String url) {
        if (url == null) {
            return null;
        }

        if (url.startsWith(HTTP)) {
            url = url.substring(HTTP.length());
        } else if (url.startsWith(HTTPS)) {
            url = url.substring(HTTPS.length());
        }

        return url;
    }

    /**
     * function:获取url的域名,并且给域名添加/favicon.ico这个后缀
     *
     * @param url :包含头http://、https://
     * @return
     * @author: ChrisLee at 2016-7-18
     */
    public static final String getFaviconUrl(String url) {
        String domainUrl = getDomainName(url, true);
        if (domainUrl != null) {
            return domainUrl + FAVICON_URL_SUFFIX;
        }
        return null;
    }

    /**
     * function:获取url的域名
     *
     * @param url ：不包含头http://、https://
     * @return
     * @author: ChrisLee at 2016-7-18
     */
    public static final String getDomainName(String url) {
        return getDomainName(url, false);
    }

    /**
     * function:获取url的域名
     *
     * @param url
     * @param hasHttpHead ：url是否包含了http://或者https://
     * @return
     * @author: ChrisLee at 2016-7-18
     */
    public static final String getDomainName(String url, boolean hasHttpHead) {
        if (url == null) {
            return null;
        }

        String result = url;
        int index;
        if (hasHttpHead) {
            if (url.indexOf(HTTP) == 0) {
                index = url.indexOf("/", HTTP.length());
            } else if (url.indexOf(HTTPS) == 0) {
                index = url.indexOf("/", HTTPS.length());
            } else {
                index = url.indexOf("/");
            }
        } else {
            result = removeHttpHead(url);
            index = result.indexOf("/");
        }
        if (index > -1) {
            result = result.substring(0, index);
        }
        return result;
    }

    /**
     * function:判断url是否是web地址
     *
     * @param url
     * @return
     * @author: ChrisLee at 2016-7-18
     */
    public static boolean isWebUrl(String url) {
        if (url == null || url.length() < 1) {
            return false;
        }

        url = url.toLowerCase();
        return url.startsWith("http://") || url.startsWith("https://")
                || url.startsWith("ftp://");
    }


    /**
     * 是否为邮件地址
     */
    public static boolean isVaildEmail(String email) {
        if (email == null || email.trim().length() == 0) {
            return false;
        }
        String emailPattern = "[a-zA-Z0-9_-|\\.]+@[a-zA-Z0-9_-]+.[a-zA-Z0-9_.-]+";
        boolean result = Pattern.matches(emailPattern, email);
        return result;
    }

    public static int getFrontSynIndex(String text, int currentIndex) {
        if (text != null) {
            if (currentIndex >= 0 && currentIndex < text.length()) {
                char[] textArray = text.toCharArray();
                for (int i = currentIndex; i >= 0; i--) {
                    if (textArray[i] == '.') {
                        return i + 1;
                    }
                }
            }
        }
        return 0;
    }

    public static int getBehindSynIndex(String text, int currentIndex) {
        if (text != null) {
            if (currentIndex >= 0 && currentIndex < text.length()) {
                char[] textArray = text.toCharArray();
                for (int i = currentIndex; i < text.length(); i++) {
                    if (textArray[i] == '.') {
                        return i;
                    }
                }
                return text.length();
            }
        }
        return 0;
    }

    /**
     * int ip地址 转换成 String 地址
     */
    public static String intToIp(int i) {
        StringBuilder sb = new StringBuilder();
        sb.append(i & 0xFF);
        sb.append(".");
        sb.append((i >> 8) & 0xFF);
        sb.append(".");
        sb.append((i >> 16) & 0xFF);
        sb.append(".");
        sb.append((i >> 24) & 0xFF);
        return sb.toString();
    }

    public static final String[] filterStr = new String[]{".com", ".cn",
            ".mobi", ".co", ".net", ".so", ".org", ".gov", ".tel", ".tv",
            ".biz", ".cc", ".hk", ".name", ".info", ".asia", ".me", ".us"};

    private static String getFilterInfoPattern() {
        StringBuilder sb = new StringBuilder();
        sb.append("\\[.*?(");
        int size = filterStr.length;
        for (int i = 0; i < size; i++) {
            sb.append(filterStr[i]);
            if (i != size - 1) {
                sb.append("|");
            }
        }
        sb.append(")\\]");
        return sb.toString();
    }

    private static final String FILTER_INFO_PATTERN = getFilterInfoPattern();

    public static String filterInfo(String str) {
        if (str == null) {
            return null;
        }
        // \[.*?(.com|.cn|.mobi|.co|.net|.so|.org|.gov|.tel|.tv|.biz|.cc|.hk|.name|.info|.asia|.me|.us)\]
        // matcher.matches():，匹配规则
        Pattern pattern = Pattern.compile(FILTER_INFO_PATTERN);
        // 进行匹配
        Matcher matcher = pattern.matcher(str);
        return matcher.replaceAll("");
    }

    /**
     * function:格式化字符串：去掉source字符串中a~z，A~Z,0~9以外的特殊字符
     *
     * @param source
     * @return
     * @author: ChrisLee at 2016-7-19
     */
    public static String formatChar(String source) {
        if (source != null) {
            Pattern p = Pattern.compile("[^a-zA-Z0-9]");
            return p.matcher(source).replaceAll("");
        }
        return null;
    }

    public static boolean isHexWepKey(String wepKey) {
        final int len = wepKey.length();
        // WEP-40, WEP-104, and some vendors using 256-bit WEP (WEP-232?)
        if (len != 10 && len != 26 && len != 58) {
            return false;
        }
        return isHex(wepKey);
    }

    public static boolean isHex(String key) {
        for (int i = key.length() - 1; i >= 0; i--) {
            final char c = key.charAt(i);
            if (!(c >= '0' && c <= '9' || c >= 'A' && c <= 'F' || c >= 'a'
                    && c <= 'f')) {
                return false;
            }
        }
        return true;
    }

    /**
     * function:将字节数组转换成16进制的数值
     *
     * @param buf
     * @return
     * @author: ChrisLee at 2016-7-19
     */
    public static String toHex(byte[] buf) {
        if (buf == null) {
            return "";
        }
        StringBuffer result = new StringBuffer(2 * buf.length);
        for (int i = 0; i < buf.length; i++) {
            appendHex(result, buf[i]);
        }
        return result.toString();
    }

    private final static String HEX = "0123456789ABCDEF";

    private static void appendHex(StringBuffer sb, byte b) {
        sb.append(HEX.charAt((b >> 4) & 0x0f)).append(HEX.charAt(b & 0x0f));
    }

    /**
     * function:16进制String数值转换成字节数组
     *
     * @param hexString
     * @return
     * @author: ChrisLee at 2016-7-19
     */
    public static byte[] toByte(String hexString) {
        int len = hexString.length() / 2;
        byte[] result = new byte[len];
        for (int i = 0; i < len; i++) {
            result[i] = Integer.valueOf(hexString.substring(2 * i, 2 * i + 2),
                    16).byteValue();
        }
        return result;
    }

    /**
     * function:将字符串str用双引号 引用起来，eg：str=ChrisLee,则返回"ChrisLee"
     *
     * @param str
     * @return
     * @author: ChrisLee at 2016-7-19
     */
    public static String toQuotedString(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        final int lastPos = str.length() - 1;
        if (lastPos < 0 || (str.charAt(0) == '"' && str.charAt(lastPos) == '"')) {
            return str;
        }
        return "\"" + str + "\"";
    }

    /**
     * function:将url中空格替换成 %20
     *
     * @param url
     * @return
     * @author: ChrisLee at 2016-7-19
     */
    public static String urlEncode(String url) {
        StringBuffer urlB = new StringBuffer();
        // 以 /作为字符分割标记，且/也作为一个标记
        StringTokenizer st = new StringTokenizer(url, "/ ", true);
        while (st.hasMoreTokens()) {
            String tok = st.nextToken();
            if (tok.equals("/")) {
                urlB.append("/");
            } else if (tok.equals(" ")) {
                urlB.append("%20");
            } else {
                try {
                    urlB.append(URLEncoder.encode(tok, "UTF-8"));
                } catch (UnsupportedEncodingException uee) {

                }
            }
        }
        Log.d(TAG, "urlEncode urlB:" + urlB.toString());
        return urlB.toString();
    }

    /**
     * 将若干条字符串拼接成一条,多条之间的分隔符为分隔符连接
     *
     * @param list
     * @return
     */
    public static List<String> composeStringList(List<String> list,
                                                 int maxLength, String split) {
        if (list == null || list.size() <= 1 || split == null || maxLength <= 0) {
            return list;
        }

        ArrayList<String> comList = new ArrayList<String>();
        StringBuilder comString = new StringBuilder(list.get(0));

        for (int i = 1; i < list.size(); i++) {
            if ((comString.length() + list.get(i).length()) >= maxLength) {
                comList.add(comString.toString());
                comString = new StringBuilder(list.get(i));
            } else {
                comString.append(split).append(list.get(i));
            }
        }

        comList.add(comString.toString());
        return comList;
    }

    /**
     * [获取指定长度的字符串]<br/>
     * 以半角长度为准
     *
     * @param str
     * @param len
     * @param symbol
     * @return
     */
    public static String getLimitLengthString(String str, int len, String symbol) {
        try {
            int counterOfDoubleByte = 0;
            byte[] b = str.getBytes("GBK");
            if (b.length <= len) {
                return str;
            }
            for (int i = 0; i < len; i++) {
                if (b[i] < 0) {
                    counterOfDoubleByte++;
                }
            }

            if (counterOfDoubleByte % 2 == 0) {
                return new String(b, 0, len, "GBK") + symbol;
            } else {
                return new String(b, 0, len - 1, "GBK") + symbol;
            }
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * function:将s字符串 转换成 html中编码格式下的字符串
     *
     * @param s
     * @return
     * @author: ChrisLee at 2016-7-19
     */
    public static String htmlEncode(String s) {
        StringBuilder sb = new StringBuilder();
        char c;
        for (int i = 0; i < s.length(); i++) {
            c = s.charAt(i);
            switch (c) {
                case '<':
                    sb.append("&lt;"); //$NON-NLS-1$
                    break;
                case '>':
                    sb.append("&gt;"); //$NON-NLS-1$
                    break;
                case '&':
                    sb.append("&amp;"); //$NON-NLS-1$
                    break;
                case '\'':
                    sb.append("&apos;"); //$NON-NLS-1$
                    break;
                case '"':
                    sb.append("&quot;"); //$NON-NLS-1$
                    break;
                default:
                    sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * function:打印程序运行到当前的线程栈的信息（类名、当前栈中拥有的类的数量等）
     *
     * @param tag
     * @author: ChrisLee at 2016-7-19
     */
    public static void printStack(String tag) {
        if (!LogUtil.isDebug()) {
            return;
        }
        StackTraceElement[] stackTraceElements = Thread.currentThread()
                .getStackTrace();
        for (int i = 0; i < stackTraceElements.length; i++) {
            StackTraceElement element = stackTraceElements[i];
            String name = element.getMethodName();
            // LogUtils.i(LogUtils.TAG,tag+ element.getClassName() + "." + name
            // + "() "
            // + element.getLineNumber());
        }
    }

    /**
     * function:比较：str1和str2是否相同（若都为null，则也认为是相同的）
     *
     * @param str1
     * @param str2
     * @return
     * @author: ChrisLee at 2016-7-19
     */
    public static boolean equalsString(String str1, String str2) {
        if (str1 != null) {
            return str1.equals(str2);
        } else {
            return str2 == null;
        }
    }

    /**
     * 比较类似于3.5.0这样的版本号字符串的大小
     *
     * @param str1
     * @param str2
     * @return str1 > str2 返回 正数；str1 = str2 返回 0；str1 < str2 返回 负数；
     */
    public static int compareVerString(String str1, String str2) {
        if (str1 == null || str2 == null) {
            return 0;
        }

        String[] cons1 = str1.split("\\.");
        String[] cons2 = str2.split("\\.");

        int i = 0;
        try {
            while (i < cons1.length && i < cons2.length) {
                int int1 = Integer.parseInt(cons1[i]);
                int int2 = Integer.parseInt(cons2[i]);
                int res = int1 - int2;
                if (res == 0) {
                    i++;
                    continue;
                } else {
                    return res;
                }
            }

            return cons1.length - cons2.length;
        } catch (Exception e) {
            return 0;
        }
    }

    public static String getUUID() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }


    /**
     * 按照数字 、字母、中文顺序排序
     *
     * @param str
     */
    public static void sort(String... str) {
        Collections.sort(Arrays.asList(str), new StringSorComparator());
//        Collections.sort(Arrays.asList(str));//按照数字 、字母、中文顺序排序
    }

    /**
     * 隐藏时手机号中间四位，用*代替
     *
     * @param phoneNumber 原手机号
     * @return 隐藏后的手机号
     */
    public static String hideMiddleFourNumber(String phoneNumber) {
        if (ObjectUtils.isEmpty(phoneNumber)) {
            return "";
        }
        phoneNumber = phoneNumber.substring(0, 3) + "****" + phoneNumber.substring(7, phoneNumber.length());
        return phoneNumber;
    }

    /**
     * 保留银行卡末尾4位数字，其余用*代替
     */
    public static String getHideBankCardNum(String bankCardNum) {
        if (bankCardNum == null)
            return "";
        int length = bankCardNum.length();
        if (length > 4) {
            String endNum = bankCardNum.substring(length - 4, length);
            bankCardNum = "****  ****  ****  " + endNum;
        }
        return bankCardNum;
    }

    /**
     * 判断strAll中包含strMin的个数
     *
     * @param strAll
     * @param strMin
     * @return counter
     */
    public static int getCountStr(String strAll, String strMin) {
        int counter = 0;
        if (strAll.indexOf(strMin) == -1) {
            return 0;
        } else if (strAll.indexOf(strMin) != -1) {
            counter++;
            getCountStr(strAll.substring(strAll.indexOf(strMin) +
                    strMin.length()), strMin);
            return counter;
        }
        return 0;
    }


}

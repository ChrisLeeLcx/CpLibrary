package cn.lee.cplibrary.util;

import android.text.TextUtils;

import java.text.DecimalFormat;

/**
 * 数据的处理、转换和判断工具类。
 * 转换数据做了异常处理
 */
public class ObjectUtils {

	public static boolean isNull(Object object) {
		try {
			if (null == object) {
				return true;
			}
			if (object instanceof String) {
				if (object.equals("")) {
					return true;
				}
			}
		} catch (Exception e) {
			return false;
		}
		return false;

	}

	/**
	 * @param
	 * @return str若为null、""、"null" 则返回true
	 */
	public static boolean isEmpty(String str) {
		if (TextUtils.isEmpty(str) || "null".equalsIgnoreCase(str.trim())) {
			return true;
		}
		return false;
	}

	/**
	 * 参数中只要有空值，就返回true
	 */
	public static boolean hasNull(Object... o) {
		Object[] objects = o;
		if (isNull(objects)) {
			return true;
		}
		for (Object object : objects) {
			if (isNull(object)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 参数中只要有非空值，就返回true
	 */
	public static boolean hasNotNull(Object... o) {
		Object[] objects = o;
		if (isNull(objects)) {
			return false;
		}
		for (Object object : objects) {
			if (!isNull(object)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * 参数中只要有空字符串（null 或 ""），就返回true
	 */
	public static boolean hasEmpty(String... str) {
		String[] strs = str;
		if (isNull(strs)) {
			return true;
		}
		for (String s : strs) {
			if (TextUtils.isEmpty(s)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 参数中只要有 非 空字符串（null 或 ""），就返回true
	 */
	public static boolean hasNotEmpty(String... str) {
		String[] strs = str;
		if (isNull(strs)) {
			return false;
		}
		for (String s : strs) {
			if (!TextUtils.isEmpty(s)) {
				return true;
			}
		}
		return false;
	}

	public static String formatStr(String s) {
		if (isNull(s) || "null".equals(s)) {
			return "";
		}
		return s;
	}

	public static int string2Int(String str) {
		if (isNull(str)) {
			return 0;
		}
		try {
			return Integer.parseInt(str);
		} catch (Exception e) {
			return 0;
		}
	}

	public static long string2Long(String str) {
		if (isNull(str)) {
			return 0L;
		}
		try {
			return Long.parseLong(str);
		} catch (Exception e) {
			return 0L;
		}
	}

	public static float string2Float(String str) {
		if (isNull(str)) {
			return 0f;
		}
		try {
			return Float.parseFloat(str);
		} catch (Exception e) {
			return 0f;
		}
	}

	public static double string2Double(String str) {
		if (isNull(str)) {
			return 0;
		}
		try {
			return Double.parseDouble(str);
		} catch (Exception e) {
			return 0;
		}
	}

	public static boolean string2Boolean(String str) {
		if (isNull(str)) {
			return false;
		}
		try {
			return Boolean.parseBoolean(str);
		} catch (Exception e) {
			return false;
		}

	}

	public static byte string2Byte(String str) {
		if (isNull(str)) {
			return 0;
		}
		try {
			return Byte.parseByte(str);
		} catch (Exception e) {
			return 0;
		}

	}

	public static String object2String(Object object) {
		return object == null ? "" : object.toString();
	}

	/**
	 * 格式化double数据，保留length位小数
	 *
	 * @param d
	 * @param length 精确小数位数
	 * @return
	 */

	public static double decimal(double d, int length) {
		StringBuffer sb = new StringBuffer();
		sb.append("0.");
		if (length > 0) {
			for (int i = 0; i < length; i++) {
				sb.append("0");
			}
			DecimalFormat decimalFormat = new DecimalFormat(sb.toString());
			return string2Double(decimalFormat.format(d));
		}
		return 0;
	}

	public static String decimal2String(double d, int length) {
		StringBuffer sb = new StringBuffer();
		sb.append("0.");
		if (length > 0) {
			for (int i = 0; i < length; i++) {
				sb.append("0");
			}
			DecimalFormat decimalFormat = new DecimalFormat(sb.toString());
			return decimalFormat.format(d);
		}
		return "";
	}

	private static final String[] digits = new String[]{"零", "一", "二", "三", "四", "五", "六", "七", "八", "九"};
	private static final String[] radices = new String[]{"", "十", "百", "千"};
	private static final String[] bigRadices = new String[]{"", "万", "亿", "万"};

	/**
	 * 将阿拉伯数字转化成汉字描述
	 * eg：digit=3600000 返回 三百六十万
	 */
	public static String digitConvert(int digit) {

		String currencyDigits = String.valueOf(digit);

		String integral = null;
		String outputCharacters = null;

		String d = null;
		int zeroCount = 0, p = 0, quotient = 0, modulus = 0;

		currencyDigits = currencyDigits.replace("/,/g", "");
		currencyDigits = currencyDigits.replace("/^0+/", "");
		String[] parts = currencyDigits.split("\\.");
		if (parts.length > 1) {
			integral = parts[0];

		} else {
			integral = parts[0];
		}

		outputCharacters = "";
		if (Double.parseDouble(integral) > 0) {

			zeroCount = 0;

			for (int i = 0; i < integral.length(); i++) {

				p = integral.length() - i - 1;
				d = integral.substring(i, i + 1);

				quotient = p / 4;
				modulus = p % 4;
				if (d.equals("0")) {
					zeroCount++;
				} else {
					if (zeroCount > 0) {
						outputCharacters += digits[0];
					}
					zeroCount = 0;
					outputCharacters += digits[Integer.parseInt(d)] + radices[modulus];
				}
				if (modulus == 0 && zeroCount < 4) {
					outputCharacters += bigRadices[quotient];
				}
			}
		}
		if (outputCharacters.length() > 1 && outputCharacters.startsWith("һ") && outputCharacters.indexOf("ʮ") == 1) {
			outputCharacters = outputCharacters.substring(1, outputCharacters.length());
		}

		return outputCharacters;
	}


}

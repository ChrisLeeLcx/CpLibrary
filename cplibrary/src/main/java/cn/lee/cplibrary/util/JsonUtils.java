package cn.lee.cplibrary.util;


import org.json.JSONArray;
import org.json.JSONObject;

public class JsonUtils {
	private static String TAG = "JsonUtils";

	public static JSONObject formatterJsonObject(String content) {
		try {
			if (!ObjectUtils.isNull(content)) {
				JSONObject jsonObject = new JSONObject(content);
				return jsonObject;
			}

		} catch (Exception e) {
			return null;
		}
		return null;
	}

	public static JSONArray formatterJsonArray(String content) {
		try {
			if (!ObjectUtils.isNull(content)) {
				JSONArray jsonArray = new JSONArray(content);
				return jsonArray;
			}

		} catch (Exception e) {
			return null;
		}
		return null;
	}

	public static boolean isJson(String content) {

		return (formatterJsonObject(content) != null) || (formatterJsonArray(content) != null);
	}

	public static Object getValue(String key, String content) {
		JSONObject jsonObject = formatterJsonObject(content);

		if (jsonObject != null) {
			if (!jsonObject.isNull(key)) {

				try {
					return jsonObject.get(key);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}
		return null;

	}

	public static String getValueString(String key, String content) {
		JSONObject jsonObject = formatterJsonObject(content);

		if (jsonObject != null) {
			if (!jsonObject.isNull(key)) {

				try {
					return jsonObject.get(key).toString();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}
		return null;

	}

}

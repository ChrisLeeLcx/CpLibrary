package cn.lee.cplibrary.util;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import cn.lee.cplibrary.R;


/**
 * 本类中Toast都是单例模式，有原生Toast、自定义Toast
 * @author: ChrisLee at 2016-11-3
 */
public class ToastUtil {
	private static Toast mToast;
	private static Toast toast;

	/**
	 * function:显示Toast（自定义和原生在此处修改）不含图片
	 */
	public static void showToast(Context context, String text) {
		toast1word(context, text);
//		 toastCommon(context, text);
	}

	public static void showToast(Context context, int string_id) {
		toast1word(context, context.getResources().getString(string_id));
		// toastCommon(context, text);
	}

	/**
	 * function:单例显示Toast，不改变Toast的样式
	 */
	private static void toastCommon(Context context, String text) {
		if (mToast == null) {
			mToast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
		} else {
			mToast.setText(text);
			mToast.setDuration(Toast.LENGTH_SHORT);

		}
		mToast.show();
	}

	/**
	 * function:显示一句话，自定义的Toast
	 */
	private static void toast1word(Context context, String text) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.cp_toast_one_word, null);
		TextView tv_toast1 =  view.findViewById(R.id.tv_toast1);
		tv_toast1.setText(text);
		tv_toast1.setGravity(Gravity.CENTER);
		// tv_toast1.setLayoutParams(new LinearLayout.LayoutParams(
		// LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		if (toast == null) {
			toast = new Toast(context);
		}
		toast.setView(view);
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}

	/**
	 * function: 取消Toast
	 */
	public static void cancelToast() {
		if (mToast != null) {
			mToast.cancel();
		}
		if (toast != null) {
			toast.cancel();
		}
	}

}

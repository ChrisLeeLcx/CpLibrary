package cn.lee.cplibrary.util;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * @desc:adapterView嵌套帮助类
 * @author ChrisLee
 * @time  2017-4-12 下午1:04:50
 */
public class AdapterViewHeightHelper {
	/**
	 * @fun：   在notifyDataSetChanged()和setAdapter之后设置
	 * @param listView
	 * @author: ChrisLee at 2017-9-6 下午3:03:38
	 */
	public static void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}

		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}
	/**
	 * @fun：   在notifyDataSetChanged()和setAdapter之后设置
	 * @param gridView
	 * @author: ChrisLee at 2017-9-6 下午3:03:38
	 */
	public static void setGridViewHeightBasedOnChildren(Context context,
			GridView gridView, int columnCount, int verticalSpacingDp) {
		ListAdapter listAdapter = gridView.getAdapter();
		if (listAdapter == null) {
			return;
		}

		int totalHeight = 0;
		View listItem = listAdapter.getView(0, null, gridView);
		listItem.measure(0, 0);
		int rowCount = (int) Math.ceil(listAdapter.getCount()
				/ (float) columnCount);
		totalHeight = listItem.getMeasuredHeight() * rowCount;
		float density = context.getResources().getDisplayMetrics().density;
		int verticalSpacing = (int) Math.ceil(verticalSpacingDp * density
				* rowCount * 2.5);
		// Log.i("xxxxx", "---->verticalSpacing:" + verticalSpacing);

		ViewGroup.LayoutParams params = gridView.getLayoutParams();
		params.height = totalHeight + verticalSpacing;
		gridView.setLayoutParams(params);
	}
}
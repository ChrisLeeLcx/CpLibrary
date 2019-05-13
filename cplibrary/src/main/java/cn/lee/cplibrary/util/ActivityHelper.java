package cn.lee.cplibrary.util;

import android.app.Activity;

import java.util.LinkedList;


/**
 * @author ChrisLee
 */
public class ActivityHelper {

    private LinkedList<Activity> mActivityList = new LinkedList<Activity>();

    private static ActivityHelper mActivityHelper;

    public static ActivityHelper getInstance() {
        if (null == mActivityHelper) {
            mActivityHelper = new ActivityHelper();
        }
        return mActivityHelper;
    }

    public void addActivity(Activity activity) {
        mActivityList.add(activity);
    }

    public void removeActivity(Activity activity) {
        if (mActivityList.contains(activity)) {
            mActivityList.remove(activity);
            activity.finish();
        }
    }

    public void finishActivity(Class<?> cls) {
        for (int i = 0; i < mActivityList.size(); i++) {
            if (mActivityList.get(i).getClass().equals(cls)) {
                mActivityList.get(i).finish();
                mActivityList.remove(i);
            }
        }

    }

    public void finishCurrentActivity() {
        if (mActivityList.size() > 0) {
            mActivityList.get(mActivityList.size() - 1).finish();
            mActivityList.remove(mActivityList.size() - 1);
        }

    }

    public void finishAllActivity() {
        for (int i = 0; i < mActivityList.size(); i++) {
            mActivityList.get(i).finish();
        }
        mActivityList.clear();
    }

    public Activity getTopActivity() {
        if (mActivityList.size() > 0) {
            return mActivityList.getLast();
        }
        return null;
    }

    public void showAllActivity() {
        for (Activity activity : mActivityList) {
//			LogUtil.d("", this, "showAllActivity:" + activity.getClass().getName());
        }
    }

    public LinkedList<Activity> getMActivityList() {
        return mActivityList;
    }


}

package cn.lee.cplibrary.widget.statelayout.helper;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;

import cn.lee.cplibrary.R;
import cn.lee.cplibrary.widget.statelayout.StateLayout;
import cn.lee.cplibrary.widget.statelayout.bean.EmptyItem;
import cn.lee.cplibrary.widget.statelayout.bean.ErrorItem;
import cn.lee.cplibrary.widget.statelayout.bean.LoadingItem;
import cn.lee.cplibrary.widget.statelayout.bean.LoginItem;
import cn.lee.cplibrary.widget.statelayout.bean.NoNetworkItem;
import cn.lee.cplibrary.widget.statelayout.bean.TimeOutItem;
import cn.lee.cplibrary.widget.statelayout.holder.EmptyViewHolder;
import cn.lee.cplibrary.widget.statelayout.holder.ErrorViewHolder;
import cn.lee.cplibrary.widget.statelayout.holder.LoadingViewHolder;
import cn.lee.cplibrary.widget.statelayout.holder.LoginViewHolder;
import cn.lee.cplibrary.widget.statelayout.holder.NoNetworkViewHolder;
import cn.lee.cplibrary.widget.statelayout.holder.TimeOutViewHolder;


/**
 * <pre>
 *     author : fingdo
 *     e-mail : fingdo@qq.com
 *     time   : 2017/03/10
 *     desc   : StateLayout帮助类
 *     version: 1.0
 * </pre>
 */

public class LayoutHelper {

    /**
     * 解析布局中的可选参数
     *
     * @param context
     * @param attrs
     * @param stateLayout
     */
    public static void parseAttr(Context context, AttributeSet attrs, StateLayout stateLayout) {
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.cp_StateLayout, 0, 0);
        try {
            int errorImg = a.getResourceId(R.styleable.cp_StateLayout_cp_errorImg, -1);
            String errorText = a.getString(R.styleable.cp_StateLayout_cp_errorText);
            stateLayout.setErrorItem(new ErrorItem(errorImg, errorText));

            int timeOutImg = a.getResourceId(R.styleable.cp_StateLayout_cp_timeOutImg, -1);
            String timeOutText = a.getString(R.styleable.cp_StateLayout_cp_timeOutText);
            stateLayout.setTimeOutItem(new TimeOutItem(timeOutImg, timeOutText));

            int emptyImg = a.getResourceId(R.styleable.cp_StateLayout_cp_emptyImg, -1);
            String emptyText = a.getString(R.styleable.cp_StateLayout_cp_emptyText);
            stateLayout.setEmptyItem(new EmptyItem(emptyImg, emptyText));

            int noNetworkImg = a.getResourceId(R.styleable.cp_StateLayout_cp_noNetworkImg, -1);
            String noNetworkText = a.getString(R.styleable.cp_StateLayout_cp_noNetworkText);
            stateLayout.setNoNetworkItem(new NoNetworkItem(noNetworkImg, noNetworkText));

            int loginImg = a.getResourceId(R.styleable.cp_StateLayout_cp_loginImg, -1);
            String loginText = a.getString(R.styleable.cp_StateLayout_cp_loginText);
            stateLayout.setLoginItem(new LoginItem(loginImg, loginText));

            String loadingText = a.getString(R.styleable.cp_StateLayout_cp_loadingText);
            stateLayout.setLoadingItem(new LoadingItem(loadingText));
        } finally {
            a.recycle();
        }
    }

    /**
     * 获取初始的错误View
     *
     * @param layoutInflater 布局填充器
     * @param item           错误bean
     * @param layout         容器
     * @return 错误View
     */
    public static View getErrorView(LayoutInflater layoutInflater, ErrorItem item,
                                    final StateLayout layout) {
        View view = layoutInflater.inflate(R.layout.cp_layout_error, null);
        if (item != null) {
            ErrorViewHolder holder = new ErrorViewHolder(view);
            view.setTag(holder);

            if (!TextUtils.isEmpty(item.getTip())) {
                holder.tvTip.setText(item.getTip());
            }
            if (item.getResId() != -1) {
                holder.ivImg.setImageResource(item.getResId());
            }
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (layout.getRefreshLListener() != null) {
                        layout.getRefreshLListener().refreshClick();
                    }
                }
            });
        }
        return view;
    }

    /**
     * 获取初始的没有网络View
     *
     * @param layoutInflater 布局填充器
     * @param item           没有网络bean
     * @param layout         容器
     * @return 没有网络View
     */
    public static View getNoNetworkView(LayoutInflater layoutInflater, NoNetworkItem item,
                                        final StateLayout layout) {
        View view = layoutInflater.inflate(R.layout.cp_layout_no_network, null);
        if (item != null) {
            NoNetworkViewHolder holder = new NoNetworkViewHolder(view);
            view.setTag(holder);

            if (!TextUtils.isEmpty(item.getTip())) {
                holder.tvTip.setText(item.getTip());
            }
            if (item.getResId() != -1) {
                holder.ivImg.setImageResource(item.getResId());
            }
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (layout.getRefreshLListener() != null) {
                        layout.getRefreshLListener().refreshClick();
                    }
                }
            });
        }
        return view;
    }

    /**
     * 获取初始的加载View
     *
     * @param layoutInflater 布局填充器
     * @param item           加载bean
     * @return 加载View
     */
    public static View getLoadingView(LayoutInflater layoutInflater, LoadingItem item) {
        View view = layoutInflater.inflate(R.layout.cp_layout_loading, null);
        if (item != null) {
            LoadingViewHolder holder = new LoadingViewHolder(view);
            view.setTag(holder);

            ProgressBar progressBar = new ProgressBar(view.getContext());
            progressBar.setIndeterminateDrawable(view.getResources().getDrawable(R.drawable.cp_bg_loading));
            holder.frameLayout.addView(progressBar);

            if (!TextUtils.isEmpty(item.getTip())) {
                holder.tvTip.setText(item.getTip());
            }
        }
        return view;
    }

    /**
     * 获取初始的超时View
     *
     * @param layoutInflater 布局填充器
     * @param item           超时bean
     * @param layout         容器
     * @return 超时View
     */
    public static View getTimeOutView(LayoutInflater layoutInflater, TimeOutItem item,
                                      final StateLayout layout) {
        View view = layoutInflater.inflate(R.layout.cp_layout_time_out, null);
        if (item != null) {
            TimeOutViewHolder holder = new TimeOutViewHolder(view);
            view.setTag(holder);

            if (!TextUtils.isEmpty(item.getTip())) {
                holder.tvTip.setText(item.getTip());
            }
            if (item.getResId() != -1) {
                holder.ivImg.setImageResource(item.getResId());
            }
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (layout.getRefreshLListener() != null) {
                        layout.getRefreshLListener().refreshClick();
                    }
                }
            });
        }
        return view;
    }

    /**
     * 获取初始的空数据View
     *
     * @param layoutInflater 布局填充器
     * @param item           空数据bean
     * @return 空数据View
     */
    public static View getEmptyView(LayoutInflater layoutInflater, EmptyItem item) {
        View view = layoutInflater.inflate(R.layout.cp_layout_empty, null);
        if (item != null) {
            EmptyViewHolder holder = new EmptyViewHolder(view);
            view.setTag(holder);

            if (!TextUtils.isEmpty(item.getTip())) {
                holder.tvTip.setText(item.getTip());
            }
            if (item.getResId() != -1) {
                holder.ivImg.setImageResource(item.getResId());
            }
        }
        return view;
    }


    /**
     * 获取初始的空数据View
     *
     * @param layoutInflater 布局填充器
     * @param item           空数据bean
     * @return 空数据View
     */
    public static View getLoginView(LayoutInflater layoutInflater, LoginItem item,
                                    final StateLayout layout) {
        View view = layoutInflater.inflate(R.layout.cp_layout_login, null);
        if (item != null) {
            LoginViewHolder holder = new LoginViewHolder(view);
            view.setTag(holder);

            if (!TextUtils.isEmpty(item.getTip())) {
                holder.tvTip.setText(item.getTip());
            }
            if (item.getResId() != -1) {
                holder.ivImg.setImageResource(item.getResId());
            }
            view.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (layout.getRefreshLListener() != null) {
                        layout.getRefreshLListener().loginClick();
                    }
                }
            });
        }
        return view;
    }

}

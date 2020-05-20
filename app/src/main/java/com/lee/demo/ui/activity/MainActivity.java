package com.lee.demo.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.lee.demo.R;
import com.lee.demo.base.BaseWebViewActivity;
import com.lee.demo.util.gaode.loc.GaoDeLBSUtil;

import java.util.ArrayList;
import java.util.List;

import cn.lee.cplibrary.util.permissionutil.PermissionDemoActivity;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<ActivityBean> totalList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerView);
        initData();
        initRecyclerView();


        findViewById(R.id.iv_to_top).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.smoothScrollToPosition(0);
            }
        });

    }
    private void initData() {
        totalList.add(new ActivityBean("未知分类", OtherActivity.class));
        totalList.add(new ActivityBean("工具类", UtilActivity.class));
        totalList.add(new ActivityBean("各种选框", DialogActivity.class));
        totalList.add(new ActivityBean("安卓6.0运行时权限", PermissionActivity.class));
        totalList.add(new ActivityBean("拍照相册", PhotoActivity.class));
        totalList.add(new ActivityBean("图片点击放大", ImageZoomActivity.class));
        totalList.add(new ActivityBean("签名", SignActivity.class));
        totalList.add(new ActivityBean("各种状态下布局", StateLayoutActivity.class));
        totalList.add(new ActivityBean("各种进度条", ProgressBarActivity.class));
        totalList.add(new ActivityBean("仿瓜子二手车品牌选择", BrandActivity.class));
        totalList.add(new ActivityBean("各种EditText", EditTextActivity.class));
        totalList.add(new ActivityBean("水波纹效果", ObjectRippleActivity.class));
        totalList.add(new ActivityBean("网页加载", BaseWebViewActivity.class));
        totalList.add(new ActivityBean("轮播图和滚轮广告", RollPagerActivity.class));
        totalList.add(new ActivityBean("拖拽排序RecyclerView", DragSortRvActivity.class));
        totalList.add(new ActivityBean("滑动点击删除Rv", SlideMenuActivity.class));
        totalList.add(new ActivityBean("懒加载ViewPager", OrderActivity.class));
        totalList.add(new ActivityBean("考试答题", ExamActivity.class));
        totalList.add(new ActivityBean("各种截图", ShotActivity.class));
        totalList.add(new ActivityBean("短信监听", GetVerCodeActivity.class));
        totalList.add(new ActivityBean("高德定位", GDLocationActivity.class));
    }

    private void initRecyclerView() {
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(new SelectAdapter(this, totalList));
    }

    public class SelectAdapter extends RecyclerView.Adapter<SelectAdapter.ViewHolder> {
        private Context context;
        private List<ActivityBean> totalList;

        public SelectAdapter(Context context, List<ActivityBean> list) {
            this.context = context;
            this.totalList = list;
        }

        @NonNull
        @Override
        public SelectAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            //若root参数使用parent 则只会显示第一行
            View view = LayoutInflater.from(context).inflate(R.layout.item_rv_demo, null, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull SelectAdapter.ViewHolder holder, final int position) {
            holder.btn_select.setText(totalList.get(position).getName());
            holder.btn_select.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, totalList.get(position).getCls()));
                }
            });
        }

        @Override
        public int getItemCount() {
            return totalList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            private Button btn_select;

            public ViewHolder(View itemView) {
                super(itemView);
                btn_select = itemView.findViewById(R.id.btn_select);

            }
        }
    }

    public class ActivityBean {
        private String name;
        private Class<?> cls;

        public ActivityBean(String name, Class<?> cls) {
            this.name = name;
            this.cls = cls;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Class<?> getCls() {
            return cls;
        }

        public void setCls(Class<?> cls) {
            this.cls = cls;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GaoDeLBSUtil.getInstance(this).stopLocation();
        GaoDeLBSUtil.getInstance(this).destroyLocation();
    }
}

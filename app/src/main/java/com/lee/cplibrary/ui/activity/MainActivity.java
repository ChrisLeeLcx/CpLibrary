package com.lee.cplibrary.ui.activity;

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

import com.lee.cplibrary.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private RecyclerView recyclerView;
    private List<ActivityBean> totalList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerView);
        initData();
        initRecyclerView();
    }

    private void initData() {
        totalList.add(new ActivityBean("DialogActivity", DialogActivity.class));
        totalList.add(new ActivityBean("PermissionActivity", PermissionActivity.class));
        totalList.add(new ActivityBean("PhotoActivity", PhotoActivity.class));
        totalList.add(new ActivityBean("图片点击放大", ImageZoomActivity.class));
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
}

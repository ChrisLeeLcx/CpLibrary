package cn.lee.cplibrary.util.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import cn.lee.cplibrary.R;
import cn.lee.cplibrary.util.ScreenUtil;

/**
 * Created by ChrisLee on 2019/3/26.
 */

public abstract class CpBaseDialogAdapter<T extends BaseDialogBean> extends RecyclerView.Adapter<CpBaseDialogAdapter.ViewHolder>{
    protected Context context;
    protected List<T> totalList;
    protected OnItemClickListener listener;
    protected CpBaseDialog dialog;

    protected abstract int getLayoutResId();//获取布局资源id

    protected CpBaseDialogAdapter(Context context, List<T> list, CpBaseDialog dialog) {
        this.context = context;
        this.totalList = list;
        this.dialog = dialog;
    }
    public interface OnItemClickListener {
        void onItemClick(CpBaseDialogAdapter adapter, View rootView, int position);
    }

    @NonNull
    @Override
    public CpBaseDialogAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //若root参数使用parent 则只会显示第一行
        View view = LayoutInflater.from(context).inflate(getLayoutResId(), null, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final CpBaseDialogAdapter.ViewHolder holder, final int position) {
        if (position == 0 && !dialog.isShowTitle()) {
            holder.line.setVisibility(View.INVISIBLE);
        } else {
            holder.line.setVisibility(View.VISIBLE);
        }
        holder.tvName.setText(totalList.get(position).getName());
        holder.tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(CpBaseDialogAdapter.this, holder.rootView, position);
                }
            }
        });
        holder.tvName.setTextSize(ScreenUtil.sp(context, dialog.getTxtSize()));
        holder.tvName.setTextColor(dialog.getTxtColor());
        int h = dialog.getItemHeight();
        if (h != LinearLayout.LayoutParams.WRAP_CONTENT && h != LinearLayout.LayoutParams.MATCH_PARENT) {
            h = ScreenUtil.dp2px(context, h);
        }
        holder.tvName.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, h));
    }


    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
    @Override
    public int getItemCount() {
        return totalList.size();
    }
    protected class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvName;
        public View line;
        public View rootView;

        public ViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            line = itemView.findViewById(R.id.line);
            rootView = itemView;
        }
    }
}

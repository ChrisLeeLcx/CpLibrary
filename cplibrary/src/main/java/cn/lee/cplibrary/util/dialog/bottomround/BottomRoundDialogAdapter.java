package cn.lee.cplibrary.util.dialog.bottomround;

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
import cn.lee.cplibrary.util.dialog.bottom.BaseDialogBean;

/**
 * @author: ChrisLee
 * @time: 2018/11/21
 */

public class BottomRoundDialogAdapter<T extends BaseDialogBean> extends RecyclerView.Adapter<BottomRoundDialogAdapter.ViewHolder> {
    private Context context;
    private List<T> totalList;
    private OnItemClickListener listener;
    private CpBottomRoundDialog dialog;

    public BottomRoundDialogAdapter(Context context, List<T> list, CpBottomRoundDialog dialog) {
        this.context = context;
        this.totalList = list;
        this.dialog = dialog;

    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public BottomRoundDialogAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //若root参数使用parent 则只会显示第一行
        View view = LayoutInflater.from(context).inflate(R.layout.cp_item_bottom_round_dialog, null, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final BottomRoundDialogAdapter.ViewHolder holder, final int position) {
        if (position == 0 && !dialog.isShowTitle()) {
            holder.line.setVisibility(View.INVISIBLE);
        } else {
            holder.line.setVisibility(View.VISIBLE);
        }
        if (position == 0) {
            if (dialog.isShowTitle()) {
                holder.tvName.setBackground(context.getResources().getDrawable(R.drawable.cp_photo_selector));
            } else {
                holder.tvName.setBackground(context.getResources().getDrawable(R.drawable.cp_photo_bgt10_selector));
            }
        } else if (position == totalList.size() - 1) {
            holder.tvName.setBackground(context.getResources().getDrawable(R.drawable.cp_photo_bgb10_selector));
        } else {
            holder.tvName.setBackground(context.getResources().getDrawable(R.drawable.cp_photo_selector));
        }
        holder.tvName.setText(totalList.get(position).getName());
        holder.tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(BottomRoundDialogAdapter.this, holder.rootView, position);
                }
            }
        });
        holder.tvName.setTextSize(ScreenUtil.sp2px(context, dialog.getTxtSize()));
        holder.tvName.setTextColor(dialog.getTxtColor());
        int h = dialog.getItemHeight();
        if (h != LinearLayout.LayoutParams.WRAP_CONTENT && h != LinearLayout.LayoutParams.MATCH_PARENT) {
            h = ScreenUtil.dp2px(context, h);
        }
        holder.tvName.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, h));
    }

    @Override
    public int getItemCount() {
        return totalList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName;
        private View line;
        private View rootView;

        public ViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            line = itemView.findViewById(R.id.line);
            rootView = itemView;
        }
    }

    public interface OnItemClickListener {
        void onItemClick(BottomRoundDialogAdapter adapter, View rootView, int position);
    }
}

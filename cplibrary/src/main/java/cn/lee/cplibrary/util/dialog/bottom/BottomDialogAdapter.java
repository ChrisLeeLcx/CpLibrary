package cn.lee.cplibrary.util.dialog.bottom;

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
 * @author: ChrisLee
 * @time: 2018/11/21
 */

public class BottomDialogAdapter<T extends BaseDialogBean> extends RecyclerView.Adapter<BottomDialogAdapter.ViewHolder> {
    private Context context;
    private List<T> totalList;
    private OnItemClickListener listener;
    private CpBottomDialog dialog;

    public BottomDialogAdapter(Context context, List<T> list, CpBottomDialog dialog) {
        this.context = context;
        this.totalList = list;
        this.dialog = dialog;

    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public BottomDialogAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //若root参数使用parent 则只会显示第一行
        View view = LayoutInflater.from(context).inflate(R.layout.cp_item_bottom_dialog, null, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final BottomDialogAdapter.ViewHolder holder, final int position) {
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
                    listener.onItemClick(BottomDialogAdapter.this, holder.rootView, position);
                }
            }
        });
        holder.tvName.setTextSize(ScreenUtil.sp2px(context, dialog.getTxtSize()));
        holder.tvName.setTextColor(dialog.getTxtColor());
        if (dialog.isChangeBg()) {
            holder.tvName.setBackgroundColor(dialog.getBgColor());
        }
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
        void onItemClick(BottomDialogAdapter adapter, View rootView, int position);
    }
}

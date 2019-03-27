package cn.lee.cplibrary.util.dialog.bottomround;

import android.content.Context;

import java.util.List;

import cn.lee.cplibrary.R;
import cn.lee.cplibrary.util.dialog.BaseDialogBean;
import cn.lee.cplibrary.util.dialog.CpBaseDialog;
import cn.lee.cplibrary.util.dialog.CpBaseDialogAdapter;
import cn.lee.cplibrary.util.dialog.bottom.CpBottomDialog;

/**
 * 底部弹出的Dialog列表 四周有弧度
 *
 * @author: ChrisLee
 * @time: 2018/11/21
 */

public class CpBottomRoundDialog<T extends BaseDialogBean> extends CpBaseDialog<T> {

    private CpBottomRoundDialog(Context context) {
        super(context);
    }

    @Override
    protected CpBaseDialogAdapter getAdapter() {
        return  new BottomRoundDialogAdapter(context, list, this);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.cp_dialog_bottom_round_list;
    }
    public static class Builder<K extends BaseDialogBean> extends  CpBaseDialog.Builder<K>{

        @Override
        protected CpBaseDialog getDialog(Context context) {
            return new CpBottomRoundDialog(context);
        }
        private Builder(Context context, List<K> list) {
            super(context, list);
        }
        public static <K extends BaseDialogBean>  Builder builder(Context context, List<K> list) {
            return new  Builder(context, list);
        }

    }


}

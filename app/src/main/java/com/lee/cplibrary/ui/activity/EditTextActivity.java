package com.lee.cplibrary.ui.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.lee.cplibrary.R;
import com.lee.cplibrary.base.BaseActivity;
import com.lee.cplibrary.base.SwipeBackActivity;

import cn.lee.cplibrary.widget.edittext.DecimalInputFilter;
import cn.lee.cplibrary.widget.edittext.IdcardValidator;
import cn.lee.cplibrary.util.LogUtil;
import cn.lee.cplibrary.util.ObjectUtils;
import cn.lee.cplibrary.util.SystemUtil;
import cn.lee.cplibrary.util.ViewUtil;
import cn.lee.cplibrary.widget.edittext.PayPsdInputView;
import cn.lee.cplibrary.widget.edittext.bankcard.BankCardTextWatcher;
import cn.lee.cplibrary.widget.edittext.bankcard.BankInfoBean;
import cn.lee.cplibrary.widget.keyboardview.PwdKeyboardView;


public class EditTextActivity extends SwipeBackActivity {

    private EditText etPrice,etBankCardNum, etIdcardno;
    private PayPsdInputView etSetPsd, etSetPsd1;
    private TextView tvNo;

    @Override
    protected BaseActivity getSelfActivity() {
        return this;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_edit_text;
    }

    @Override
    public String getPagerTitle() {
        return "各种输入框";
    }

    @Override
    public String getPagerRight() {
        return null;
    }

    @Override
    public void initView() {
        etPrice = (EditText) findViewById(R.id.et_price);
        etBankCardNum = (EditText) findViewById(R.id.et_bank_card_num);
        etIdcardno = (EditText) findViewById(R.id.et_idcardno);
        etSetPsd = (PayPsdInputView) findViewById(R.id.et_setPsd);
        etSetPsd1 = (PayPsdInputView) findViewById(R.id.et_setPsd1);
        tvNo = (TextView) findViewById(R.id.tv_no);
        PwdKeyboardView keyboardView = (PwdKeyboardView) findViewById(R.id.key_board);
        //限制只能输入2位小数
        InputFilter[] filtersPrice = {new DecimalInputFilter(2)};
        etPrice.setFilters(filtersPrice);
        //监听
        BankCardTextWatcher.bind(etBankCardNum);//实现每4个卡号空一格
        etBankCardNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                LogUtil.i("", "", "银行卡号输入=" + etBankCardNum.getText().toString().trim());
            }
        });
        findViewById(R.id.btn_sure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check();
            }
        });

        keyboardView.setOnKeyListener(new PwdKeyboardView.OnKeyListener() {
            @Override
            public void onInput(String text) {
                LogUtil.i("", "", "onInput: text = " + text);
                etSetPsd1.append(text);
                String content = etSetPsd1.getText().toString();
                LogUtil.i("", "", "onInput: content = " + content);

            }
            @Override
            public void onDelete() {
                LogUtil.i("", "", "onDelete:  " );
                String content = etSetPsd1.getText().toString();
                if (content.length() > 0) {
                    etSetPsd1.setText(content.substring(0, content.length() - 1));
                }
            }
        });


    }

    @Override
    protected void initData() {
        initPsdInputView();
    }

    private void initPsdInputView() {
        SystemUtil.setEtFocusChangeSystemFit(getSelfActivity(), etSetPsd,etSetPsd1);
        ViewUtil.setEditTextEditState(false,etSetPsd1);//设置不可线性密码框 不可编辑，则不能弹出系统键盘
        etSetPsd.setComparePassword(new PayPsdInputView.onPasswordListener() {
            @Override
            public void onDifference(String oldPsd, String newPsd) {//两次输入不同
                LogUtil.i("", "", "不同" + "-oldPsd=" + oldPsd + "；newPsd=" + newPsd);
            }

            @Override
            public void onEqual(String psd) {//两次输入相等
                LogUtil.i("", "", "相同" + "-psd=" + psd);
            }

            @Override
            public void inputFinished(String inputPsd) {//输入完成
                toast("完成：" + inputPsd);
                LogUtil.i("", "", "密码：" + etSetPsd.getPasswordString());
                etSetPsd.cleanPsd();//清空密码
                SystemUtil.closeKeyboard(getSelfActivity());
            }
        });
        etSetPsd1.setComparePassword(new PayPsdInputView.onPasswordListener() {
            @Override
            public void onDifference(String oldPsd, String newPsd) {
            }

            @Override
            public void onEqual(String psd) {
            }

            @Override
            public void inputFinished(String inputPsd) {
                toast("完成：" + inputPsd);
            }
        });
    }

    /**
     * 检验银行卡号、身份证是否正确
     *
     * @return
     */
    private boolean check() {
        //银行卡
        String cardNum = etBankCardNum.getText().toString().replace(" ", "");
        BankInfoBean bankInfoBean = new BankInfoBean(cardNum);
        if (ObjectUtils.isEmpty(cardNum)) {
            toast("请输入银行卡号");
            return false;
        }
        if (!BankInfoBean.checkBankCard(cardNum)) {
            toast("银行卡号不正确");
            return false;
        }
        if (!"储蓄卡".equals(bankInfoBean.getCardType())) {
            toast("该业务仅支持储蓄卡");
            return false;
        }
        //身份证
        String idcardNo = etIdcardno.getText().toString().trim();
        if (ObjectUtils.isEmpty(idcardNo)) {
            toast("请输入身份证");
            return false;
        }
        if (!new IdcardValidator().isValidatedAllIdcard(idcardNo)) {
            toast("身份证不正确");
            return false;
        }
        tvNo.setText("银行卡："+cardNum+"\n身份证："+idcardNo);
        return true;
    }

}

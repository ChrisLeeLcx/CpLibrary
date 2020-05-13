package com.lee.demo.ui.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.Html;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.lee.demo.R;
import com.lee.demo.base.BaseActivity;
import com.lee.demo.model.QuestionsBean;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;

import cn.lee.cplibrary.util.DrawableUtil;
import cn.lee.cplibrary.util.LogUtil;
import cn.lee.cplibrary.util.ObjectUtils;
import cn.lee.cplibrary.util.ScreenUtil;
import cn.lee.cplibrary.util.dialog.CpComDialog;
import cn.lee.cplibrary.util.timer.ScheduledHandler;
import cn.lee.cplibrary.util.timer.ScheduledTimer;
import cn.lee.cplibrary.util.timer.TimeUtils;
import cn.lee.cplibrary.widget.flipper.FlipperLayout;

/**
 * 考试页面
 * Created by ChrisLee on 2019/9/11.
 */

public class ExamActivity extends BaseActivity implements FlipperLayout.OnSlidePageListener, View.OnClickListener {
    FlipperLayout fl;
    Button btnLeft;
    View vCenter;
    Button btnRight;
    LinearLayout llBottom;
    TextView tvTimes;
    private List<QuestionsBean.DataBean> data;
    private QuestionsBean bean;
    private int subjectNum = 0;//题目数量
    private String[] subjectType = {"单选题", "判断题", "多选题"};//题目类型
    String exam_questions = "", exam_answers = "";//用户获得到的考题号,答案
    private int timesAll = 0;//考试可用总时长 ，单位：s ：考试结束时间-当前系统时间
    //    private int timesAll = 1 * 45 * 60;//总时长45分钟 ，单位：s
    private int timesSpent = 0;// 用户考试的耗时，单位：s
    ScheduledTimer scheduledTimer;


    @Override
    protected BaseActivity getSelfActivity() {
        return this;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_exam;
    }

    @Override
    public String getPagerTitle() {
        return "";
    }

    @Override
    public String getPagerRight() {
        return "";
    }

    @Override
    public void initView() {
        findViews();
        btnLeft.setVisibility(View.GONE);
        vCenter.setVisibility(View.GONE);
    }

    private void findViews() {
        fl = findViewById(R.id.fl);
        btnLeft = findViewById(R.id.btn_left);
        vCenter = findViewById(R.id.v_center);
        btnRight = findViewById(R.id.btn_right);
        llBottom = findViewById(R.id.ll_bottom);
        tvTimes = findViewById(R.id.tv_times);
        findViewById(R.id.tv_submit).setOnClickListener(this);
        btnLeft.setOnClickListener(this);
        btnRight.setOnClickListener(this);
    }


    @Override
    protected void initData() {
        bean = getBean();
        data = bean.getData();
        subjectNum = Integer.valueOf(bean.getTotal_num());
        initPage();
        //计算总时长
        timesAll = Integer.valueOf(bean.getEnd_time_span()) - getCurTimeSeconds();//用户可答题总时长：考试结束时间 - 当前系统时间
//        if (timesAll <= 0) {
//            CpComDialog.Builder.builder(getSelfActivity())
//                    .setTitle("温馨提示").setContent("非常抱歉，考试时间已过期，您不能参加本场考试！").setSure("退出考试")
//                    .setCancel(false)
//                    .build().show1BtnDialog(new CpComDialog.Dialog1BtnCallBack() {
//                @Override
//                public void sure() {
//                    finishCurrentActivity();
//                }
//
//
//            });
//            return;
//        }
        tvTimes.setText("倒计时 " + formatTime(timesAll));
        startScheduledTimer();

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_left:
                fl.autoPrePage();
                break;
            case R.id.btn_right:
                if (data.get(fl.getIndex() - 1).getUserAnswerMap().size() <= 0) {
                    toast("请先作答当前题目");
                    return;
                }
                fl.autoNextPage();
                break;
            case R.id.tv_submit:
                if (isAllAnswers()) {
                    CpComDialog.Builder.builder(getSelfActivity()).
                            setTitle("温馨提示").setContent("您确定提交本次考试答案吗？").setSure("提交答案").setTxtCancel("再检查下")
                            .setCancel(false)
                            .build().show2BtnDialog(new CpComDialog.Dialog2BtnCallBack() {
                        @Override
                        public void sure() {
                            requestGetAnswer();
                        }

                        @Override
                        public void cancel() {
                        }
                    });
                }
                break;
        }

    }


    /*** 获取系统当前时间 单位：s*/
    public static int getCurTimeSeconds() {
        int curTime = Integer.parseInt(String.valueOf(TimeUtils.getCurTimeMillis() / 1000));
        return curTime;
    }

    /**
     * 初始化页面View，初始化页面数据（第一页和第二页）
     */
    private void initPage() {
        if (data.size() > 0) {
            fl.removeAllViews();
            fl.setIndex(1);
            View preView = LayoutInflater.from(ExamActivity.this).inflate(R.layout.item_exam, null);//上一页
            View curView = LayoutInflater.from(ExamActivity.this).inflate(R.layout.item_exam, null);//当前页面
            View nextView = LayoutInflater.from(ExamActivity.this).inflate(R.layout.item_exam, null);//下一页
            fl.initFlipperViews(this, nextView, curView, preView);
            if (data.size() == 1) {
                //填充第一页的文本
                setView(curView, data.get(0));
            } else if (data.size() >= 2) {
                setView(curView, data.get(0));  //填充第一页的文本
                setView(nextView, data.get(1)); //填充第二页的文本
            }
        }
    }


    private void setView(View view, QuestionsBean.DataBean dataBean) {
        TextView tvType = view.findViewById(R.id.tv_type);
        TextView tvNum = view.findViewById(R.id.tv_num);
        TextView tvTitle = view.findViewById(R.id.tv_title);
        RadioGroup rgSingle = view.findViewById(R.id.rg_single);
        LinearLayout llMulti = view.findViewById(R.id.ll_multi);
        String sequence = String.format(getResources().getString(R.string.exam_num), dataBean.getSequence() + "", subjectNum + "");
        tvNum.setText(Html.fromHtml(sequence));
        tvType.setText(subjectType[dataBean.getType()]);
        tvTitle.setText(dataBean.getSequence() + "." + dataBean.getContent());
        List<String> option = dataBean.getOption();//选项
        switch (dataBean.getType()) { //0单选题 1判断题 2多选题
            case 0://单选题
                llMulti.setVisibility(View.GONE);
                rgSingle.setVisibility(View.VISIBLE);
                addView(rgSingle, dataBean);

                break;
            case 1://判断题
                llMulti.setVisibility(View.GONE);
                rgSingle.setVisibility(View.VISIBLE);
                if (option == null || option.size() <= 0) {
                    option = new ArrayList<>();
                    option.add("A、正确");
                    option.add("B、错误");
                }
                dataBean.setOption(option);
                addView(rgSingle, dataBean);
                break;
            case 2://多选题
                llMulti.setVisibility(View.VISIBLE);
                rgSingle.setVisibility(View.GONE);
                addView(llMulti, dataBean);
                break;
        }
    }

    /**
     * 单选及判断题
     */
    private void addView(RadioGroup rgSingle, final QuestionsBean.DataBean bean) {
        rgSingle.removeAllViews();
        List<String> list = bean.getOption();//选项
        for (int i = 0; i < list.size(); i++) {
            final RadioButton radioButton = new RadioButton(getSelfActivity());
            setCompoundButton(rgSingle, radioButton, list.get(i), bean);
        }
    }

    /**
     * 多选题
     */
    private void addView(LinearLayout llMulti, QuestionsBean.DataBean bean) {
        llMulti.removeAllViews();
        List<String> list = bean.getOption();//选项
        for (int i = 0; i < list.size(); i++) {
            final CheckBox cb = new CheckBox(getSelfActivity());
            setCompoundButton(llMulti, cb, list.get(i), bean);
        }
    }

    /**
     * @param rg
     * @param comBtn
     * @param txt    当前选项的文本
     */
    private void setCompoundButton(LinearLayout rg, CompoundButton comBtn, final String txt, final QuestionsBean.DataBean bean) {
        Activity activity = getSelfActivity();
        comBtn.setText(txt);
        comBtn.setGravity(Gravity.CENTER_VERTICAL);
        comBtn.setPadding(0, ScreenUtil.dp2px(activity, 10), 0, ScreenUtil.dp2px(activity, 10));
        comBtn.setTextColor(Color.parseColor("#333333"));
        comBtn.setTextSize(18);
        RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        comBtn.setLayoutParams(layoutParams);
        DrawableUtil.setDrawableLeft(activity, getResources().getDrawable(R.drawable.selector_exam), comBtn, 12);

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {// 4.4系统 及以下
            try {
                Field field = comBtn.getClass().getSuperclass().getDeclaredField("mButtonDrawable");
                field.setAccessible(true);
                field.set(comBtn, null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            comBtn.setButtonDrawable(null);
        }
        rg.addView(comBtn);
//        //填写已选过的答案
        final TreeMap<String, String> answerMap = bean.getUserAnswerMap();
        comBtn.setChecked(answerMap.containsValue(txt));//用户的答案值包含该选项，则选中
        //选项添加选择监听
        comBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (bean.getType() != 2) {//单选或者判断题
                        answerMap.clear();
                    }
                    answerMap.put(String.valueOf(txt.charAt(0)), txt);//key:是A、B  value是：A、正确 等
                } else {//未选择删除
                    answerMap.remove(String.valueOf(txt.charAt(0)));

                }

            }
        });
    }

    //-------------------------------页面选择监听---------------------------
    @Override
    public View createView(int direction, int index) { //index是序号第几页，index从2开始
        LogUtil.i("", this, "createView-index=" + index);
        View newView = null;
        if (direction == FlipperLayout.OnSlidePageListener.MOVE_TO_LEFT && index < data.size()) { //下一页
            QuestionsBean.DataBean subDataBean = data.get(index);
            newView = LayoutInflater.from(this).inflate(R.layout.item_exam, null);
            setView(newView, subDataBean);

        } else if (direction == FlipperLayout.MOVE_TO_RIGHT && index >= 2) {//上一页
            QuestionsBean.DataBean subDataBean = data.get(index - 2);
            newView = LayoutInflater.from(this).inflate(R.layout.item_exam, null);
            setView(newView, subDataBean);
        }
        return newView;
    }

    @Override
    public void currentPosition(int index) {//index是序号，页面初始化时不会触发此方法
        LogUtil.i("", this, "currentPosition-index=" + index);

        if (index == 1) {//第一页
            btnLeft.setVisibility(View.GONE);
            vCenter.setVisibility(View.GONE);
            btnRight.setVisibility(View.VISIBLE);
//            btnRight.setText("下一题");
        } else if (currentIsNotLastPage()) {//中间页面
            btnLeft.setVisibility(View.VISIBLE);
            vCenter.setVisibility(View.VISIBLE);
            btnRight.setVisibility(View.VISIBLE);
//            btnRight.setText("下一题");

        } else {//最后一页
            btnLeft.setVisibility(View.VISIBLE);
            vCenter.setVisibility(View.GONE);
            btnRight.setVisibility(View.GONE);
//            btnRight.setText("交卷");
        }
    }

    /**
     * 判断当前页面是否最后一页
     */
    @Override
    public boolean currentIsNotLastPage() {
        int index = fl.getIndex();
        if (data.size() == index) {//最后一页
            return false;
        }
        return true;
    }

    /**
     * 判断当前页面是否有下一页（最后一页没有下一页）
     */
    @Override
    public boolean whetherHasNextPage() {
        int index = fl.getIndex();
        if (data.size() == index) {
            return false;
        }
        return true;
    }
    //--------------------------------工具类-----------------------------

    /**
     * 获取最终用户选择的答案和答题号
     */
    public void getAnswers() {
        exam_answers="";
        exam_questions="";
        for (int i = 0; i < data.size(); i++) {
            //获取每道题目答案
            QuestionsBean.DataBean bean = data.get(i);
            exam_answers += bean.getUserAnswerByMap() + ",";
            exam_questions += bean.getId() + ",";
        }
        exam_answers = exam_answers.substring(0, exam_answers.length() - 1);
        exam_questions = exam_questions.substring(0, exam_questions.length() - 1);
        LogUtil.i("", this, "答题号：" + exam_questions);
        LogUtil.i("", this, "答案：" + exam_answers);
    }

    /**
     * 判断用户是否所有题目都做答了
     */
    public boolean isAllAnswers() {
        String seqUnAnswers = "";//未作答的题目序号
        for (int i = 0; i < data.size(); i++) {
            QuestionsBean.DataBean bean = data.get(i);
            if (bean.getUserAnswerMap() == null || bean.getUserAnswerMap().size() <= 0) { //未作答
                seqUnAnswers += bean.getSequence() + "、";
            }
        }
        if (!ObjectUtils.isEmpty(seqUnAnswers)) {
            seqUnAnswers = seqUnAnswers.substring(0, seqUnAnswers.length() - 1);
            CpComDialog.Builder.builder(getSelfActivity()).
                    setTitle("温馨提示").setContent("您尚有题目未作答，题目号是" + seqUnAnswers).setSure("继续答题")
                    .setCancel(false)
                    .build().show1BtnDialog(new CpComDialog.Dialog1BtnCallBack() {
                @Override
                public void sure() {
                    requestGetAnswer();
                }


            });
            return false;
        }
        return true;
    }

    /**
     * 将times 格式化成 小时：分钟：秒的格式
     * 例如：1:30:00 ，45:00 ，0:0:30
     *
     * @param seconds 单位：s
     */
    private String formatTime(int seconds) {
        int milliseconds = seconds * 1000;
        int hh = milliseconds / 1000 / 60 / 60;
        int mm = (milliseconds - (hh * 60 * 60 * 1000)) / 1000 / 60;
        int ss = (milliseconds - (mm * 1000 * 60) - (hh * 60 * 60 * 1000)) / 1000;
        if (hh != 0) {
            return String.format("%01d:%02d:%02d", hh, mm, ss);
        }
        return String.format("%02d:%02d", mm, ss);
    }


    /**
     * 获取答案
     */
    private void requestGetAnswer() {
        getAnswers();
        LogUtil.i("", "exam_questions=" + exam_questions);
        LogUtil.i("", "exam_answers=" + exam_answers);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (scheduledTimer != null) {
            scheduledTimer.cancel();
            scheduledTimer = null;
        }

    }

    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            backToastKeyDown();
            return true;

        }
        return super.onKeyDown(keyCode, event);

    }

    private void backToastKeyDown() {
        if (System.currentTimeMillis() - exitTime > 2000) {
            toast("再按一次退出当前考试！");
            exitTime = System.currentTimeMillis();
        } else {
            finishCurrentActivity();
        }
    }



    //考试倒计时
    private void startScheduledTimer() {
        scheduledTimer = new ScheduledTimer(new ScheduledHandler() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void post(int times) {//times 单位是s
                timesSpent = times;
                tvTimes.setText("倒计时 " + formatTime(timesAll - timesSpent));
            }

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void end() {//倒计时结束
                CpComDialog.Builder.builder(getSelfActivity()).
                        setTitle("温馨提示").setContent("考试时间到，请提交考试答案！").setSure("提交答案")
                        .setCancel(false)
                        .build().show1BtnDialog(new CpComDialog.Dialog1BtnCallBack() {
                    @Override
                    public void sure() {
                        requestGetAnswer();
                    }


                });


            }
        }, 1000, 1000, timesAll);
        scheduledTimer.start();
    }


    public QuestionsBean getBean() {
        QuestionsBean bean = new QuestionsBean();
        bean.setExam_name("2019南京市查验员初级考试");
        bean.setAppointment_id("24");
        bean.setStart_time("2019-10-31 15:00:00");
        bean.setEnd_time("2019-10-31 17:30:00");
        bean.setStart_time_span( String.valueOf(TimeUtils.formatTime2Date(null,bean.getStart_time()).getTime()/ 1000));
        bean.setEnd_time_span( String.valueOf(TimeUtils.formatTime2Date(null,bean.getEnd_time()).getTime()/ 1000));
        bean.setNotes("1.考试总题目数：10道；\n2.考试开始时间：2019-09-10 08:00:00;\n3.考试结束时间：2019-09-06 10:30:00;\n4.考试时间到系统自动交卷，请考生合理分配时间。");

        List<QuestionsBean.DataBean> list = new ArrayList<>();
        QuestionsBean.DataBean dataBean1 = new QuestionsBean.DataBean();
        dataBean1.setId("184");
        dataBean1.setType(0);
        dataBean1.setContent("对登记后上道路行驶的机动车，应当依照法律、行政法规的规定，根据车辆用途、载客载货数量、___ 等不同情况，定期进行安全技术检验。");
        dataBean1.setOption(Arrays.asList("A、使用次数", "B、使用年限", "C、行驶里程", "D、维修次数"));
        dataBean1.setAnswer("B");
        dataBean1.setGrade(1);
        dataBean1.setSequence(1);

        QuestionsBean.DataBean dataBean2 = new QuestionsBean.DataBean();
        dataBean2.setId("313");
        dataBean2.setType(1);
        dataBean2.setContent("公安机关交通管理部门应当依法对机动车安全技术检验机构出具虚假检验结果情况进行监督管理。");
        dataBean2.setOption(null);
        dataBean2.setAnswer("正确");
        dataBean2.setGrade(2);
        dataBean2.setSequence(2);

        QuestionsBean.DataBean dataBean3 = new QuestionsBean.DataBean();
        dataBean3.setId("111");
        dataBean3.setType(2);
        dataBean3.setContent("___应当按照国家机动车安全技术检验标准对机动车进行检验，对检验结果承担法律责任。");
        dataBean3.setOption(Arrays.asList("A、公安车辆管理机关交通管理部门", "B、机动车安全技术检验机构", "C、质量技术监督部门", "D、车辆维修保养企业"));
        dataBean3.setAnswer("BD");
        dataBean3.setGrade(1);
        dataBean3.setSequence(3);

        list.add(dataBean1);
        list.add(dataBean2);
        list.add(dataBean3);
        bean.setData(list);
        bean.setTotal_num(list.size()+"");

        return bean;
    }
}
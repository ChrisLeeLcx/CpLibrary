package com.lee.demo.ui.activity;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lee.demo.R;
import com.lee.demo.base.BaseActivity;
import com.lee.demo.model.SectionUtil;
import com.lee.demo.model.UtilBean;
import com.lee.demo.ui.adapter.UtilSectionAdapter;

import java.util.ArrayList;
import java.util.List;

import cn.lee.cplibrary.util.system.AppUtils;
import cn.lee.cplibrary.util.ObjectUtils;
import cn.lee.cplibrary.util.ScreenUtil;
import cn.lee.cplibrary.util.StringUtil;
import cn.lee.cplibrary.util.URLUtil;
import cn.lee.cplibrary.util.WifiConnUtils;

public class UtilActivity extends BaseActivity {

    private RecyclerView mRecyclerView;
    private List<SectionUtil> mData;

    @Override
    protected BaseActivity getSelfActivity() {
        return this;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_rv;
    }

    @Override
    public String getPagerTitle() {
        return null;
    }

    @Override
    public String getPagerRight() {
        return null;
    }

    @Override
    public void initView() {
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mData = getData();
        UtilSectionAdapter sectionAdapter = new UtilSectionAdapter(getSelfActivity(), R.layout.item_section_content, R.layout.def_section_head, mData);


        sectionAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
            }
        });
        mRecyclerView.setAdapter(sectionAdapter);


    }

    @Override
    protected void initData() {

    }


    public List<SectionUtil> getData() {
        List<SectionUtil> mData = new ArrayList<>();
        mData.add(new SectionUtil(true, "StringUtil"));
        mData.add(new SectionUtil(new UtilBean("Str2Int", "" + StringUtil.String2Int("100"))));
        mData.add(new SectionUtil(new UtilBean("checkNameChese\n是否汉字", "" + StringUtil.checkNameChese("是否汉字"))));
        mData.add(new SectionUtil(new UtilBean("parseChineseNumber\n0~9999汉字转化成数字", "" + StringUtil.parseChineseNumber("三千四百五十六"))));
        mData.add(new SectionUtil(new UtilBean("formatMoney\n格式化金额", "" + StringUtil.formatMoney(.12f))));
        mData.add(new SectionUtil(new UtilBean("formatMoneyWithoutZero\n格式化金额2", "" + StringUtil.formatMoneyWithoutZero(.055f))));
        mData.add(new SectionUtil(new UtilBean("isHttpUrl\n判断isHttpUrl", "" + StringUtil.isHttpUrl("https://home.firefoxchina.cn/"))));
        mData.add(new SectionUtil(new UtilBean("removeWWWHead\n过滤WWW", "" + StringUtil.removeWWWHead("https://www.baidu.com/"))));
        mData.add(new SectionUtil(new UtilBean("getDomainName\n获取域名", "" + StringUtil.getDomainName("https://home.firefoxchina.cn/"))));
        mData.add(new SectionUtil(new UtilBean("isWebUrl", "" + StringUtil.isWebUrl("https://home.firefoxchina.cn/"))));
        mData.add(new SectionUtil(new UtilBean("isVaildEmail\n是否是邮件地址", "" + StringUtil.isVaildEmail("2272143930@qq.com"))));
//         mData.add(new SectionUtil(new UtilBean("",""+ StringUtil.filterInfo())));
        mData.add(new SectionUtil(new UtilBean("formatChar\n格式化保留字符串中的数字字母", "" + StringUtil.formatChar("abc12A#@"))));
//         mData.add(new SectionUtil(new UtilBean("",""+ StringUtil.isHexWepKey())));
        mData.add(new SectionUtil(new UtilBean("isHex\n是否是十六进制", "" + StringUtil.isHex("20f2"))));
//         mData.add(new SectionUtil(new UtilBean("",""+ StringUtil.toHex(new Byte{125b}))));
        mData.add(new SectionUtil(new UtilBean("toByte\n16进制转成byte数组", "" + StringUtil.toByte("20f2"))));
        mData.add(new SectionUtil(new UtilBean("toQuotedString\n给字符串加双引号", "" + StringUtil.toQuotedString("ChrisLee"))));
        mData.add(new SectionUtil(new UtilBean("urlEncode\n格式化地址空格", "" + StringUtil.urlEncode("https:// home. firefoxchina .cn/"))));
        mData.add(new SectionUtil(new UtilBean("htmlEncode\n将字符串格式化成html语言", "" + StringUtil.htmlEncode("abc<we&"))));
        mData.add(new SectionUtil(new UtilBean("equalsString\n比较字符串", "" + StringUtil.equalsString("12", "12"))));
        mData.add(new SectionUtil(new UtilBean("compareVerString\n比较app版本号大小", "" + StringUtil.compareVerString("2.5.3.5", "2.5.4"))));
        mData.add(new SectionUtil(new UtilBean("UUID", "" + StringUtil.getUUID())));
        mData.add(new SectionUtil(new UtilBean("StringUtil.sort", "按照数字 、字母、中文顺序排序")));
        mData.add(new SectionUtil(new UtilBean("hideMiddleFourNumber\n隐藏手机号中间4位", "" + StringUtil.hideMiddleFourNumber("15568741236"))));
        mData.add(new SectionUtil(new UtilBean("getHideBankCardNum\n隐藏银行卡中间数字", "" + StringUtil.getHideBankCardNum("321665475556689234"))));
        mData.add(new SectionUtil(new UtilBean("getCountStr\n字符串2在字符串1中出现的次数", "" + StringUtil.getCountStr("abcfgabcerfafgacb", "abc"))));
        WifiConnUtils wifiConnUtils = new WifiConnUtils(getSelfActivity());
        mData.add(new SectionUtil(true, "WifiConnUtils"));
        mData.add(new SectionUtil(new UtilBean("getConnectedMacAddr\n", "" + wifiConnUtils.getConnectedMacAddr())));
        mData.add(new SectionUtil(new UtilBean("getConnectedIPAddr\n", "" + wifiConnUtils.getConnectedIPAddr(getSelfActivity()))));
        mData.add(new SectionUtil(new UtilBean("getConnectedID\n", "" + wifiConnUtils.getConnectedID())));
        mData.add(new SectionUtil(new UtilBean("isNetworkConnected\n", "" + wifiConnUtils.isNetworkConnected(getSelfActivity()))));
        mData.add(new SectionUtil(new UtilBean("isWifiConnected\n", "" + wifiConnUtils.isWifiConnected(getSelfActivity()))));
        mData.add(new SectionUtil(new UtilBean("isWifiOpen\n", "" + wifiConnUtils.isWifiOpen(getSelfActivity()))));
        mData.add(new SectionUtil(new UtilBean("getRSSI\n", "" + wifiConnUtils.getRSSI(getSelfActivity()))));
        mData.add(new SectionUtil(new UtilBean("getCurrConnectWifiSSID\n", "" + wifiConnUtils.getCurrConnectWifiSSID(getSelfActivity()))));
        mData.add(new SectionUtil(new UtilBean("isNetworkGprs\n", "" + wifiConnUtils.isNetworkGprs(getSelfActivity()))));
        mData.add(new SectionUtil(new UtilBean("isNetwork3G\n", "" + wifiConnUtils.isNetwork3G(getSelfActivity()))));
        mData.add(new SectionUtil(new UtilBean("getNetworkType\n", "" + wifiConnUtils.getNetworkType(getSelfActivity()))));
        mData.add(new SectionUtil(new UtilBean("getNetworkName\n", "" + wifiConnUtils.getNetworkName(getSelfActivity()))));
        mData.add(new SectionUtil(true, "URLUtil"));
        mData.add(new SectionUtil(new UtilBean("URLRequest\n获取URL地址参数部分键值对", "" + URLUtil.URLRequest("http://appnj.jdcdj.cn/api/app_get_country?appid=app&timestamp=158&sign=E41A8&nonce=0.704"))));
        mData.add(new SectionUtil(true, "SpanUtils"));
        mData.add(new SectionUtil(new UtilBean("SpanUtils\n", "")));
        mData.add(new SectionUtil(new UtilBean("span案例\n", "")));
        mData.add(new SectionUtil(true, "ScreenUtil"));
        mData.add(new SectionUtil(new UtilBean("getScreenWidth\n", ""+ ScreenUtil.getScreenWidth(getSelfActivity()))));
        mData.add(new SectionUtil(new UtilBean("dp2px\n", "")));
        mData.add(new SectionUtil(new UtilBean("px2dp\n", "")));
        mData.add(new SectionUtil(new UtilBean("px2sp\n", "")));
        mData.add(new SectionUtil(new UtilBean("sp2px\n", "")));
        mData.add(new SectionUtil(new UtilBean("sp\n将float的sp值转换成int的sp值", "")));
        mData.add(new SectionUtil(new UtilBean("getLocationOnScreenX\n某个View距离页面左端距离,以页面为参照", ""+ScreenUtil.getLocationOnScreenX(mRecyclerView))));
        mData.add(new SectionUtil(new UtilBean("getLocationOnScreenY\n某个View距离页面顶端距离,以页面屏幕为参照", ""+ScreenUtil.getLocationOnScreenY(mRecyclerView))));
        mData.add(new SectionUtil(new UtilBean("getLocationInWindowX\n某个View距离页面左端距离,以屏幕为参照", ""+ScreenUtil.getLocationInWindowX(mRecyclerView))));
        mData.add(new SectionUtil(new UtilBean("getLocationInWindowY\n某个View距离页面顶端距离,以屏幕为参照", ""+ScreenUtil.getLocationInWindowY(mRecyclerView))));
        mData.add(new SectionUtil(new UtilBean("rotate\n顺时针旋转View", "")));
        mData.add(new SectionUtil(new UtilBean("setActivityPortrait\n设置Activity竖屏", "")));
        mData.add(new SectionUtil(new UtilBean("setActivityLandscape\n设置Activity横屏", "")));
        mData.add(new SectionUtil(true, "ObjectUtils"));
        mData.add(new SectionUtil(new UtilBean("isNull\n", "")));
        mData.add(new SectionUtil(new UtilBean("isEmpty\n", "")));
        mData.add(new SectionUtil(new UtilBean("hasNull\n", "")));
        mData.add(new SectionUtil(new UtilBean("hasNotNull\n", "")));
        mData.add(new SectionUtil(new UtilBean("hasEmpty\n", "")));
        mData.add(new SectionUtil(new UtilBean("hasNotEmpty\n", "")));
        mData.add(new SectionUtil(new UtilBean("formatStr\n若字符串是null或\"\",直接返回空字符,", "")));
        mData.add(new SectionUtil(new UtilBean("string2Int\n处理了异常", "")));
        mData.add(new SectionUtil(new UtilBean("string2Long\n", "")));
        mData.add(new SectionUtil(new UtilBean("string2Float\n", "")));
        mData.add(new SectionUtil(new UtilBean("string2Double\n", "")));
        mData.add(new SectionUtil(new UtilBean("string2Boolean\n", "")));
        mData.add(new SectionUtil(new UtilBean("string2Byte\n", "")));
        mData.add(new SectionUtil(new UtilBean("object2String\n", "")));
        mData.add(new SectionUtil(new UtilBean("decimal\n格式化double数据，保留指定位数小数", "")));
        mData.add(new SectionUtil(new UtilBean("decimal2String\n", "")));
        mData.add(new SectionUtil(new UtilBean("digitConvert\n阿拉伯数字转化成汉字", ObjectUtils.digitConvert(3600000))));
        mData.add(new SectionUtil(true, "NumberValidationUtils"));
        mData.add(new SectionUtil(new UtilBean("isPositiveInteger\n是否是正整数", "")));
        mData.add(new SectionUtil(new UtilBean("isNegativeInteger\n是否是负整数", "")));
        mData.add(new SectionUtil(new UtilBean("isWholeNumber\n是否是正或负整数", "")));
        mData.add(new SectionUtil(new UtilBean("isPositiveDecimal\n是否是正小数", "")));
        mData.add(new SectionUtil(new UtilBean("isNegativeDecimal\n是否是负小数", "")));
        mData.add(new SectionUtil(new UtilBean("isDecimal\n是否是正或负小数", "")));
        mData.add(new SectionUtil(new UtilBean("isRealNumber\n是否实数", "")));
        mData.add(new SectionUtil(new UtilBean("isPositiveRealNumber\n是否正实数", "")));
        mData.add(new SectionUtil(true, "AppUtils"));
        mData.add(new SectionUtil(new UtilBean("getVersionCode\n", ""+ AppUtils.getVersionCode(getSelfActivity()))));
        mData.add(new SectionUtil(new UtilBean("getVersionName\n", ""+AppUtils.getVersionName(getSelfActivity()))));
        mData.add(new SectionUtil(new UtilBean("getScale\n获取屏幕密度", ""+AppUtils.getScale(getSelfActivity()))));
        mData.add(new SectionUtil(new UtilBean("getImsiIMSI(国际移动用户识别码)\n", ""+AppUtils.getImsi(getSelfActivity()))));
        mData.add(new SectionUtil(new UtilBean("Imei/DeviceId/设备串号\n", ""+AppUtils.getImei(getSelfActivity()))));
        mData.add(new SectionUtil(new UtilBean("getImsiPhone/手机号\n", ""+AppUtils.getImsiPhone(getSelfActivity()))));
        mData.add(new SectionUtil(new UtilBean("getPhoneModel\n", ""+AppUtils.getPhoneModel())));
        mData.add(new SectionUtil(new UtilBean("getPhoneBrand\n", ""+AppUtils.getPhoneBrand())));
        mData.add(new SectionUtil(new UtilBean("getSystemVersion\n", ""+AppUtils.getSystemVersion())));
        mData.add(new SectionUtil(new UtilBean("getMacAddress\n", ""+AppUtils.getMacAddress(getSelfActivity()))));
        mData.add(new SectionUtil(new UtilBean("getAvailMemory\n", ""+AppUtils.getAvailMemory(getSelfActivity()))));
        mData.add(new SectionUtil(new UtilBean("getTotalMemory\n", ""+AppUtils.getTotalMemory(getSelfActivity()))));
        mData.add(new SectionUtil(new UtilBean("getCpuInfo\n", ""+AppUtils.getCpuInfo())));
        mData.add(new SectionUtil(new UtilBean("getAppId\n", ""+AppUtils.getAppId(getSelfActivity()))));
        mData.add(new SectionUtil(new UtilBean("closeKeyboard\n关闭软键盘", "")));
        mData.add(new SectionUtil(new UtilBean("jumpAppSettingInfo\n跳转到App设置页面方法1", "")));
        mData.add(new SectionUtil(new UtilBean("jumpAppSettingInfo\n跳转到App设置页面方法2", "")));
        mData.add(new SectionUtil(new UtilBean("openGpsSettings\n打开App定位服务设置页面", "")));
        mData.add(new SectionUtil(new UtilBean("jumpSetting\n跳转到系统设置页面", "")));
        mData.add(new SectionUtil(new UtilBean("jumpWifi\n跳转到Wifi设置页面", "")));
        mData.add(new SectionUtil(new UtilBean("jumpMobileNet\n跳转到移动网络设置界面", "")));
        mData.add(new SectionUtil(new UtilBean("getActivity\n根据Object获取Activity（object原本是activity或fragment）", "")));
        mData.add(new SectionUtil(new UtilBean("isLocationEnabled\n判断定位服务是否开启", ""+AppUtils.isLocationEnabled(getSelfActivity()))));
        mData.add(new SectionUtil(new UtilBean("checkLocationEnabled\n判断定位服务是否开启", ""+AppUtils.checkLocationEnabled(getSelfActivity()))));
        mData.add(new SectionUtil(new UtilBean("isCoverInstall\n", ""+AppUtils.isCoverInstall(getSelfActivity()))));

        return mData;
    }


}

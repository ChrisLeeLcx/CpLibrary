package cn.lee.cplibrary.util;

import java.util.ArrayList;
import java.util.List;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.WifiLock;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import cn.lee.cplibrary.util.net.SecurityType;

/**
 * wifi信息工具类：功能：检查WIFI状态 开启WIFI 1、关闭WIFI 2、扫描wifi 3、得到Scan结果 4 、 Scan结果转为Sting 5
 * 、 得到Wifi配置好的信息 6 、判定指定WIFI是否已经配置好,依据WIFI的地址BSSID,返回NetId
 * 
 * 7、添加指定WIFI的配置信息,原列表不存在此SSID 8、连接指定Id的WIFI 9、创建一个WIFILock 10、锁定wifilock 11、
 * 解锁WIFI 12、 得到建立连接的信息 13 、 得到连接的MAC地址 14、 得到连接的名称SSID 15 、 得到连接的IP地址 16 、
 * 得到连接的ID 17 、 判断网络是否连接 18、判断Wifi网络是否连接 19、判断设置中心是否连接的WifiBao网络
 * 20、检查网络是否连接,没连接，Toast提示  21、 确认WiFi服务是关闭且不在开启运行中 
 * 
 * @author sunny
 * 
 */
public class WifiConnUtils {
	private WifiManager localWifiManager;// 提供Wifi管理的各种主要API，主要包含wifi的扫描、建立连接、配置信息等
	/**
	 * ScanResult用来描述已经检测出的接入点，包括接入的地址、名称、身份认证、频率、信号强度等,
	 * WIFIConfiguration描述WIFI的链接信息，包括SSID、SSID隐藏、password等的设置
	 **/
	private List<WifiConfiguration> wifiConfigList;//
	private WifiInfo wifiConnectedInfo;// 已经建立好网络链接的信息
	private WifiLock wifiLock;// 手机锁屏后，阻止WIFI也进入睡眠状态及WIFI的关闭
	private static ConnectivityManager mConnectivityManager;
	private WifiInfo wifiInfo;
	public static final String TAG = "WifiConnUtils";

	public WifiConnUtils(Context context) {
		localWifiManager = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		mConnectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		wifiInfo = localWifiManager.getConnectionInfo();
		getConfiguration();
	}

	// 检查WIFI状态
	public int WifiCheckState() {
		return localWifiManager.getWifiState();
	}

	// 开启WIFI
	public void WifiOpen() {
		if (!localWifiManager.isWifiEnabled()) {
			localWifiManager.setWifiEnabled(true);
		}
	}

	// 关闭WIFI
	public void WifiClose() {
		if (!localWifiManager.isWifiEnabled()) {
			localWifiManager.setWifiEnabled(false);
		}
	}

	// 扫描wifi
	public void WifiStartScan() {
		localWifiManager.startScan();
	}

	// 得到Scan结果
	public List<ScanResult> getScanResults() {
		localWifiManager.startScan();
		return localWifiManager.getScanResults();// 得到扫描结果
	}

	// Scan结果转为Sting
	public List<String> scanResultToString(List<ScanResult> list) {
		List<String> strReturnList = new ArrayList<String>();
		for (int i = 0; i < list.size(); i++) {
			ScanResult strScan = list.get(i);
			String str = strScan.toString();
			boolean bool = strReturnList.add(str);
			if (!bool) {
				Log.i("scanResultToSting", "Addfail");
			}
		}
		return strReturnList;
	}

	// 得到Wifi配置好的信息
	public void getConfiguration() {
		wifiConfigList = localWifiManager.getConfiguredNetworks();// 得到配置好的网络信息
		if (wifiConfigList != null && wifiConfigList.size() > 0) {
			for (int i = 0; i < wifiConfigList.size(); i++) {
				// Log.i("getConfiguration", wifiConfigList.get(i).SSID);
				// Log.i("getConfiguration",
				// String.valueOf(wifiConfigList.get(i).networkId));
			}
		}
	}

	// 判定指定WIFI是否已经配置好,依据WIFI的地址BSSID,返回NetId
	public int IsConfiguration(String SSID) {
		Log.i("IsConfiguration", String.valueOf(wifiConfigList.size()));
		for (int i = 0; i < wifiConfigList.size(); i++) {
			Log.i(wifiConfigList.get(i).SSID,
					String.valueOf(wifiConfigList.get(i).networkId));
			if (wifiConfigList.get(i).SSID.equals(SSID)) {// 地址相同
				return wifiConfigList.get(i).networkId;
			}
		}
		return -1;
	}

	/**
	 * 判断wifi是否有密码
	 * 
	 * @param scanResult
	 */
	public boolean isWifiNoPsw(ScanResult scanResult) {
		boolean isNoPsw = getSecurityByCapabilities(scanResult.capabilities) == SecurityType.OPEN ? true
				: false;
		LogUtil.i(LogUtil.TAG, "-4,isNoPsw=" + isNoPsw);
		return isNoPsw;
	}

	// 添加指定WIFI的配置信息,原列表不存在此SSID(无密码)
	public int AddWifiConfig(List<ScanResult> wifiList, String ssid,
			String Password) {
		int Type = 0;
		int wifiId = -1;
		for (int i = 0; i < wifiList.size(); i++) {
			ScanResult wifi = wifiList.get(i);
			if (wifi.SSID.equals(ssid)) {
				String capabilities = wifi.capabilities;
				// 判断路由器加密类型
				if (!TextUtils.isEmpty(capabilities)) {
					if (capabilities.contains("WPA")
							|| capabilities.contains("wpa")) {
						// samehopeA:[WPA-PSK-CCMP][WPA2-PSK-CCMP][ESS]
						Type = 1;
						LogUtil.i(LogUtil.TAG, "-capabilities1=" + capabilities);
					} else if (capabilities.contains("WEP")
							|| capabilities.contains("wep")) {
						Type = 2;
						LogUtil.i(LogUtil.TAG, "-capabilities2=" + capabilities);
					} else {
						Type = 0;
						LogUtil.i(LogUtil.TAG, "-capabilities0=" + capabilities);
					}
				}
				WifiConfiguration wifiCong = new WifiConfiguration();
				wifiCong.allowedAuthAlgorithms.clear();
				wifiCong.allowedGroupCiphers.clear();
				wifiCong.allowedKeyManagement.clear();
				wifiCong.allowedPairwiseCiphers.clear();
				wifiCong.allowedProtocols.clear();
				wifiCong.SSID = "\"" + wifi.SSID + "\"";// \"转义字符，代表"

				WifiConfiguration tempConfig = this.IsExsits(wifi.SSID);
				if (tempConfig != null) {
					localWifiManager.removeNetwork(tempConfig.networkId);
				}

				if (Type == 0) // 没有密码的情况
				{
					wifiCong.wepKeys[0] = "\"" + Password + "\"";
					wifiCong.allowedKeyManagement
							.set(WifiConfiguration.KeyMgmt.NONE);
					wifiCong.wepTxKeyIndex = 0;
				}
				if (Type == 1) // WPA加密的情况
				{
					wifiCong.preSharedKey = "\"" + Password + "\"";
					wifiCong.hiddenSSID = true;
					wifiCong.allowedAuthAlgorithms
							.set(WifiConfiguration.AuthAlgorithm.OPEN);
					wifiCong.allowedGroupCiphers
							.set(WifiConfiguration.GroupCipher.TKIP);
					wifiCong.allowedKeyManagement
							.set(WifiConfiguration.KeyMgmt.WPA_PSK);
					wifiCong.allowedPairwiseCiphers
							.set(WifiConfiguration.PairwiseCipher.TKIP);
					// config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
					wifiCong.allowedGroupCiphers
							.set(WifiConfiguration.GroupCipher.CCMP);
					wifiCong.allowedPairwiseCiphers
							.set(WifiConfiguration.PairwiseCipher.CCMP);
					wifiCong.status = WifiConfiguration.Status.ENABLED;
				}
				if (Type == 2) // WEP加密的情况
				{
					wifiCong.hiddenSSID = true;
					wifiCong.wepKeys[0] = "\"" + Password + "\"";
					wifiCong.allowedAuthAlgorithms
							.set(WifiConfiguration.AuthAlgorithm.SHARED);
					wifiCong.allowedGroupCiphers
							.set(WifiConfiguration.GroupCipher.CCMP);
					wifiCong.allowedGroupCiphers
							.set(WifiConfiguration.GroupCipher.TKIP);
					wifiCong.allowedGroupCiphers
							.set(WifiConfiguration.GroupCipher.WEP40);
					wifiCong.allowedGroupCiphers
							.set(WifiConfiguration.GroupCipher.WEP104);

					wifiCong.allowedKeyManagement
							.set(WifiConfiguration.KeyMgmt.NONE);
					wifiCong.wepTxKeyIndex = 0;
				}

				wifiId = localWifiManager.addNetwork(wifiCong);// 将配置好的特定WIFI密码信息添加,添加完成后默认是不激活状态，成功返回ID，否则为-1
				if (wifiId != -1) {
					return wifiId;
				}
			}
		}
		return wifiId;
	}

	private WifiConfiguration IsExsits(String SSID) {
		List<WifiConfiguration> existingConfigs = localWifiManager
				.getConfiguredNetworks();
		for (WifiConfiguration existingConfig : existingConfigs) {
			if (existingConfig.SSID.equals("\"" + SSID + "\"")) {
				return existingConfig;
			}
		}
		return null;
	}

	// 连接指定Id的WIFI
	public boolean ConnectWifi(int wifiId) {
		for (int i = 0; i < wifiConfigList.size(); i++) {
			WifiConfiguration wifi = wifiConfigList.get(i);
			if (wifi.networkId == wifiId) {
				while (!(localWifiManager.enableNetwork(wifiId, true))) {// 激活该Id，建立连接
					Log.i("ConnectWifi",
							String.valueOf(wifiConfigList.get(wifiId).status));// status:0--已经连接，1--不可连接，2--可以连接
				}
				if (localWifiManager.enableNetwork(wifiId, true)) {
					return true;
				}
			}
		}
		return false;
	}

	// 创建一个WIFILock
	public void createWifiLock(String lockName) {
		wifiLock = localWifiManager.createWifiLock(lockName);
	}

	// 锁定wifilock
	public void acquireWifiLock() {
		wifiLock.acquire();
	}

	// 解锁WIFI
	public void releaseWifiLock() {
		if (wifiLock.isHeld()) {// 判定是否锁定
			wifiLock.release();
		}
	}

	// 得到建立连接的信息
	public void getConnectedInfo() {
		wifiConnectedInfo = localWifiManager.getConnectionInfo();
	}

	// 得到连接的MAC地址
	public String getConnectedMacAddr() {
		wifiConnectedInfo = localWifiManager.getConnectionInfo();
		return (wifiConnectedInfo == null) ? "NULL" : wifiConnectedInfo
				.getMacAddress();
	}

	// 得到连接的名称SSID
	public String getConnectedSSID() {
		wifiConnectedInfo = localWifiManager.getConnectionInfo();
		int deviceVersion = Build.VERSION.SDK_INT;
		String ssid = wifiConnectedInfo.getSSID();
		if (deviceVersion >= 17) {
			if (ssid.startsWith("\"") && ssid.endsWith("\"")) {

				ssid = ssid.substring(1, ssid.length() - 1);
			}
		}
		return (wifiConnectedInfo == null) ? "NULL" : ssid;
	}

	// 得到连接的名称SSID
	public String getConnectedSSID1() {
		return (wifiInfo == null) ? "NULL" : wifiInfo.getSSID();
	}

	// 得到连接的IP地址
	public static int getConnectedIPAddr(Context mContext) {
		WifiManager manager = (WifiManager) mContext
				.getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = manager.getConnectionInfo();
		;// 已经建立好网络链接的信息
		return (info == null) ? 0 : info.getIpAddress();
	}

	// 得到连接的ID
	public int getConnectedID() {
		return (wifiConnectedInfo == null) ? 0 : wifiConnectedInfo
				.getNetworkId();
	}

	// 判断网络是否连接
	public static boolean isNetworkConnected(Context context) {
		mConnectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (context != null) {
			NetworkInfo mNetworkInfo = mConnectivityManager
					.getActiveNetworkInfo();
			if (mNetworkInfo != null) {
				return mNetworkInfo.isAvailable();
			}
		}
		return false;
	}

	/**
	 * 判断Wifi网络是否连接 
	 * 
	 * @param context
	 * @return true 可用， false不可用
	 */
	public static boolean isWifiConnected(Context context) {
		if (context == null) {
			return false;
		}
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getApplicationContext().getSystemService(
						Context.CONNECTIVITY_SERVICE);

		if (connectivity == null) {
			return false;
		}
		NetworkInfo info = connectivity.getActiveNetworkInfo();
		if (info == null) {
			return false;
		}
		if (info.isConnected()
				&& info.getType() == ConnectivityManager.TYPE_WIFI) {
			return true;
		}
		return false;
	}

	/**
	 * 确认WiFi服务是关闭且不在开启运行中 
	 * 
	 * @param context
	 * @return true 可用， false不可用
	 */
	public static boolean isWifiOpen(Context context) {
		/* 取得WifiManager与LocationManager */
		WifiManager wManager = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);

		/* 确认WiFi服务是关闭且不在开启运行中 */
		if (!wManager.isWifiEnabled()
				&& wManager.getWifiState() != WifiManager.WIFI_STATE_ENABLING) {
			return false;
		} else {
			return true;
		}
	}

	public static int getRSSI(Context context) {
		WifiManager manager = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		if (manager == null) {
			return 0;
		}
		WifiInfo info = manager.getConnectionInfo();
		if (info == null) {
			return 0;
		}
		return info.getRssi();
	}

	/**
	 * 获取当前已连接的wifi热点的名称
	 * 
	 * @param ctx
	 * @return
	 */
	public static String getCurrConnectWifiSSID(Context ctx) {
		if (ctx == null) {
			return null;
		}
		WifiManager wifiManager = (WifiManager) ctx
				.getSystemService(Context.WIFI_SERVICE);
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		if (wifiInfo != null) {
			String ssid = wifiInfo.getSSID();
			int deviceVersion = Build.VERSION.SDK_INT;
			if (deviceVersion >= 17) {
				if (ssid.startsWith("\"") && ssid.endsWith("\"")) {

					ssid = ssid.substring(1, ssid.length() - 1);
				}
			}
			return ssid;
		}
		return null;
	}

	/**
	 * 判断是否GPRS
	 * 
	 * @param context
	 * @return true表示是GPRS，否则不是GPRS
	 */
	public static boolean isNetworkGprs(Context context) {
		TelephonyManager telMgr = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		if (telMgr == null) {
			return false;
		}
		/*
		 * telMgr.getNetworkCountryIso();//获取电信网络国别
		 * telMgr.getPhoneType();//获得行动通信类型 telMgr.getNetworkType();//获得网络类型
		 */
		// Log.i(TAG, "NetworkType" + telMgr.getNetworkType());
		int networkType = telMgr.getNetworkType();
		if (networkType == TelephonyManager.NETWORK_TYPE_GPRS
				|| networkType == TelephonyManager.NETWORK_TYPE_EDGE) {
			return true;
		}
		return false;
	}

	/**
	 * 判断是否3G网络
	 * 
	 * @param context
	 * @return true表示是3G，否则不是3G
	 */
	public static boolean isNetwork3G(Context context) {
		TelephonyManager telMgr = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		if (telMgr == null) {
			return false;
		}
		int networkYype = telMgr.getNetworkType();
		// NETWORK_TYPE_EDGE 是为GPRS到第三代移动通信的过渡性技术方案(GPRS俗称2.5G， EDGE俗称2.75G）
		if (networkYype != TelephonyManager.NETWORK_TYPE_GPRS
				&& networkYype != TelephonyManager.NETWORK_TYPE_UNKNOWN
				&& networkYype != TelephonyManager.NETWORK_TYPE_EDGE) {
			return true;
		}
		return false;
	}

	public static final int NETWORK_WIFI = 1;
	public static final int NETWORK_3G = 2;
	public static final int NETWORK_2G = 3;
	public static final int NETWORK_NULL = 4;

	/**
	 * 判断网络连接的类型：
	 * 
	 * @param context
	 * @return
	 */
	public static int getNetworkType(Context context) {
		if (context == null) {
			return NETWORK_NULL;
		}
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getApplicationContext().getSystemService(
						Context.CONNECTIVITY_SERVICE);
		if (connectivity == null) {
			return NETWORK_NULL;
		}
		NetworkInfo info = connectivity.getActiveNetworkInfo();
		if (info == null) {
			return NETWORK_NULL;
		}
		if (info.isConnected()) {
			final int type = info.getType();
			if (type == ConnectivityManager.TYPE_WIFI) {
				return NETWORK_WIFI;
			} else if (type == ConnectivityManager.TYPE_MOBILE) {
				if (isNetworkGprs(context)) {
					return NETWORK_2G;
				} else if (isNetwork3G(context)) {
					return NETWORK_3G;
				}
			}
		}
		return NETWORK_NULL;
	}

	/**
	 * function:
	 * 
	 * @param context
	 * @return
	 * @author: ChrisLee at 2016-8-17
	 */
	public static String getNetworkName(Context context) {
		int type = getNetworkType(context);
		String netWorkName = "";
		switch (type) {
		case NETWORK_NULL:
			netWorkName = "未检测到网络";
			break;
		case NETWORK_WIFI:
			netWorkName = getCurrConnectWifiSSID(context);
			break;
		case NETWORK_2G:
			netWorkName = "有线网络";
			break;
		case NETWORK_3G:
			netWorkName = "有线网络";
			break;
		default:
			netWorkName = "有线网络";
			break;
		}
		return netWorkName;
	}


	public static SecurityType getSecurityByCapabilities(String c) {
		if (c != null) {
			SecurityType[] securitys = new SecurityType[] { SecurityType.PSK,
					SecurityType.EAP, SecurityType.WEP };
			for (int i = 0; i < securitys.length; i++) {
				if (c.contains(securitys[i].toString())) {
					return securitys[i];
				}
			}
		}
		return SecurityType.OPEN;
	}

	// 根据Android的版本判断获取到的SSID是否有双引号(4.2以上有，4.2以下没有)
	public String whetherToRemoveTheDoubleQuotationMarks(String ssid) {
		// 获取Android版本号
		int deviceVersion = Build.VERSION.SDK_INT;
		if (deviceVersion >= 17) {
			if (ssid.startsWith("\"") && ssid.endsWith("\"")) {
				ssid = ssid.substring(1, ssid.length() - 1);
			}
		}
		return ssid;
	}



}

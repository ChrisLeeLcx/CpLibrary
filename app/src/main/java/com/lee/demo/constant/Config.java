package com.lee.demo.constant;

/**
 * 整个工程的配置文件
 * @author: ChrisLee
 * @time: 2018/7/27
 */

public class Config {
    public static final boolean isDebug = true;//是否开启debug
    public static final Environment CURRENT_ENVIRONMENT = Environment.LOCAL;//项目当前服务器环境

    public static final String SMS_URI_ALL = "content://sms/"; //所有短信
    public static final String SMS_URI_INBOX = "content://sms/inbox"; // 收件箱
    public static  final String SMS_URI_SEND = "content://sms/sent"; //已发送
    public static   final String SMS_URI_DRAFT = "content://sms/draft"; //草稿
    public static  final String SMS_URI_OUTBOX = "content://sms/outbox"; //发件箱
    public static   final String SMS_URI_FAILED = "content://sms/failed";//发送失败
    public static   final String SMS_URI_QUEUED = "content://sms/queued"; //待发送列表
}

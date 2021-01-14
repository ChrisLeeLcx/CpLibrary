package cn.lee.cplibrary.util.video;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import java.util.List;

import cn.lee.cplibrary.R;
import cn.lee.cplibrary.util.ToastUtil;
import cn.lee.cplibrary.util.dialog.CpComDialog;
import cn.lee.cplibrary.util.permissionutil.PermissionProxy;
import cn.lee.cplibrary.util.permissionutil.PermissionUtil;
import cn.lee.cplibrary.util.system.AppUtils;
import cn.lee.cplibrary.util.timer.TimeUtils;

/**
 * 获取拍摄短视频或者相册中的视频
 */
public class CpVideoDialog {

    private Activity activity;
    public static boolean isShowGuideDialog = true;//用户永久拒绝需要的权限时，是否显示引导对话框，使用时候可自行设置
    public static final int REQUEST_CODE_FOR_RECORD = 5230;//录制视频请求码
    public static final int REQUEST_CODE_FOR_COMPRESS = 5231;//压缩视频请求码
    public static final int REQUEST_FOR_VIDEO_FILE = 5232;//选择文件中视频请求吗
    private int duration = -1;//录制视频的时长，-1表示不限制时长 ，单位ms
    private int quality = VideoRecordActivity.Q1080;//录制视频的分辨率

    /***********录像权限相关开始***********/
    private static PermissionUtil permissionUtil;
    public static final int REQUEST_CODE_PER = 500;//权限请求码
    private static final String[] permissionArray = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,//写
            Manifest.permission.READ_EXTERNAL_STORAGE,//读
            Manifest.permission.RECORD_AUDIO,//录制音频权限
            Manifest.permission.CAMERA,//相机权限
    };

    //录制,相册
    public enum VideoVersion {
        record, album
    }

    public CpVideoDialog(Activity activity) {
        this.activity = activity;
    }

    /**
     * 注意：打开上传图片Dialog的Activity
     * 必须重写onActivityResult、 onRequestPermissionsResult,permissionUtil.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
     */
    public Dialog showChooseDialog() {
        View view = LayoutInflater.from(activity).inflate(R.layout.cp_dialog_pic_choose, null);
        final Dialog picDialog = CpComDialog.getBottomDialog(activity, true, view);
        Button btnAlbum = view.findViewById(R.id.photoAlbum);
        Button btnRecord = view.findViewById(R.id.photograph);
        btnAlbum.setText("打开相册");
        btnRecord.setText("拍摄视频");
        //相册
        btnAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                picDialog.dismiss();
                requestPermission(activity, quality, VideoVersion.album);
            }
        });
        //拍摄
        btnRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                picDialog.dismiss();
                requestPermission(activity, quality, VideoVersion.record);
            }
        });

        view.findViewById(R.id.cancel)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        picDialog.dismiss();
                    }
                });
        return picDialog;
    }

    //请求权限
    public void requestPermission(final Activity activity, final int quality, final VideoVersion ver) {
        permissionUtil = new PermissionUtil(new PermissionProxy() {
            @Override
            public void granted(Object source, int requestCode) {
                switch (ver) {
                    case record:
//                        VideoRecordActivity.startActivityForResult(activity, REQUEST_CODE_FOR_RECORD, quality);
                        VideoRecordActivity.startActivityForResult(activity, REQUEST_CODE_FOR_RECORD, quality,duration);
                        break;
                    case album:
                        openVideoFile(activity);
                        break;
                }
            }

            @Override
            public void denied(Object source, int requestCode, List deniedPermissions) {
            }

            @Override
            public void deniedNoShow(Object source, int requestCode, List noShowPermissions) {
//                ToastUtil.showToast(activity ,"需要的权限被禁止，请到设置中心设置权限");
                showGuideDialog(activity, "请允许访问相关权限");
            }

            @Override
            public void rationale(Object source, int requestCode) {

            }

            @Override
            public boolean needShowRationale(int requestCode) {
                return false;
            }
        });
        permissionUtil.requestPermissions(activity, REQUEST_CODE_PER, permissionArray);
    }

    /**
     * 选择文件视频
     *
     * @param activity
     */
    private void openVideoFile(Activity activity) {
        Intent intent = new Intent();
        /* 开启Pictures画面Type设定为image */
        //intent.setType("video/*;image/*");
        //intent.setType("audio/*"); //选择音频
        intent.setType("video/*"); //选择视频 （mp4 3gp 是android支持的视频格式）
        intent.setAction(Intent.ACTION_GET_CONTENT);
        activity.startActivityForResult(intent, REQUEST_FOR_VIDEO_FILE);
    }


    /**
     * 跳转到压缩页面 压缩
     *
     * @param activity
     * @param inputPath
     */
    public void copressVideo(Activity activity, String inputPath) {
        VideoCompressActivity.startActivityForResult(activity, REQUEST_CODE_FOR_COMPRESS, inputPath);
    }

    /**
     * （必须调用本方法）
     * 权限申请结果处理,在Activity或者Fragment的onRequestPermissionsResult方法中的super方法上一行调用
     * eg：
     */
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        permissionUtil.onRequestPermissionsResult(activity, requestCode, permissions, grantResults);
    }

    public void showGuideDialog(final Activity c, String msg) {
        if (!isShowGuideDialog) {
            ToastUtil.showToast(c, msg);
            return;
        }
        TimeUtils.isCheckFastClick = false;
        CpComDialog.Builder.builder(c).
                setTitle("提示").setContent(msg).setTxtCancel("拒绝").setSure("设置")
                .setTitleSize(20).setContentSize(16).setBtnSize(20)
                .setBtnCancelColor(Color.parseColor("#8d8d8d"))
                .setCancel(false)
                .build().show2BtnDialog(new CpComDialog.Dialog2BtnCallBack() {
            @Override
            public void sure() {
                AppUtils.jumpAppSettingInfo(c);
            }

            @Override
            public void cancel() {

            }
        });
        TimeUtils.isCheckFastClick = true;
    }

    public int getDuration() {
        return duration;
    }

    /**
     * 设置录制视频的时长，-1表示不限制时长 ，单位 ms
     * @param duration
     */
    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getQuality() {
        return quality;
    }
    /**
     * quality 拍摄视频的质量，值为VideoRecordActivity.Q480，Q720，Q1080，Q21600,质量越高，画质越清晰，文件越大
     */
    public void setQuality(int quality) {
        this.quality = quality;
    }
}




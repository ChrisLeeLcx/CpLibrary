package cn.lee.cplibrary.util.video;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.util.List;

import cn.lee.cplibrary.R;
import cn.lee.cplibrary.util.ObjectUtils;
import cn.lee.cplibrary.util.ToastUtil;
import cn.lee.cplibrary.util.dialog.CpComDialog;
import cn.lee.cplibrary.util.permissionutil.PermissionProxy;
import cn.lee.cplibrary.util.permissionutil.PermissionUtil;
import cn.lee.cplibrary.util.system.AppUtils;
import cn.lee.cplibrary.util.timer.TimeUtils;
import cn.lee.cplibrary.util.video.videocompressor.VideoController;

/**
 * 获取拍摄短视频或者相册中的视频
 */
public class CpVideoDialog {

    private Activity activity;
    public static boolean isShowGuideDialog = true;//用户永久拒绝需要的权限时，是否显示引导对话框，使用时候可自行设置
    public static final int REQUEST_CODE_FOR_RECORD = 5230;//绘制相机：录制视频请求码
    public static final int REQUEST_CODE_FOR_COMPRESS = 5231;//压缩视频请求码
    public static final int REQUEST_FOR_VIDEO_FILE = 5232;//选择文件中视频请求吗
    public static final int REQUEST_CODE_FOR_CAMERA = 5233;//系统相机：选择文件中视频请求吗
    private int duration = -1;//录制视频的时长，-1表示不限制时长 ，单位ms（绘制相机，系统相机）
    private int quality = VideoRecordActivity.Q1080;//系统相机和绘制相机：录制视频的分辨率
    private boolean isSysCamera = true;//录制视频是否使用系统相机
    //系统相机设置
    private long sys_max_size_limit = 1024L * 1024 * 100;// 系统相机：允许的最大大小(以 B 为单位)  例如：限制大小为100M

    /***********录像权限相关开始***********/
    private static PermissionUtil permissionUtil;
    public static final int REQUEST_CODE_PER = 500;//权限请求码
    private static final String[] permissionArray = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,//写
            Manifest.permission.READ_EXTERNAL_STORAGE,//读
            Manifest.permission.RECORD_AUDIO,//录制音频权限
            Manifest.permission.CAMERA,//相机权限
    };

    //绘制相机,相册,系统相机
    public enum VideoVersion {
        record, album, camera
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
                requestPermission(activity, VideoVersion.album);
            }
        });
        //拍摄
        btnRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                picDialog.dismiss();
                if (isSysCamera) {
                    requestPermission(activity, VideoVersion.camera);
                } else {
                    requestPermission(activity, VideoVersion.record);
                }
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
    public void requestPermission(final Activity activity, final VideoVersion ver) {
        permissionUtil = new PermissionUtil(new PermissionProxy() {
            @Override
            public void granted(Object source, int requestCode) {
                switch (ver) {
                    case record:
//                        VideoRecordActivity.startActivityForResult(activity, REQUEST_CODE_FOR_RECORD, quality);
                        VideoRecordActivity.startActivityForResult(activity, REQUEST_CODE_FOR_RECORD, quality, duration);
                        break;
                    case camera:
                        openCamera(activity);
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
    public void openVideoFile(Activity activity) {
        Intent intent = new Intent();
        /* 开启Pictures画面Type设定为image */
        //intent.setType("video/*;image/*");
        //intent.setType("audio/*"); //选择音频
        intent.setType("video/*"); //选择视频 （mp4 3gp 是android支持的视频格式）
        intent.setAction(Intent.ACTION_GET_CONTENT);
        activity.startActivityForResult(intent, REQUEST_FOR_VIDEO_FILE);
    }

    /**
     * 打开系统相机：选择文件视频
     */
    private void openCamera(Activity activity) {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        Uri camera_URI;
//        String camera_video_output_path = CpVideoUtil.getOutputMediaFile(activity);
//        File videoFile = new File(camera_video_output_path);//视频存储地址
//        if (videoFile.exists()) {
//            videoFile.delete();
//        }
//        videoFile.mkdir();
        File videoFile = new File(CpVideoUtil.getAlbumStorageDir(), "temp_video" );
        if (!videoFile.exists()) {
            videoFile.mkdir();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {//FileProvider只能用于高版本的app中
            intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            camera_URI = FileProvider.getUriForFile(activity, AppUtils.getAppId(activity) + ".provider", videoFile);
        } else {
            camera_URI = Uri.fromFile(videoFile);
        }
        //储存地址
        intent.putExtra(MediaStore.EXTRA_OUTPUT, camera_URI);
        //用于控制录制视频的质量；0——低质量；1——高质量
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, quality);
        if (duration > 1 * 1000) {//大于1s才设置最大时长，否则不限时长
            //允许记录的最长时间(以 秒 为单位)  例如：限制为60S
            intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, duration / 1000);
        }
        //允许的最大大小(以 B 为单位)  例如：限制大小为100M
        intent.putExtra(MediaStore.EXTRA_SIZE_LIMIT, sys_max_size_limit);
        activity.startActivityForResult(intent, REQUEST_CODE_FOR_CAMERA);

        intent.putExtra("android.intent.extras.CAMERA_FACING",1);
    }

    /**
     * 跳转到压缩页面 压缩
     * @param activity
     * @param inputPath
     */
    public void compressVideo(Activity activity, String inputPath) {
        if (!ObjectUtils.isEmpty(inputPath)) {
            VideoCompressActivity.startActivityForResult(activity, REQUEST_CODE_FOR_COMPRESS, inputPath);
        }
    }

    /**
     * 跳转到压缩页面 压缩
     * @param activity
     * @param inputPath
     * @param quality :值为 VideoController.COMPRESS_QUALITY_HIGH,
     *                VideoController.COMPRESS_QUALITY_MEDIUM，
     *                VideoController.COMPRESS_QUALITY_LOW
     */
    public void compressVideo(Activity activity, String inputPath,int quality) {
        if (!ObjectUtils.isEmpty(inputPath)) {
            VideoCompressActivity.startActivityForResult(activity, REQUEST_CODE_FOR_COMPRESS, inputPath,quality);
        }
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

    /**
     * 设置录制视频的时长，-1表示不限制时长 ，单位 ms
     * eg：15s 则传入15*1000
     */
    public void setDuration(int duration) {
        this.duration = duration;
    }

    /**
     * 1、绘制相机： quality 拍摄视频的质量，
     * 值为VideoRecordActivity.Q480，Q720，Q1080，Q21600,质量越高，画质越清晰，文件越大
     * 2、系统相机 CamcorderProfile.QUALITY_HIGH1——高质量，  QUALITY_LOW0——低质量；
     */
    public void setQuality(int quality) {
        this.quality = quality;
    }

    /**
     * 录制视频是否使用系统相机,true 使用系统相机，false 使用绘制相机
     */
    public void setSysCamera(boolean sysCamera) {
        isSysCamera = sysCamera;
    }

    /**
     * 系统相机：允许的最大大小(以 B 为单位)
     * 例如：限制大小为100M 则设置1024L * 1024 * 100
     */
    public void setSys_max_size_limit(long sys_max_size_limit) {
        this.sys_max_size_limit = sys_max_size_limit;
    }

}




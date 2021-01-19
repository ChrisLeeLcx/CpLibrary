package com.lee.demo.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.CamcorderProfile;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.lee.demo.R;

import java.net.URISyntaxException;

import cn.lee.cplibrary.constant.CpConfig;
import cn.lee.cplibrary.util.FileUtils;
import cn.lee.cplibrary.util.LogUtil;
import cn.lee.cplibrary.util.ObjectUtils;
import cn.lee.cplibrary.util.ToastUtil;
import cn.lee.cplibrary.util.video.CpVideoDialog;
import cn.lee.cplibrary.util.video.CpVideoUtil;
import cn.lee.cplibrary.util.video.VideoCompressActivity;
import cn.lee.cplibrary.util.video.VideoPlayerActivity;
import cn.lee.cplibrary.util.video.VideoRecordActivity;
import cn.lee.cplibrary.util.video.videocompressor.VideoController;

/**
 * 最初版本的视频录制页面参考 https://github.com/hui46226021/ShVideoDemo
 * 最初版本的视频框架参考 https://github.com/fishwjy/VideoCompressor
 */
public class VideoDemoActivity extends AppCompatActivity implements View.OnClickListener {
    Button btn;
    ImageView imageSrc, imageCompress;
    TextView first;
    CpVideoDialog videoUtil;
    private VideoDemoActivity activity;
    String pathSrc, pathCompress;
    private boolean isSysCamera = true;//录制视频是否使用系统相机
    private long min_compress_size = 5 * 1024 * 1024L;//超过5M则压缩

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_demo);
        activity = this;
        findViews();
        videoUtil = new CpVideoDialog(VideoDemoActivity.this);
        videoUtil.setSysCamera(isSysCamera);//不设置也是默认使用系统相机
        if (isSysCamera) {//系统相机
            videoUtil.setDuration(15 * 1000);//15s
            videoUtil.setQuality(CamcorderProfile.QUALITY_HIGH);//高质量
            videoUtil.setSys_max_size_limit(1024L * 1024 * 100);//100M
        } else {//绘制相机
            videoUtil.setDuration(15 * 1000);//15s
            videoUtil.setQuality(VideoRecordActivity.Q1080);
        }

    }

    private void findViews() {
        btn = findViewById(R.id.btn);
        imageSrc = findViewById(R.id.image_src);
        imageCompress = findViewById(R.id.image_compress);
        first = findViewById(R.id.tv_info);
        btn.setOnClickListener(this);
        imageSrc.setOnClickListener(this);
        imageCompress.setOnClickListener(this);
        findViewById(R.id.btn_play_src).setOnClickListener(this);
        findViewById(R.id.btn_play_compress).setOnClickListener(this);
        findViewById(R.id.btn_play_uri).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn:  //录制不限时视频
                videoUtil.showChooseDialog();
                break;
            case R.id.btn_play_src:  //播放原视频
//                pathSrc = "/storage/emulated/0/DCIM/Camera/VID20210111184738.mp4";
                if (ObjectUtils.isEmpty(pathSrc)) {
                    ToastUtil.showToast(activity, "请先录制视频");
                    return;
                }
                VideoPlayerActivity.startActivity(activity, false, pathSrc);
                break;
            case R.id.btn_play_compress://播放压缩视频
//                pathCompress = "/storage/emulated/0/DCIM/Camera/VID20210113161137355.mp4";
                if (ObjectUtils.isEmpty(pathCompress)) {
                    ToastUtil.showToast(activity, "请先录制视频");
                    return;
                }
                VideoPlayerActivity.startActivity(activity, false, pathCompress);
                break;
            case R.id.btn_play_uri:  //播放网络视频
                String uri = "http://fxjyw.fangxiangjia.com/pics/488ff768-8c05-45ae-a958-7b1dcc49f94e/488ff768-8c05-45ae-a958-7b1dcc49f94e-0.mp4";
                VideoPlayerActivity.startActivity(activity, true, uri);
                break;
        }
    }

    /**
     * 录制视频回调
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogUtil.i("", "resultCode=" + resultCode);
        if (requestCode == CpVideoDialog.REQUEST_CODE_FOR_RECORD && resultCode == RESULT_OK) {//绘制相机：录制视频
            pathSrc = data.getStringExtra(VideoRecordActivity.INTENT_EXTRA_VIDEO_PATH);
            pathCompress = data.getStringExtra(VideoCompressActivity.INTENT_COMPRESS_VIDEO_PATH);
            LogUtil.i("", "原视频地址：" + pathSrc + "\n压缩视频地址：" + pathCompress);
            imageSrc.setImageBitmap(getVideoThumbnail(pathSrc));
            imageCompress.setImageBitmap(getVideoThumbnail(pathCompress));
            first.setText("原视频大小：" + CpVideoUtil.getFileSize(pathSrc) + "\n压缩视频大小:" + CpVideoUtil.getFileSize(pathCompress));
        }
        if (requestCode == CpVideoDialog.REQUEST_CODE_FOR_CAMERA) {//系统相机：录制视频
            if (resultCode == RESULT_OK) {//成功
                if (data == null || data.getData() == null) {
                    return;
                }
                try {
                    pathSrc = CpVideoUtil.getFilePath(this, data.getData());
                    LogUtil.i("", "原视频地址：" + pathSrc);
                    imageSrc.setImageBitmap(getVideoThumbnail(pathSrc));
                    first.setText("原视频大小：" + CpVideoUtil.getFileSize(pathSrc) + "\n原视频时长：" + CpVideoUtil.getLocalVideoDuring(pathSrc));
                    compress(pathSrc);
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            } else {//失败 ：当用户点击返回按钮、或者取消时候 resultCode=RESULT_CANCELED，此时删除已经创建的文件
//                FileUtils.deleteFile(pathSrc, new FileUtils.FileDeleteCallback() {
//                    @Override
//                    public void result(int state) {
//                        if (state == 1) {
//                            LogUtil.i("", "删除成功：" + pathSrc);
//                        } else {
//                            LogUtil.i("", "删除失败：" + pathSrc);
//                        }
//                    }
//                });
            }
        } else if (requestCode == CpVideoDialog.REQUEST_FOR_VIDEO_FILE && resultCode == RESULT_OK) {//选择文件中的视频：返回
            if (data != null && data.getData() != null) {
                try {
                    pathSrc = CpVideoUtil.getFilePath(this, data.getData());
                    compress(pathSrc);
                    LogUtil.i("", "原视频地址：" + pathSrc);
                    imageSrc.setImageBitmap(getVideoThumbnail(pathSrc));
                    first.setText("原视频大小：" + CpVideoUtil.getFileSize(pathSrc) + "\n原视频时长：" + CpVideoUtil.getLocalVideoDuring(pathSrc));
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            }
        } else if (requestCode == CpVideoDialog.REQUEST_CODE_FOR_COMPRESS) {//视频压缩：返回了
            if (resultCode == RESULT_OK) {//成功
                pathCompress = data.getStringExtra(VideoCompressActivity.INTENT_COMPRESS_VIDEO_PATH);
                LogUtil.i("", "压缩视频地址:" + pathCompress);
                imageCompress.setImageBitmap(getVideoThumbnail(pathCompress));
                first.setText(first.getText().toString() + "\n压缩视频大小:" + CpVideoUtil.getFileSize(pathCompress) + "\n压缩视频时长：" + CpVideoUtil.getLocalVideoDuring(pathCompress));
            }
            if (resultCode == VideoCompressActivity.RESULT_CODE_FOR_COMPRESS_VIDEO_FAILED) {//失败
                ToastUtil.showToast(activity, "处理失败,请重新上传");
            }

        }
        //if (requestCode == CpVideoDialog.REQUEST_CODE_FOR_RECORD && resultCode == RESULT_CANCELED) {//取消
        //}

    }

    /**
     * 压缩标准
     * @param srcPath
     */
    private void compress(String srcPath) {
//        videoUtil.compressVideo(VideoDemoActivity.this, srcPath, VideoController.COMPRESS_QUALITY_LOW);//进行压缩
        videoUtil.compressVideo(VideoDemoActivity.this, srcPath );//进行压缩
//        if (FileUtils.getFileSize(srcPath) >  20 * 1024 * 1024L) {//大于 20M
//            videoUtil.compressVideo(VideoDemoActivity.this, srcPath, VideoController.COMPRESS_QUALITY_LOW);//进行压缩
//        }else if (FileUtils.getFileSize(srcPath) > 10 * 1024 * 1024L) {// 10~20
//            videoUtil.compressVideo(VideoDemoActivity.this, srcPath, VideoController.COMPRESS_QUALITY_MEDIUM);//进行压缩
//        }else if (FileUtils.getFileSize(srcPath) > min_compress_size) {//视频大于 min_compress_size
//            videoUtil.compressVideo(VideoDemoActivity.this, srcPath,VideoController.COMPRESS_QUALITY_HIGH);//进行压缩
//        }
    }

    private Bitmap getVideoThumbnail(String path) {
        //根据视频地址获取缩略图
//        Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(path, MediaStore.Video.Thumbnails.MINI_KIND);
        Bitmap bitmap = CpVideoUtil.getLocalVideoThumb(path);
        return bitmap;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        videoUtil.onRequestPermissionsResult(requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    /***********权限相关结束***********/

}

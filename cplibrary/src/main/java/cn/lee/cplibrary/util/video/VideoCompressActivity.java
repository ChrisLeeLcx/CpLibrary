package cn.lee.cplibrary.util.video;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.util.Locale;

import cn.lee.cplibrary.R;
import cn.lee.cplibrary.util.ObjectUtils;
import cn.lee.cplibrary.util.SystemBarUtils;
import cn.lee.cplibrary.util.ToastUtil;
import cn.lee.cplibrary.util.dialog.CpComDialog;
import cn.lee.cplibrary.util.video.videocompressor.VideoCompress;

/**
 * 视频压缩页面
 * 本页面输入：需要压缩的视频的路径
 * 输出：压缩完成的视频的路径
 * Activity 显示成Dialog形式
 */
public class VideoCompressActivity extends AppCompatActivity {
    public static final String INTENT_COMPRESS_VIDEO_PATH = "intent_compress_video_path";//压缩的视频路径
    public static final int RESULT_CODE_FOR_COMPRESS_VIDEO_FAILED = 4;//视频压缩失败
    private String inputPath;//需要压缩的视频路径
    private String outputPath;//压缩完成的路径
    TextView tvProgress;
    private VideoCompressActivity context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        SystemBarUtils.setFullScreenNoText(context);
        init();
        compressVideo();
    }

    public static void startActivityForResult(Activity activity, int requestCode, String inputPath) {
        Intent intent = new Intent(activity, VideoCompressActivity.class);
        intent.putExtra("inputPath", inputPath);//源视频路径
        ActivityCompat.startActivityForResult(activity, intent, requestCode, null);
    }

    //获取源视频路径
    private void init() {
        inputPath = getIntent().getStringExtra("inputPath");
        if (ObjectUtils.isEmpty(inputPath)) {
            ToastUtil.showToast(this, "获取视频失败");
            finish();
        }
    }

    //压缩
    private void compressVideo() {
        outputPath = CpVideoUtil.getOutputMediaFile(context);
        VideoCompress.compressVideoLow(inputPath, outputPath, new VideoCompress.CompressListener() {
            @Override
            public void onStart() {//开始压缩
                Dialog dialog = CpComDialog.showProgressDialog(VideoCompressActivity.this, "处理中...");
                tvProgress = dialog.findViewById(R.id.tv_info);
                View ll_bg = dialog.findViewById(R.id.ll_bg);
                ll_bg.setBackgroundResource(R.drawable.cp_shape_bg_loading_tr_dialog);
            }

            @Override
            public void onSuccess() {//压缩成功
                CpComDialog.closeProgressDialog();
                //回传压缩视频路径
                Intent intent = new Intent();
                intent.putExtra(INTENT_COMPRESS_VIDEO_PATH, outputPath);//压缩视频路径
                setResult(RESULT_OK, intent);//回传请求码： CpVideoDialog.REQUEST_CODE_FOR_COMPRESS
                finish();
            }

            @Override
            public void onFail() {//压缩失败
                CpComDialog.closeProgressDialog();
                //回传压缩失败结果
                setResult(RESULT_CODE_FOR_COMPRESS_VIDEO_FAILED);
                finish();
            }

            @Override
            public void onProgress(float percent) {//压缩进度%
                //只保留整数部分
                tvProgress.setText("处理中"+(int) Math.round(percent) + "%");//四舍五入
            }
        });
    }

    //获取时间
    private Locale getLocale() {
        Configuration config = getResources().getConfiguration();
        Locale sysLocale = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            sysLocale = getSystemLocale(config);
        } else {
            sysLocale = getSystemLocaleLegacy(config);
        }
        return sysLocale;
    }

    @SuppressWarnings("deprecation")
    public static Locale getSystemLocaleLegacy(Configuration config) {
        return config.locale;
    }

    @TargetApi(Build.VERSION_CODES.N)
    public static Locale getSystemLocale(Configuration config) {
        return config.getLocales().get(0);
    }
}

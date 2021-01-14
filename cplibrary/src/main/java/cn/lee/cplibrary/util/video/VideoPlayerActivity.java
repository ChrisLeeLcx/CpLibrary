package cn.lee.cplibrary.util.video;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;

import java.util.HashMap;

import cn.lee.cplibrary.R;
import cn.lee.cplibrary.util.LogUtil;
import cn.lee.cplibrary.util.ToastUtil;

/**
 * 视频播放页面
 * 本页面输入：需要压缩的视频的路径
 * 输出：压缩完成的视频的路径
 * Activity 显示成Dialog形式
 */
public class VideoPlayerActivity extends AppCompatActivity {

    private VideoPlayerActivity activity;
    private VideoView videoView;
    public static final String KEY_ISURI = "isUri";
    public static final String KEY_PATH = "path";//网络视频或者本地视频

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cp_activity_video_player);
        activity = this;
        videoView = findViewById(R.id.videoView);
        findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initVideoView();
        play();
    }



    /**
     * 播放视频
     *  videoView start()、stop()、psuse()
     */
    private void play() {
        Intent intent = getIntent();
        boolean isUri = intent.getBooleanExtra(KEY_ISURI, true);
        String path = intent.getStringExtra(KEY_PATH);
        LogUtil.i("","path="+path);
        if (isUri) {//播放网络视频
            Bitmap bitmap = getNetVideoBitmap(path);
            if (bitmap != null) {
                videoView.setBackground(new BitmapDrawable(getResources(), bitmap));
            }
            videoView.setVideoURI(Uri.parse(path));
        } else {//播放本地视频
            videoView.setVideoPath(path);
        }
        videoView.start();
    }




    /**
     * 设置VideoView
     */
    private void initVideoView() {
        MediaController mediaController = new MediaController(activity);
        videoView.setMediaController(mediaController); //加载一个MediaController
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            // 解决播放本地视频短暂黑屏问题
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                    @Override
                    public boolean onInfo(MediaPlayer mp, int what, int extra) {
                        if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START)
                            videoView.setBackgroundColor(Color.TRANSPARENT);
                        return true;
                    }
                });
            }
        });
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {//播放结束
                ToastUtil.showToast(activity,"播放结束");
            }
        });
    }

    /**
     * @param activity
     * @param isUri   true播放网络图片，false：播放本地视频
     * @param path 网络视频 或者本地视频路径
     */
    public static void startActivity(Activity activity, boolean isUri, String path) {
        Intent intent = new Intent(activity, VideoPlayerActivity.class);
        intent.putExtra(VideoPlayerActivity.KEY_ISURI, isUri);
        intent.putExtra(VideoPlayerActivity.KEY_PATH, path);
        activity.startActivity(intent);
    }

    public static Bitmap getNetVideoBitmap(String videoUrl) {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            //根据url获取缩略图
            retriever.setDataSource(videoUrl, new HashMap());
            //获得第一帧图片
            bitmap = retriever.getFrameAtTime();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } finally {
            retriever.release();
        }
        return bitmap;
    }

}

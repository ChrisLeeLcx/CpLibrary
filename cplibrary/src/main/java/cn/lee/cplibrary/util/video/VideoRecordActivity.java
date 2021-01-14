package cn.lee.cplibrary.util.video;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.lee.cplibrary.R;
import cn.lee.cplibrary.util.LogUtil;
import cn.lee.cplibrary.util.ToastUtil;
import cn.lee.cplibrary.widget.video.CameraPreview;

/**
 * 由VideoRecordOldActivity修改的全新版本
 * 模仿淘宝评论时录制评价短视频页面
 * Created by ChrisLee on 2020/1/08
 */

public class VideoRecordActivity extends AppCompatActivity implements View.OnClickListener {
    //控件
    ImageView button_ChangeCamera;
    LinearLayout cameraPreview;
    ImageView buttonCapture;
    ImageView buttonFlash;
    Chronometer textChrono;
    ImageView chronoRecordingImage;
    ImageView btnFinish, btnNext;
    private CameraPreview mPreview;
    private Camera mCamera;
    private MediaRecorder mediaRecorder;

    private String url_file;
    private static boolean flash = false;
    private static boolean cameraFront = false;
    private int quality = CamcorderProfile.QUALITY_480P;

    private static final int FOCUS_AREA_SIZE = 500;

    String TAG = "VideoInputActivity";

    public static final String INTENT_EXTRA_VIDEO_PATH = "intent_extra_video_path";//录制的视频路径
    public static final int RESULT_CODE_FOR_RECORD_VIDEO_FAILED = 3;//视频录制出错

    public static int Q480 = CamcorderProfile.QUALITY_480P;
    public static int Q720 = CamcorderProfile.QUALITY_720P;
    public static int Q1080 = CamcorderProfile.QUALITY_1080P;
    public static int Q21600 = CamcorderProfile.QUALITY_2160P;
    //    private boolean recording = false;//是否是正在录制视频
    private RecordState recordState = RecordState.pre;//录制状态

    private enum RecordState {
        pre //录制前
        , recording //录制中
        , pause //录制暂停 //7.0以上手机支持
    }

    private long pauseTime = 0;// 总暂停时间
    private long pauseStartTime = 0;// 开始暂停时间
    private VideoRecordActivity context;
    private static int duration=-1;////录制视频的时长，-1表示不限制时长 ，单位ms
    public static void startActivityForResult(Activity activity, int requestCode, int quality) {
        Intent intent = new Intent(activity, VideoRecordActivity.class);
        intent.putExtra("quality", quality);
        ActivityCompat.startActivityForResult(activity, intent, requestCode, null);
    }
    public static void startActivityForResult(Activity activity, int requestCode, int quality,int videoDuration) {
        duration =videoDuration;
        Intent intent = new Intent(activity, VideoRecordActivity.class);
        intent.putExtra("quality", quality);
        ActivityCompat.startActivityForResult(activity, intent, requestCode, null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cp_activity_video_record);
        context = this;
        quality = getIntent().getIntExtra("quality", Q480);
        initialize();
    }

    private void focusOnTouch(MotionEvent event) {
        if (mCamera != null) {
            Camera.Parameters parameters = mCamera.getParameters();
            if (parameters.getMaxNumMeteringAreas() > 0) {
                Rect rect = calculateFocusArea(event.getX(), event.getY());
                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
                List<Camera.Area> meteringAreas = new ArrayList<Camera.Area>();
                meteringAreas.add(new Camera.Area(rect, 800));
                parameters.setFocusAreas(meteringAreas);
                mCamera.setParameters(parameters);
                mCamera.autoFocus(mAutoFocusTakePictureCallback);
            } else {
                mCamera.autoFocus(mAutoFocusTakePictureCallback);
            }
        }
    }

    private Rect calculateFocusArea(float x, float y) {
        int left = clamp(Float.valueOf((x / mPreview.getWidth()) * 2000 - 1000).intValue(), FOCUS_AREA_SIZE);
        int top = clamp(Float.valueOf((y / mPreview.getHeight()) * 2000 - 1000).intValue(), FOCUS_AREA_SIZE);
        return new Rect(left, top, left + FOCUS_AREA_SIZE, top + FOCUS_AREA_SIZE);
    }


    private int clamp(int touchCoordinateInCameraReper, int focusAreaSize) {
        int result;
        if (Math.abs(touchCoordinateInCameraReper) + focusAreaSize / 2 > 1000) {
            if (touchCoordinateInCameraReper > 0) {
                result = 1000 - focusAreaSize / 2;
            } else {
                result = -1000 + focusAreaSize / 2;
            }
        } else {
            result = touchCoordinateInCameraReper - focusAreaSize / 2;
        }
        return result;
    }

    private Camera.AutoFocusCallback mAutoFocusTakePictureCallback = new Camera.AutoFocusCallback() {
        @Override
        public void onAutoFocus(boolean success, Camera camera) {
            if (success) {
                // do something...
                Log.i("tap_to_focus", "success!");
            } else {
                // do something...
                Log.i("tap_to_focus", "fail!");
            }
        }
    };

    public void onResume() {
        super.onResume();
        if (!hasCamera(getApplicationContext())) {
            //这台设备没有发现摄像头
            Toast.makeText(getApplicationContext(), R.string.cp_dont_have_camera_error
                    , Toast.LENGTH_SHORT).show();
            recordError(RESULT_CANCELED);
        }
        if (mCamera == null) {
            releaseCamera();
            final boolean frontal = cameraFront;

            int cameraId = findFrontFacingCamera();
            if (cameraId < 0) {
                //前置摄像头不存在
                switchCameraListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(VideoRecordActivity.this, R.string.cp_dont_have_front_camera, Toast.LENGTH_SHORT).show();
                    }
                };

                //尝试寻找后置摄像头
                cameraId = findBackFacingCamera();
                if (flash) {
                    mPreview.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                    buttonFlash.setImageResource(R.drawable.ic_flash_on_white);
                }
            } else if (!frontal) {
                cameraId = findBackFacingCamera();
                if (flash) {
                    mPreview.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                    buttonFlash.setImageResource(R.drawable.ic_flash_on_white);
                }
            }

            mCamera = Camera.open(cameraId);
            mPreview.refreshCamera(mCamera);


        }
    }


    /**
     * 找前置摄像头,没有则返回-1
     *
     * @return cameraId
     */
    private int findFrontFacingCamera() {
        int cameraId = -1;
        //获取摄像头个数
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                cameraId = i;
                cameraFront = true;
                break;
            }
        }
        return cameraId;
    }

    /**
     * 找后置摄像头,没有则返回-1
     *
     * @return cameraId
     */
    private int findBackFacingCamera() {
        int cameraId = -1;
        //获取摄像头个数
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                cameraId = i;
                cameraFront = false;
                break;
            }
        }
        return cameraId;
    }


    //切换前置后置摄像头
    View.OnClickListener switchCameraListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (recordState != RecordState.recording) {//非录制中
                int camerasNumber = Camera.getNumberOfCameras();
                if (camerasNumber > 1) {
                    releaseCamera();
                    chooseCamera();
                } else {
                    //只有一个摄像头不允许切换
                    Toast.makeText(getApplicationContext(), R.string.cp_only_have_one_camera
                            , Toast.LENGTH_SHORT).show();
                }
            }
        }
    };
    //闪光灯
    View.OnClickListener flashListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (recordState != RecordState.recording && !cameraFront) {
                if (flash) {
                    flash = false;
                    buttonFlash.setImageResource(R.drawable.ic_flash_off_white);
                    setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                } else {
                    flash = true;
                    buttonFlash.setImageResource(R.drawable.ic_flash_on_white);
                    setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                }
            }
        }
    };

    //选择摄像头
    public void chooseCamera() {
        if (cameraFront) {
            //当前是前置摄像头
            int cameraId = findBackFacingCamera();
            if (cameraId >= 0) {
                // open the backFacingCamera
                // set a picture callback
                // refresh the preview
                mCamera = Camera.open(cameraId);
                // mPicture = getPictureCallback();
                mPreview.refreshCamera(mCamera);

            }
        } else {
            //当前为后置摄像头
            int cameraId = findFrontFacingCamera();
            if (cameraId >= 0) {
                // open the backFacingCamera
                // set a picture callback
                // refresh the preview
                mCamera = Camera.open(cameraId);
                if (flash) {
                    flash = false;
                    buttonFlash.setImageResource(R.drawable.ic_flash_off_white);
                    mPreview.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                }
                // mPicture = getPictureCallback();
                mPreview.refreshCamera(mCamera);

            }
        }
    }

    //闪光灯
    public void setFlashMode(String mode) {
        try {
            if (getPackageManager().hasSystemFeature(
                    PackageManager.FEATURE_CAMERA_FLASH)
                    && mCamera != null
                    && !cameraFront) {

                mPreview.setFlashMode(mode);
                mPreview.refreshCamera(mCamera);

            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), R.string.cp_hanging_flashLight_mode,
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_finish) {
            back();
        } else if (id == R.id.btn_next) {//拍摄结束，进入下页面处理压缩
            recordFinish();
        } else if (id == R.id.button_capture) {//开始或暂停
            if (recordState == RecordState.pre) {//开始录制
                recordStart();
            } else if (recordState == RecordState.recording) {//正在录制：暂停录制
                recordPause();
            } else if (recordState == RecordState.pause) {//正在录制：暂停录制
                recordResume();
            }
        }
    }


    //---------------------------------------------录制过程----------------------------------------------------

    //录制中只有录制按钮，显示录制时长控件可以显示，其他按钮均隐藏
    //录制开始
    private void recordStart() {
        //准备开始录制视频
        if (!prepareMediaRecorder()) {
            Toast.makeText(VideoRecordActivity.this, getString(R.string.cp_camera_init_fail), Toast.LENGTH_SHORT).show();
            recordError(RESULT_CODE_FOR_RECORD_VIDEO_FAILED);
        }
        //开始录制视频
        runOnUiThread(new Runnable() {
            public void run() {
                try {
                    mediaRecorder.start();
                    startChronometer();
                    if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {//竖屏
                        changeRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    } else {
                        changeRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    }
                    buttonCapture.setImageResource(R.drawable.player_recording);
                } catch (final Exception ex) {
                    Log.i("---", "Exception in thread");
                    recordError(RESULT_CODE_FOR_RECORD_VIDEO_FAILED);
                }
            }
        });
        recordState = RecordState.recording;
    }


    // SystemClock.elapsedRealtime() 从系统开始启动到现在的时间
    //录制暂停
    private void recordPause() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {//7.0及以上
            if (recordState == RecordState.recording) {
                mediaRecorder.pause();
                stopChronometer();
                recordState = RecordState.pause;
                buttonCapture.setImageResource(R.drawable.player_pause);
            }
        } else {

        }
    }


    //录制复位
    private void recordResume() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {//7.0及以上
            if (recordState == RecordState.pause) {
                mediaRecorder.resume();
                pauseTime += SystemClock.elapsedRealtime() - pauseStartTime; //总暂停时间以前暂停时间+本次暂停时间
                textChrono.start();
                recordState = RecordState.recording;
                buttonCapture.setImageResource(R.drawable.player_recording);
            }
        } else {

        }
    }

    //录制结束，进入下个页面
    private void recordFinish() {
        if (recordState == RecordState.pre) {
            ToastUtil.showToast(context, "请先录制视频");
            return;
        }
        //如果正在录制点击这个按钮表示录制完成
        mediaRecorder.stop(); //停止
        finishChronometer();
        buttonCapture.setImageResource(R.drawable.player_record);
        changeRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        releaseMediaRecorder();
//        Toast.makeText(VideoRecordActivity.this, R.string.cp_video_captured, Toast.LENGTH_SHORT).show();
        recordState = RecordState.pre;
        releaseCamera();
        releaseMediaRecorder();
        VideoCompressActivity.startActivityForResult(VideoRecordActivity.this, CpVideoDialog.REQUEST_CODE_FOR_COMPRESS, url_file);
    }

    //录制出错
    private void recordError(int resultCodeForRecordVideoFailed) {
        setResult(resultCodeForRecordVideoFailed);
        releaseCamera();
        releaseMediaRecorder();
        finish();
    }

    // 非正常结束：按系统或页面返回键，需要停止录制 释放资源
    private void back() {
        if (mediaRecorder != null) {
            mediaRecorder.stop(); //停止录制
        }
        finishChronometer();
        buttonCapture.setImageResource(R.drawable.player_record);
        changeRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        releaseMediaRecorder();
        recordState = RecordState.pre;
        releaseCamera();
        releaseMediaRecorder();
        finish();
    }
    //---------------------------------------------工具----------------------------------------------------

    /**
     * 初始化控件和监听
     **/
    public void initialize() {
        button_ChangeCamera = (ImageView) findViewById(R.id.button_ChangeCamera);
        cameraPreview = (LinearLayout) findViewById(R.id.camera_preview);
        buttonCapture = (ImageView) findViewById(R.id.button_capture);
        buttonFlash = (ImageView) findViewById(R.id.buttonFlash);
        chronoRecordingImage = (ImageView) findViewById(R.id.chronoRecordingImage);
        textChrono = (Chronometer) findViewById(R.id.textChrono);
        btnFinish = findViewById(R.id.btn_finish);
        btnNext = findViewById(R.id.btn_next);
        buttonFlash.setOnClickListener(flashListener);
        mPreview = new CameraPreview(VideoRecordActivity.this, mCamera);
        cameraPreview.addView(mPreview);
        buttonCapture.setOnClickListener(this);//录制
        button_ChangeCamera.setOnClickListener(switchCameraListener);
        btnFinish.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        //点击对焦
        cameraPreview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    try {
                        focusOnTouch(event);
                    } catch (Exception e) {
                        Log.i(TAG, getString(R.string.cp_fail_when_camera_try_autofocus, e.toString()));
                        //do nothing
                    }
                }
                return true;
            }
        });

    }

    //检查设备是否有摄像头
    private boolean hasCamera(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            return true;
        } else {
            return false;
        }
    }

    //断开相机连接 释放资源
    private void releaseCamera() {
        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
    }

    //断开录制音视频连接 释放资源
    private void releaseMediaRecorder() {
        if (mediaRecorder != null) {
            mediaRecorder.reset();
            mediaRecorder.release();
            mediaRecorder = null;
            mCamera.lock();
        }
    }

    //准备开始录制视频
    private boolean prepareMediaRecorder() {
        mediaRecorder = new MediaRecorder();
        mCamera.unlock();
        mediaRecorder.setCamera(mCamera);
        // 这两项需要放在setOutputFormat之前
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        // 这两项需要放在setOutputFormat之前
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
//        //设置录制视频的输出格式
//        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
//        //设置音频编码格式
//        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
//        //设置视频编码格式// 设置录制的视频编码h263 h264
//        mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
//        mediaRecorder.setVideoSize(1920, 1080);
//        //这是设置视频录制的帧率，即1秒钟30帧。。必须放在设置编码和格式的后面，否则报错
//        mediaRecorder.setVideoFrameRate(30);
//        //这个属性很重要，这个也直接影响到视频录制的大小，这个设置的越大，视频越清晰
//        mediaRecorder.setVideoEncodingBitRate(12 * 1024 * 1024);
        //设置录制最长时间
         if(duration>0){
            mediaRecorder.setMaxDuration(duration);
        }
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            if (cameraFront) {
                mediaRecorder.setOrientationHint(270);
            } else {
                mediaRecorder.setOrientationHint(90);
            }
        }
        mediaRecorder.setProfile(CamcorderProfile.get(quality));

        url_file = CpVideoUtil.getOutputMediaFile(context);
        File file1 = new File(url_file);
//        File file1 = CpVideoUtil.getOutputMediaFile();
        if (file1.exists()) {
            file1.delete();
        }
//        File file = new File("/mnt/sdcard/videokit");
//        if (!file.exists()) {
//            file.mkdirs();
//        }
//        Date d = new Date();
//        String timestamp = String.valueOf(d.getTime());

//        url_file = "/mnt/sdcard/videokit/in.mp4";

//
//        File file1 = new File(url_file);
//        if (file1.exists()) {
//            file1.delete();
//        }
        mediaRecorder.setOutputFile(file1.toString());
        try {
            mediaRecorder.prepare();
        } catch (IllegalStateException e) {
            e.printStackTrace();
            releaseMediaRecorder();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            releaseMediaRecorder();
            return false;
        }
        return true;

    }

    //停止录制时长显示
    private void finishChronometer() {
        textChrono.stop();
        chronoRecordingImage.setVisibility(View.INVISIBLE);
        textChrono.setVisibility(View.INVISIBLE);
    }

    //计时器停止计时
    private void stopChronometer() {
        textChrono.stop();// 停止计时：让画面显示的数定格，但实际依旧在计时
        pauseStartTime = SystemClock.elapsedRealtime();//暂停时刻
    }

    private long countUp;//计时器经历的时间，单位s，

    //计时器:页面显示
    private void startChronometer() {
        textChrono.setVisibility(View.VISIBLE);
        final long startTime = SystemClock.elapsedRealtime();
        //为Chronometer绑定事件监听器
        textChrono.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer arg0) {
                if(duration>0 && countUp * 1000 >= duration){//录制有时长限制,并且超过了设置最大时长，计时器停止
                    stopChronometer();
                }else{
                    //初始时间-暂停时间
                    countUp = (SystemClock.elapsedRealtime() - startTime - pauseTime) / 1000;
                }
                // countUp = (SystemClock.elapsedRealtime() - startTime - pauseTime) / 1000; //初始时间-暂停时间
                if (countUp % 2 == 0) {
                    chronoRecordingImage.setVisibility(View.VISIBLE);
                } else {
                    chronoRecordingImage.setVisibility(View.INVISIBLE);
                }
                String asText = String.format("%02d", countUp / 60) + ":" + String.format("%02d", countUp % 60);
                textChrono.setText(asText);
            }
        });
        textChrono.setBase(SystemClock.elapsedRealtime());//设置起始时间
        textChrono.start();
    }

    //设置横竖屏
    private void changeRequestedOrientation(int orientation) {
        setRequestedOrientation(orientation);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            back();
            return true;

        }
        return super.onKeyDown(keyCode, event);
    }

    //压缩视频回调
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CpVideoDialog.REQUEST_CODE_FOR_COMPRESS) {//视频压缩：返回了
            if (resultCode == RESULT_OK) {//成功
                String path = data.getStringExtra(VideoCompressActivity.INTENT_COMPRESS_VIDEO_PATH);
                Intent intent = new Intent();
                intent.putExtra(VideoCompressActivity.INTENT_COMPRESS_VIDEO_PATH, path);//压缩的视频的地址
                intent.putExtra(INTENT_EXTRA_VIDEO_PATH, url_file);//录制的视频地址
                setResult(RESULT_OK, intent);//请求码：回传CpVideoDialog.REQUEST_CODE_FOR_RECORD
                finish();
            }
            if (resultCode == VideoCompressActivity.RESULT_CODE_FOR_COMPRESS_VIDEO_FAILED) {//失败
                ToastUtil.showToast(context, "处理失败,请重新上传");
                setResult(RESULT_CODE_FOR_RECORD_VIDEO_FAILED);
                finish();
            }

        }

        super.onActivityResult(requestCode, resultCode, data);
    }

}

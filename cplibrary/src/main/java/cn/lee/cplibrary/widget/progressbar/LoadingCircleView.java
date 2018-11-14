package cn.lee.cplibrary.widget.progressbar;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import cn.lee.cplibrary.R;
import cn.lee.cplibrary.util.LogUtil;

/**
 * Created by Allen on 2017/12/4.
 * <p>
 * 加载view带动画
 */

public class LoadingCircleView extends View {

    private int mWidth, mHeight, centerX, centerY;
    private float radius;
    private Paint bgCirclePaint, arcPaint, successPaint, failPaint,failPaint2;
    private int paintWidth;
    public static final int STATE_START = 0, STATE_LOADING = 1, STATE_SUCCESS = 2, STATE_FAILED = 3, STATE_STOP = 4;

    /**
     * 当前加载状态
     **/
    private int mCurrentState = STATE_START;
    /**
     * 绘制圆弧区域
     */
    private RectF rectF = new RectF();
    /**
     * bgColor默认圆背景颜色, progressColor: 圆弧进度条颜色,可通过布局文件更改
     */
    private int bgColor = 0xFFe1e5e8, progressColor = 0xFFf66b12;
    /**
     * duration:动画执行时间; startDelay动画延时启动时间
     */
    private int duration = 800, startDelay = 0;
    /**
     * startAngle圆弧开始偏移角度; sweepAngle偏移长度
     */
    private float startAngle = 0, sweepAngle = 200;
    /**
     * animatorDrawLoading圆弧转圈; animatorDrawArcToCircle; animatorDrawOk 绘制对勾（√）的动画
     */
    private ValueAnimator animatorDrawLoading;
    /**
     * 路径--用来获取对勾的路径;  failedPath,failedPath2路径--失败差号的路径
     */
    private Path successPath, failedPath, failedPath2;
    /**
     * 取路径的长度
     */
    private PathMeasure pathMeasureSuccess, pathMeasureFailed,pathMeasureFailed2;
    /**
     * 对路径处理实现绘制动画效果
     */
    private PathEffect effect;
    private AnimatorSet animatorSetSuccess,animatorSetFail;
    public enum AnimationType {
        SUCCESSFUL, FAILED
    }

    /**
     * 点击事件及动画事件2完成回调
     */
    private AnimationFinishListener loadingViewListener;

    public void setLoadingViewListener(AnimationFinishListener loadingViewListener) {
        this.loadingViewListener = loadingViewListener;
    }


    public LoadingCircleView(Context context) {
        this(context, null);
    }

    public LoadingCircleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingCircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
        initLoadingAnimation();
        initSuccessAnimator();
        initFailedAnimator();
    }


    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.cp_LoadingCircleView, 0, 0);
            bgColor = ta.getInt(R.styleable.cp_LoadingCircleView_cp_circleBgdColor, bgColor);
            progressColor = ta.getInt(R.styleable.cp_LoadingCircleView_cp_circlePgColor, progressColor);
            ta.recycle();
        }
        mCurrentState = STATE_START;
        initPaint();
    }

    private void initPaint() {
        paintWidth = dp2px(3);
        bgCirclePaint = getPaint(paintWidth, bgColor, Paint.Style.STROKE);
        arcPaint = getPaint(paintWidth, progressColor, Paint.Style.STROKE);
        successPaint = getPaint(paintWidth, progressColor, Paint.Style.STROKE);
        failPaint = getPaint(paintWidth, progressColor, Paint.Style.STROKE);
        failPaint2 = getPaint(paintWidth, progressColor, Paint.Style.STROKE);
    }

    /**
     * 统一处理paint
     *
     * @param strokeWidth 画笔宽度
     * @param color       颜色
     * @param style       风格
     * @return paint
     */
    private Paint getPaint(int strokeWidth, int color, Paint.Style style) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeWidth(strokeWidth);
        paint.setColor(color);
        paint.setAntiAlias(true);
        paint.setStyle(style);
        return paint;
    }

    //    在控件大小发生改变时调用。所以这里初始化会被调用一次, 作用：获取控件的宽和高度
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        centerX = w / 2;
        centerY = h / 2;
        radius = Math.min(w, h) / 2 - paintWidth;
        rectF.left = centerX - radius;
        rectF.top = centerY - radius;
        rectF.right = centerX + radius;
        rectF.bottom = centerY + radius;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        switch (mCurrentState) {
            case STATE_START:
                canvas.drawCircle(centerX, centerY, radius, bgCirclePaint);
                canvas.drawArc(rectF, startAngle, sweepAngle, false, arcPaint);
                break;
            case STATE_LOADING:
                canvas.drawCircle(centerX, centerY, radius, bgCirclePaint);
                canvas.drawArc(rectF, startAngle, sweepAngle, false, arcPaint);
                break;
            case STATE_SUCCESS:
                canvas.drawCircle(centerX, centerY, radius, arcPaint);
                canvas.drawPath(successPath, successPaint);
                break;
            case STATE_FAILED:
                canvas.drawCircle(centerX, centerY, radius, arcPaint);
                canvas.drawPath(failedPath, failPaint);
                canvas.drawPath(failedPath2, failPaint2);
                break;
            default:
                break;
        }
    }
    public int getmCurrentState() {
        return mCurrentState;
    }

    /**
     * 初始化弧度转圈的loading开始动画
     */
    private void initLoadingAnimation() {
        animatorDrawLoading = ValueAnimator.ofFloat(0, 360);
        animatorDrawLoading.setDuration(duration);
        animatorDrawLoading.setStartDelay(startDelay);
        animatorDrawLoading.setRepeatCount(-1);
        animatorDrawLoading.setInterpolator(new LinearInterpolator());
        animatorDrawLoading.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mCurrentState = STATE_LOADING;
                float value = (float) valueAnimator.getAnimatedValue();
                startAngle = value;
                invalidate();
            }
        });
    }



    /**
     * 初始化成功动画
     */
    private void initSuccessAnimator() {
        // 初始化弧度变圆动画
//        ValueAnimator animatorDrawArcToCircle = ValueAnimator.ofFloat(sweepAngle, 360);
//        animatorDrawArcToCircle.setDuration(duration);
//        animatorDrawArcToCircle.setRepeatMode(ValueAnimator.RESTART);
//        animatorDrawArcToCircle.setStartDelay(0);
//        animatorDrawArcToCircle.setInterpolator(new LinearInterpolator());
//        animatorDrawArcToCircle.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator valueAnimator) {
//                float value = (float) valueAnimator.getAnimatedValue();
//                sweepAngle = value;
//                invalidate();
//            }
//        });
        //绘制对勾的动画
        ValueAnimator successAnimator = ValueAnimator.ofFloat(1.0f, 0.0f);
        successAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float value = (Float) valueAnimator.getAnimatedValue();
                effect = new DashPathEffect(new float[]{pathMeasureSuccess.getLength(), pathMeasureSuccess.getLength()}, value * pathMeasureSuccess.getLength());
                successPaint.setPathEffect(effect);
                invalidate();
            }
        });
        successAnimator.addListener(new AnimatorEndListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                mCurrentState = STATE_SUCCESS;
            }
        });
        successAnimator.setDuration(duration);
        successAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        animatorSetSuccess = new AnimatorSet();
        animatorSetSuccess.playSequentially(successAnimator);
        animatorSetSuccess.addListener(new AnimatorEndListener() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (loadingViewListener != null) {
                    loadingViewListener.onAnimationFinished(AnimationType.SUCCESSFUL);
                }
            }
        });


    }

    private void initFailedAnimator() {
        ValueAnimator failedAnimator = ValueAnimator.ofFloat(1.0f,0.0f);
        failedAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float value = (Float) valueAnimator.getAnimatedValue();
                PathEffect PathEffect = new DashPathEffect(new float[]{pathMeasureFailed.getLength(), pathMeasureFailed.getLength()}, pathMeasureFailed.getLength()* value);
                failPaint.setPathEffect(PathEffect);
                invalidate();
            }
        });
        failedAnimator.addListener(new AnimatorEndListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                mCurrentState = STATE_FAILED;

            }
        });
        failedAnimator.setDuration(duration);
        failedAnimator.setInterpolator(new AccelerateDecelerateInterpolator());

        ValueAnimator failedAnimator2 = ValueAnimator.ofFloat(1.0f,0.0f);
        failedAnimator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float value = (Float) valueAnimator.getAnimatedValue();
                PathEffect PathEffect = new DashPathEffect(new float[]{pathMeasureFailed2.getLength(), pathMeasureFailed2.getLength()}, pathMeasureFailed2.getLength()* value);
                failPaint2.setPathEffect(PathEffect);
                invalidate();
            }
        });
        failedAnimator2.setDuration(duration);
        failedAnimator2.setInterpolator(new AccelerateDecelerateInterpolator());

        animatorSetFail = new AnimatorSet();
        animatorSetFail.playSequentially(failedAnimator, failedAnimator2);
        animatorSetFail.addListener(new AnimatorEndListener() {
            @Override
            public void onAnimationEnd(Animator animator) {
                if (loadingViewListener != null) {
                    loadingViewListener.onAnimationFinished(AnimationType.FAILED);
                }


            }
        });


    }

    public void loadingStart() {
        if (mCurrentState != STATE_LOADING) {
            mCurrentState = STATE_START;
            animatorDrawLoading.cancel();
            animatorDrawLoading.start();
        }

    }

    /**
     * loading data successful
     */
    public void loadingSuccessful() {
        createSuccessPath();
        if (animatorDrawLoading != null && animatorDrawLoading.isStarted()) {
            mCurrentState = STATE_STOP;
            animatorDrawLoading.end();
            animatorSetSuccess.cancel();
            animatorSetSuccess.start();
        }
    }

    /**
     * loading data failed
     */
    public void loadingFailed() {
        createFailedPath();
        if (animatorDrawLoading != null && animatorDrawLoading.isStarted()) {
            animatorDrawLoading.end();
            mCurrentState = STATE_STOP;
            animatorSetFail.start();
            animatorSetFail.cancel();
        }
    }
    protected int dp2px(int dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, getResources().getDisplayMetrics());
    }
    /**
     * 绘制对勾
     */
    private void createSuccessPath() {
        if (successPath != null) {
            successPath.reset();
        } else {
            successPath = new Path();
        }
        //对勾的路径
        successPath.moveTo(mWidth / 8 * 3, mHeight / 2);
        successPath.lineTo(mWidth / 2, mHeight / 5 * 3);
        successPath.lineTo(mWidth / 3 * 2, mHeight / 5 * 2);
        pathMeasureSuccess = new PathMeasure(successPath, true);
    }

    private void createFailedPath() {
        if (failedPath != null) {
            failedPath.reset();
            failedPath2.reset();
        } else {
            failedPath = new Path();
            failedPath2 = new Path();
        }
        failedPath.moveTo(mWidth / 3, mHeight / 3);
        failedPath.lineTo(mWidth / 3 * 2, mHeight / 3 * 2);
        failedPath2.moveTo(mWidth / 3 * 2, mHeight / 3);
        failedPath2.lineTo(mWidth / 3, mHeight / 3 * 2);
        pathMeasureFailed = new PathMeasure(failedPath, false);
        pathMeasureFailed2 = new PathMeasure(failedPath2, false);
        PathEffect PathEffect2 = new DashPathEffect(new float[]{pathMeasureFailed2.getLength(), pathMeasureFailed2.getLength()}, pathMeasureFailed2.getLength());
        failPaint2.setPathEffect(PathEffect2);
    }
    private abstract class AnimatorEndListener implements Animator.AnimatorListener {

        @Override
        public void onAnimationStart(Animator animator) {
        }

        @Override
        public void onAnimationCancel(Animator animator) {
        }

        @Override
        public void onAnimationRepeat(Animator animator) {
        }

        @Override
        public void onAnimationEnd(Animator animation) {

        }
    }
    /**
     * 动画结束接口回调
     */
    public interface AnimationFinishListener {
        /**
         * 动画完成回调
         */
        void onAnimationFinished(AnimationType animationType);
    }
}

package cn.lee.cplibrary.widget.sign.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import cn.lee.cplibrary.R;
import cn.lee.cplibrary.widget.sign.glide.SignFileOutputStream;
import cn.lee.cplibrary.widget.sign.view.point.DrawPoint;
import cn.lee.cplibrary.widget.sign.view.point.PointUtil;
import cn.lee.cplibrary.widget.sign.view.point.TimedPoint;


public class HandWriteView extends View {

    List<TimedPoint> points = new ArrayList<>();
    Stack<TimedPoint> cachePoints = new Stack<>();
    PointUtil pointUtil = new PointUtil();
    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Paint mPaint;
    private boolean isSign = false;

    private int mBackColor = Color.TRANSPARENT;

    public HandWriteView(Context context) {
        this(context, null);
    }

    public HandWriteView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HandWriteView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.cp_HandWriteView);
        int maxWidth = a.getDimensionPixelSize(R.styleable.cp_HandWriteView_cp_paintMaxWidth, 16);
        int minWidth = a.getDimensionPixelSize(R.styleable.cp_HandWriteView_cp_paintMinWidth, 8);
        int paintColor = a.getColor(R.styleable.cp_HandWriteView_cp_paintColor, Color.BLACK);
        pointUtil.setWidth(minWidth, maxWidth);
        mPaint = new Paint();
        mPaint.setColor(paintColor);
        mPaint.setStrokeWidth(10);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                getParent().requestDisallowInterceptTouchEvent(true);
                points.clear();
                addPoint(getNewPoint(x, y));
                break;
            case MotionEvent.ACTION_MOVE:
                addPoint(getNewPoint(x, y));
                break;
            case MotionEvent.ACTION_UP:
                isSign = true;
                addPoint(getNewPoint(x, y));
                getParent().requestDisallowInterceptTouchEvent(false);
                break;
        }
        invalidate();
        return true;
    }

    private TimedPoint getNewPoint(float x, float y) {
        if (cachePoints.empty()) {
            return new TimedPoint(x, y);
        } else {
            return cachePoints.pop().set(x, y);
        }
    }

    private void recyclePoint(TimedPoint point) {
        cachePoints.push(point);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        //画背景 如果有需要的话
        super.onDraw(canvas);
        if (mBitmap != null) {
            canvas.drawBitmap(mBitmap, 0, 0, null);
        }
    }

    private void addPoint(TimedPoint point) {
        points.add(point);
        if (points.size() > 3) {
            ensureSignatureBitmap();
            TimedPoint s0 = points.get(0);
            TimedPoint s1 = points.get(1);
            TimedPoint s2 = points.get(2);
            TimedPoint s3 = points.get(3);
            float cx1 = s1.x + (s2.x - s0.x) / 4;
            float cy1 = s1.y + (s2.y - s0.y) / 4;
            float cx2 = s2.x - (s3.x - s1.x) / 4;
            float cy2 = s2.y - (s3.y - s1.y) / 4;
            pointUtil.set(s1, getNewPoint(cx1, cy1), getNewPoint(cx2, cy2), s2);
            float originalWidth = mPaint.getStrokeWidth();
            float drawSteps = (float) Math.floor(pointUtil.length());
            for (int i = 0; i < drawSteps; i++) {
                float t = (float) i / drawSteps;
                DrawPoint drawPoint = pointUtil.calculate(t);
                mPaint.setStrokeWidth(drawPoint.width);
                mCanvas.drawPoint(drawPoint.x, drawPoint.y, mPaint);
            }
            mPaint.setStrokeWidth(originalWidth);
            recyclePoint(points.remove(0));
            recyclePoint(pointUtil.control1);
            recyclePoint(pointUtil.control2);
        } else if (points.size() == 1) {
            points.add(getNewPoint(point.x, point.y));
        }
    }

    private void ensureSignatureBitmap() {
        if (mBitmap == null) {
            mBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
            mCanvas = new Canvas(mBitmap);
        }
    }


    /**
     * 保存画板
     *
     * @param path 保存到路劲
     */

    public void save(String path) throws IOException {
        save(path, false, 0, false);
    }

    public void save(String path, boolean isEncrypt) throws IOException {
        save(path, false, 0, isEncrypt);
    }

    /**
     * 保存画板
     *
     * @param path       保存到路径
     * @param clearBlank 是否清楚空白区域
     * @param blank      保留的边缘空白区域 单位：px
     * @param isEncrypt  加密存储，选择加密存储会自动追加后缀为.sign
     */
    public void save(String path, boolean clearBlank, int blank, boolean isEncrypt) throws IOException {
        Bitmap bitmap = mBitmap;
        if (clearBlank) {
            bitmap = clearBlank(bitmap, blank);
        }
        if (isEncrypt) {
            path = path + ".sign";
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
        byte[] buffer = bos.toByteArray();
        if (buffer != null) {
            File file = new File(path);
            if (file.exists()) {
                file.delete();
            } else if (!file.getParentFile().exists() && !file.isDirectory()) {
                file.getParentFile().mkdirs();
            }
            OutputStream outputStream = isEncrypt ? new SignFileOutputStream(file) : new FileOutputStream(file);
            outputStream.write(buffer);
            outputStream.close();
        }
    }

    /**
     * 逐行扫描 清除边界空白。
     *
     * @param bp
     * @param blank 边距留多少个像素
     * @return
     */
    private Bitmap clearBlank(Bitmap bp, int blank) {
        int HEIGHT = bp.getHeight();
        int WIDTH = bp.getWidth();
        int top = 0, left = 0, right = WIDTH, bottom = HEIGHT;
        int[] pixs = new int[WIDTH];
        boolean isStop;
        for (int y = 0; y < HEIGHT; y++) {
            bp.getPixels(pixs, 0, WIDTH, 0, y, WIDTH, 1);
            isStop = false;
            for (int pix : pixs) {
                if (pix != mBackColor) {
                    top = y;
                    isStop = true;
                    break;
                }
            }
            if (isStop) {
                break;
            }
        }
        for (int y = HEIGHT - 1; y >= 0; y--) {
            bp.getPixels(pixs, 0, WIDTH, 0, y, WIDTH, 1);
            isStop = false;
            for (int pix : pixs) {
                if (pix != mBackColor) {
                    bottom = y;
                    isStop = true;
                    break;
                }
            }
            if (isStop) {
                break;
            }
        }

        int scanHeight = bottom - top;
        pixs = new int[scanHeight];
        for (int x = 0; x < WIDTH; x++) {
            bp.getPixels(pixs, 0, 1, x, top, 1, scanHeight);
            isStop = false;
            for (int pix : pixs) {
                if (pix != mBackColor) {
                    left = x;
                    isStop = true;
                    break;
                }
            }
            if (isStop) {
                break;
            }
        }
        for (int x = WIDTH - 1; x > 0; x--) {
            bp.getPixels(pixs, 0, 1, x, top, 1, scanHeight);
            isStop = false;
            for (int pix : pixs) {
                if (pix != mBackColor) {
                    right = x;
                    isStop = true;
                    break;
                }
            }
            if (isStop) {
                break;
            }
        }
        if (blank < 0) {
            blank = 0;
        }
        left = left - blank > 0 ? left - blank : 0;
        top = top - blank > 0 ? top - blank : 0;
        right = right + blank > WIDTH - 1 ? WIDTH - 1 : right + blank;
        bottom = bottom + blank > HEIGHT - 1 ? HEIGHT - 1 : bottom + blank;
        return Bitmap.createBitmap(bp, left, top, right - left, bottom - top);
    }

    public void setPaintColor(int paintColor) {
        mPaint.setColor(paintColor);
    }

    public void setPaintWidth(int mMinWidth, int mMaxWidth) {
        if (mMinWidth > 0 && mMaxWidth > 0 && mMinWidth <= mMaxWidth) {
            pointUtil.setWidth(mMinWidth, mMaxWidth);
        }
    }

    /**
     * 清楚签名
     */
    public void clear() {
        if (mBitmap != null && !mBitmap.isRecycled()) {
            mBitmap.recycle();
        }
        mBitmap = null;
        ensureSignatureBitmap();
        invalidate();
        isSign = false;
    }

    /**
     * 是否已经签名
     */
    public boolean isSign() {
        return isSign;
    }
}

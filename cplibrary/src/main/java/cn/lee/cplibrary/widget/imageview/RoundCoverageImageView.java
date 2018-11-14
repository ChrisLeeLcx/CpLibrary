package cn.lee.cplibrary.widget.imageview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ImageView;

import cn.lee.cplibrary.R;


/**
 * function:圆角，蒙层 Imageview
 * Created by ChrisLee on 2018/3/7.
 */


public class RoundCoverageImageView extends ImageView {

    private Paint paint;
    private boolean isCenterImgShow;
    private Bitmap bitmap;
    public  static final int IMAGEID_DEFAULT= R.drawable.geren_xiugaimc;//默认蒙层的ID


    public RoundCoverageImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    public RoundCoverageImageView(Context context) {
        super(context);
        init();
    }
    public RoundCoverageImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * @param centerImgShow ：true则设置图片蒙层，false则不设置图片蒙层
     * @param ImageId:当centerImgShow=false的时候，随便设置,不起作用，建议设置为-1
     */
    public void setCenterImgShow(boolean centerImgShow, int ImageId) {
        isCenterImgShow = centerImgShow;
        if (isCenterImgShow) {
            if (ImageId > 0) {
                bitmap = BitmapFactory.decodeResource(getResources(), ImageId);
            } else {
                bitmap = BitmapFactory.decodeResource(getResources(), IMAGEID_DEFAULT);
            }
        }
        invalidate();
    }

    private final RectF roundRect = new RectF();

    private float rect_adius = 10;

    private final Paint maskPaint = new Paint();

    private final Paint zonePaint = new Paint();

    private void init() {
        maskPaint.setAntiAlias(true);
        maskPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        zonePaint.setAntiAlias(true);
        zonePaint.setColor(Color.WHITE);
        float density = getResources().getDisplayMetrics().density;
        rect_adius = rect_adius * density;
        paint = new Paint();
    }

    public void setRectAdius(float adius) {
        rect_adius = adius;
        invalidate();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right,
                            int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        int w = getWidth();
        int h = getHeight();
        roundRect.set(0, 0, w, h);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.saveLayer(roundRect, zonePaint, Canvas.ALL_SAVE_FLAG);
        canvas.drawRoundRect(roundRect, rect_adius, rect_adius, zonePaint);
        canvas.saveLayer(roundRect, maskPaint, Canvas.ALL_SAVE_FLAG);
        super.draw(canvas);
        canvas.restore();
        if (isCenterImgShow && bitmap != null) {
            canvas.drawBitmap(bitmap, getMeasuredWidth() / 2 - bitmap.getWidth() / 2, getMeasuredHeight() / 2 - bitmap.getHeight() / 2, paint);
        }
    }
}
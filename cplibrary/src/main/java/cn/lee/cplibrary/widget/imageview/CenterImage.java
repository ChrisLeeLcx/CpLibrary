package cn.lee.cplibrary.widget.imageview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.ImageView;

import cn.lee.cplibrary.R;


/**
 * function:
 * Created by ChrisLee on 2018/3/7.
 */

public class CenterImage extends ImageView {//AppCompatImageView
    private Paint paint;
    private boolean isCenterImgShow;
    private Bitmap bitmap;
    public  static final int IMAGEID_DEFAULT= R.drawable.geren_xiugaimc;//默认蒙层的ID
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

    public CenterImage(Context context) {
        super(context);
        init();
    }

    public CenterImage(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CenterImage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isCenterImgShow && bitmap != null) {
            canvas.drawBitmap(bitmap, getMeasuredWidth() / 2 - bitmap.getWidth() / 2, getMeasuredHeight() / 2 - bitmap.getHeight() / 2, paint);
        }
    }
}
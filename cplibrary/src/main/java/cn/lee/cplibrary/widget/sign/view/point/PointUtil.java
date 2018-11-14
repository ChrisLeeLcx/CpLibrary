package cn.lee.cplibrary.widget.sign.view.point;


public class PointUtil {

    public TimedPoint startPoint;
    public TimedPoint control1;
    public TimedPoint control2;
    public TimedPoint endPoint;
    private int mMinWidth = 8;
    private int mMaxWidth = 16;
    private float mVelocityFilterWeight = 0.9f;
    private float mLastVelocity;
    private float mLastWidth;
    private float mStartWidth;
    private float widthDelta;
    private DrawPoint drawPoint = new DrawPoint();

    public PointUtil() {
    }

    public void setWidth(int mMinWidth, int mMaxWidth) {
        this.mMinWidth = mMinWidth;
        this.mMaxWidth = mMaxWidth;
    }

    public PointUtil set(TimedPoint startPoint, TimedPoint control1,
                         TimedPoint control2, TimedPoint endPoint) {
        this.startPoint = startPoint;
        this.control1 = control1;
        this.control2 = control2;
        this.endPoint = endPoint;

        float velocity = startPoint.velocityTo(endPoint);
        velocity = Float.isNaN(velocity) ? 0.0f : velocity;

        velocity = mVelocityFilterWeight * velocity
                + (1 - mVelocityFilterWeight) * mLastVelocity;
        float newWidth = mMinWidth + (mMaxWidth - mMinWidth) / (Math.max(1, velocity));
        mLastVelocity = velocity;
        widthDelta = newWidth - mLastWidth;
        mStartWidth = mLastWidth;
        mLastWidth = newWidth;
        return this;
    }

    /**
     * 获得贝塞尔曲线的长度
     *
     * @return
     */
    public float length() {
        int steps = 10;
        float length = 0;
        double cx, cy, px = 0, py = 0, xDiff, yDiff;
        for (int i = 0; i <= steps; i++) {
            float t = (float) i / steps;
            cx = point(t, this.startPoint.x, this.control1.x,
                    this.control2.x, this.endPoint.x);
            cy = point(t, this.startPoint.y, this.control1.y,
                    this.control2.y, this.endPoint.y);
            if (i > 0) {
                xDiff = cx - px;
                yDiff = cy - py;
                length += Math.sqrt(xDiff * xDiff + yDiff * yDiff);
            }
            px = cx;
            py = cy;
        }
        return length;

    }

    /**
     * 求分段的贝塞尔曲线长度。
     * //P(t)=p1(1-t)^3+3p2(1-t)^2t+3p3(1-t)t^2+p4t^3；
     *
     * @param t
     * @param start
     * @param c1
     * @param c2
     * @param end
     * @return
     */

    public double point(float t, float start, float c1, float c2, float end) {
        return start * (1.0 - t) * (1.0 - t) * (1.0 - t)
                + 3.0 * c1 * (1.0 - t) * (1.0 - t) * t
                + 3.0 * c2 * (1.0 - t) * t * t
                + end * t * t * t;
    }

    public DrawPoint calculate(float t) {
        float tt = t * t;
        float ttt = tt * t;
        float u = 1 - t;
        float uu = u * u;
        float uuu = uu * u;

        float x = uuu * this.startPoint.x;
        x += 3 * uu * t * this.control1.x;
        x += 3 * u * tt * this.control2.x;
        x += ttt * this.endPoint.x;

        float y = uuu * this.startPoint.y;
        y += 3 * uu * t * this.control1.y;
        y += 3 * u * tt * this.control2.y;
        y += ttt * this.endPoint.y;
        return drawPoint.set(x, y, mStartWidth + ttt * widthDelta);
    }

}
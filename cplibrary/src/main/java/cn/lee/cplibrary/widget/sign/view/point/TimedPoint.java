package cn.lee.cplibrary.widget.sign.view.point;

/**
 * @version 1.0
 * @auth wastrel
 * @date 2018/1/3 9:46
 * @copyRight 四川金信石信息技术有限公司
 * @since 1.0
 */
public class TimedPoint {
    public float x;
    public float y;
    public long timestamp;

    public TimedPoint(float x, float y) {
        this.x = x;
        this.y = y;
        this.timestamp = System.currentTimeMillis();
    }

    public TimedPoint set(float x, float y) {
        this.x = x;
        this.y = y;
        this.timestamp = System.currentTimeMillis();
        return this;
    }

    public float distanceTo(TimedPoint point) {
        return (float) Math.sqrt(Math.pow(point.x - x, 2) + Math.pow(point.y - y, 2));
    }

    public float velocityTo(TimedPoint point) {
        long t = point.timestamp - timestamp;
        if (t == 0) return 0;
        else return distanceTo(point) / t;
    }
}

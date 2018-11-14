package cn.lee.cplibrary.widget.sign.view.point;

/**
 * @version 1.0
 * @auth wastrel
 * @date 2018/1/3 17:51
 * @copyRight 四川金信石信息技术有限公司
 * @since 1.0
 */
public class DrawPoint {
    public float x;
    public float y;
    public float width;

    public DrawPoint set(float x, float y, float width) {
        this.x = x;
        this.y = y;
        this.width = width;
        return this;
    }
}

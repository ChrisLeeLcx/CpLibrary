package cn.lee.cplibrary.widget.sidebar;

/**
 * function:
 * Created by ChrisLee on 2018/5/24.
 */

public class BaseSideBarBean {
    //注意 TYPE_HEADER_SECTION的值ordinal()必须是1,位置不能动
    public  enum Type {
        /**0:item 内容 **/ TYPE_ITEM,
        /**1:小组头部，悬浮部分**/ TYPE_HEADER_SECTION,
        /**2:列表头部**/TYPE_HEADER
    }
    public String name;//界面显示的内容
    public String pys;//name的拼音首字母的大写
    public Type type;//0:item,1:小组头部，2：RecyclerView头部

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPys() {
        return pys;
    }

    public void setPys(String pys) {
        this.pys = pys;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }


}

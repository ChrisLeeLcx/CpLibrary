package cn.lee.cplibrary.util.dialog.bottom;

/**
 *
 * @author: ChrisLee
 * @time: 2018/11/21
 */

public class BaseDialogBean {
    private String name;//对话框上显示的文字

    public BaseDialogBean(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

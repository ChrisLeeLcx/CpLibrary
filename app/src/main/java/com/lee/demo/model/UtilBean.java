package com.lee.demo.model;

/**
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 */
public class UtilBean {
    private String name;
    private String content;

    public UtilBean(String name, String  content) {
        this.content = content;
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

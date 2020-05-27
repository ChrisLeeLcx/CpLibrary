package com.lee.demo.model;

import com.chad.library.adapter.base.entity.SectionEntity;

/**
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 */
public class SectionUtil extends SectionEntity<UtilBean> {

    public SectionUtil(boolean isHeader, String header ) {
        super(isHeader, header);
    }

    public SectionUtil(UtilBean t) {
        super(t);
    }


}

package com.lee.demo.util.sidebar;

import com.lee.demo.util.sidebar.model.GirdSideBarWithPinYinBean;

import java.util.Comparator;

/**
 * Created by ChrisLee on 2020/6/10.
 * 网格索引字母列表数据按照拼音首字母排序
 */

public class GridSideBarLetterComparator implements Comparator<GirdSideBarWithPinYinBean> {

    @Override
    public int compare(GirdSideBarWithPinYinBean l, GirdSideBarWithPinYinBean r) {
        if (l == null || r == null) {
            return 0;
        }
        String lhsSortLetters = l.pys.substring(0, 1).toUpperCase();
        String rhsSortLetters = r.pys.substring(0, 1).toUpperCase();
        if (lhsSortLetters == null || rhsSortLetters == null) {
            return 0;
        }
        return lhsSortLetters.compareTo(rhsSortLetters);
    }
}
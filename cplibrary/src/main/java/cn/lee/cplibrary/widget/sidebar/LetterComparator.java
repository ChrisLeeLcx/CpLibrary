package cn.lee.cplibrary.widget.sidebar;

import java.util.Comparator;

public abstract class LetterComparator<T extends BaseSideBarBean> implements Comparator<T> {

    @Override
    public int compare(T l, T r) {
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
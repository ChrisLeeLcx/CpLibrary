package cn.lee.cplibrary.util;

import java.text.Collator;
import java.util.Comparator;

/**
 * 按照中文 、英文 、数字顺序排序
 * @author: ChrisLee
 * @time: 2018/8/9
 */

class StringSorComparator implements Comparator<String> {
    @Override
    public int compare(String str1, String str2) {
        char[] c={str1.toLowerCase().charAt(0),str2.toLowerCase().charAt(0)};//首字母
        String[] str={str1.substring(0, 1),str2.substring(0, 1)};
        int type[]={1,1};
        for(int i=0;i<2;i++)
        {
            if(str[i].matches("[\\u4e00-\\u9fbb]+"))//中文字符
                type[i]=1;
            else if(c[i]>='a' && c[i]<='z')
                type[i]=2;
            else if(c[i]>='1' && c[i]<='9')
                type[i]=3;
            else
                type[i]=4;
        }
        if(type[0]==1 && type[1]==1)
            return Collator.getInstance(java.util.Locale.CHINESE).compare(str1, str2);
        if(type[0]==type[1]) //同一类
            return str1.compareTo(str2);
        return type[0]-type[1];
    }
}
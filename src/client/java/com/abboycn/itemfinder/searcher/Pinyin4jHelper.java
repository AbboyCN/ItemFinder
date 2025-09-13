package com.abboycn.itemfinder.searcher;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class Pinyin4jHelper {

    private static final HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();

    static {
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
    }

    //获取字符串的拼音首字母
    public static String getPinyinInitials(String chinese) {
        StringBuilder sb = new StringBuilder();
        for (char c : chinese.toCharArray()) {
            if (isChinese(c)) {
                try {
                    String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(c, format);
                    if (pinyinArray != null && pinyinArray.length > 0) {
                        sb.append(pinyinArray[0].charAt(0));
                    }
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                    //noinspection CallToPrintStackTrace
                    e.printStackTrace();
                }
            }
            else{
                sb.append(c);
            }
        }
        return sb.toString();
    }

    //判断字符是否是中文
    public static boolean isChinese(char c) {
        return String.valueOf(c).matches("[\\u4E00-\\u9FA5]");
    }
}
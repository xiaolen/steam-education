package com.example.steam.crwal.tools;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * @Author: mly
 * @Date: Created in 2019/6/11
 */
public class StringUtils {
    public static String StringFilter(String str) throws PatternSyntaxException {
        // 只允许字母和数字、中文
        // String regEx="[^a-zA-Z0-9]";
        // 清除掉[]中所有特殊字符
        String regEx = "[`~☆★!@#$%^&*()+=|{}':;,\\[\\]》·.<>/?~ @#￥%……（）——+|{}【】‘；：”“’\"\"。，、？?.—]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim().replace(" ", "").replace("\\", "");
    }
}

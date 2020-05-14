package utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * html标签处理工具类
 */
public class HTMLUtil {
    public static String delHTMLTag(String htmlStr) {
        //定义script的正则表达式
        String regEx_script = "<script[^>]*?>[\\s\\S]*?<\\/script>";
        //定义style的正则表达式
        String regEx_style = "<style[^>]*?>[\\s\\S]*?<\\/style>";
        //定义HTML标签的正则表达式
        String regEx_html = "<[^>]+>";
//        String regEx_html = "<.*?>";
        // 定义一些特殊字符的正则表达式 如：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        String regEx_special = "\\&[a-zA-Z]{1,10};";

        //过滤script标签
        Pattern p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
        Matcher m_script = p_script.matcher(htmlStr);
        htmlStr = m_script.replaceAll("");
        //过滤style标签
        Pattern p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
        Matcher m_style = p_style.matcher(htmlStr);
        htmlStr = m_style.replaceAll("");
        //过滤html标签
        Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
        Matcher m_html = p_html.matcher(htmlStr);
        htmlStr = m_html.replaceAll("");
        //过滤nbsp
        Pattern p_special = Pattern.compile(regEx_special, Pattern.CASE_INSENSITIVE);
        Matcher m_special = p_special.matcher(htmlStr);
        htmlStr = m_special.replaceAll("");

        //返回文本字符串
        return htmlStr.trim();
    }
}
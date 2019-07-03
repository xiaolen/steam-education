package com.example.steam.crwal.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.html.DomAttr;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * PageUtils
 *
 * @author xl
 * @date 2016/8/21
 */
@Slf4j
public class PageUtils {

    /**
     * @desc: 获取页面String
     * @param: page
     * @author: YuYangjun
     * @date: 2017/11/30 下午5:44
     */
    public static String getPageInfo(Page page) {
        if (null != page) {
                return page.getWebResponse().getContentAsString();
        } else {
            return "";
        }
    }

    /**
     * @desc: 获取Document
     * @param: page
     * @author: YuYangjun
     * @date: 2017/11/30 下午5:44
     */
        public static Document getDocument(Page page) {
            try {
                String pageInfo = getPageInfo(page);
                if (StringUtils.isNotBlank(pageInfo)) {
                    return Jsoup.parse(pageInfo);
                }
            } catch (Exception e) {
                log.error("获取页面失败", e);
            }
            return null;
        }

        /**
         * 判断是否是json结构
         */
        public static boolean isJson(String json) {
            try {
                JSON.parse(json);
            } catch (JSONException e) {
                return false;
            }
            return true;
        }

        /**
         * @desc: 获取页面Json
         * @param: page
         * @author: YuYangjun
         * @date: 2017/12/6 下午3:10
         */
        public static JSONObject getJSONObject(Page page) {
            return JsonUtils.getJSONObject(getPageInfo(page));
        }

        /**
         * @desc: 获取页面Json
         * @param: pageInfo
         * @author: YuYangjun
         * @date: 2017/12/6 下午3:10
         */
        public static JSONObject getJSONObject(String pageInfo) {
            return JsonUtils.getJSONObject(pageInfo);
        }

        /**
         * @desc: 获取页面Json
         * @param: pageInfo
         * @author: Lsz
         * @date: 2018/05/22
         */
        public static JSONArray getJSONArray(String pageInfo) {
            return JsonUtils.getJSONArray(pageInfo);
        }

        public static String getValueById(Page page, String id) {
            String result = "";
            if (StringUtils.isNotBlank(PageUtils.getPageInfo(page))) {
                DomElement domElement = ((HtmlPage) page).getElementById(id);
                if (domElement != null) {
                    result = domElement.getAttribute("value");
                }
            }
            return result;
        }

        public static String getValueByName(Page page, String name) {
        String result = "";
        if (StringUtils.isNotBlank(PageUtils.getPageInfo(page))) {
            DomElement domElement = ((HtmlPage) page).getElementByName(name);
            if (domElement != null) {
                result = domElement.getAttribute("value");
            }
        }
        return result;
    }

    public static String getValueByXpath(Page page, String xpath) {
        String result = "";
        if (StringUtils.isNotBlank(PageUtils.getPageInfo(page))) {
            HtmlElement htmlElement = ((HtmlPage) page).getFirstByXPath(xpath);
            if (htmlElement != null) {
                result = htmlElement.asText();
            }
        }
        return result;
    }

    public static List<String> getListValueByXpath(Page page, String xpath) {
        List<String> result = new ArrayList();
        if (StringUtils.isNotBlank(PageUtils.getPageInfo(page))) {
            List<HtmlElement> elements = getElementByXpath(page, xpath);
            if (CollectionUtil.isNotEmpty(elements)) {
                for (HtmlElement element : elements) {
                    String value = element.asText();
                    result.add(value);
                }
            }
        }
        return result;
    }

    public static String getListValuesByXpath(Page page, String xpath) {
        StringBuffer result = new StringBuffer();
        if (StringUtils.isNotBlank(PageUtils.getPageInfo(page))) {
            List<HtmlElement> elements = getElementByXpath(page, xpath);
            if (CollectionUtil.isNotEmpty(elements)) {
                for (HtmlElement element : elements) {
                    String value = element.asText();
                    result.append(value + "\n");
                }
            }
        }
        return result.toString();
    }

    public static String getBrValuesByXpath(Page page, String xpath) {
        StringBuffer result = new StringBuffer();
        if (StringUtils.isNotBlank(PageUtils.getPageInfo(page))) {
            List<HtmlElement> elements = ((HtmlPage) page).getByXPath(xpath);
            if (CollectionUtil.isNotEmpty(elements)) {
                for (HtmlElement element : elements) {
                    String value = element.asText();
                    result.append(value + "<br></br>");
                }
            }
        }
        return result.toString();
    }

    public static String getValueByXpath(HtmlElement element, String xpath) {
        String result = "";
        HtmlElement htmlElement = element.getFirstByXPath(xpath);
        if (htmlElement != null) {
            result = htmlElement.asText();
        }
        return result;
    }

    public static List<String> getListValueByXpath(HtmlElement ele, String xpath) {
        List<String> result = new ArrayList();
        List<HtmlElement> elements = ele.getByXPath(xpath);
        if (CollectionUtil.isNotEmpty(elements)) {
            for (HtmlElement element : elements) {
                String value = element.asText();
                result.add(value);
            }
        }
        return result;
    }

    /**
     * 换行不加br处理
     *
     * @param ele
     * @param xpath
     * @return
     * @description
     * @author heliang
     * @create 2016年9月8日 下午3:19:41
     */
    public static String getListValuesByXpath(HtmlElement ele, String xpath) {
        StringBuffer result = new StringBuffer();
        @SuppressWarnings("unchecked")
        List<HtmlElement> elements = ele.getByXPath(xpath);
        if (CollectionUtil.isNotEmpty(elements)) {
            for (HtmlElement element : elements) {
                String value = element.asText();
                result.append(value + "<br></br>");
            }
        }
        return result.toString();
    }

    /**
     * 换行加br处理
     *
     * @param ele
     * @param xpath
     * @return
     * @description
     * @author heliang
     * @create 2016年9月8日 下午3:19:41
     */
    public static String getBrValuesByXpath(HtmlElement ele, String xpath) {
        StringBuffer result = new StringBuffer();
        List<HtmlElement> elements = ele.getByXPath(xpath);
        if (CollectionUtil.isNotEmpty(elements)) {
            for (HtmlElement element : elements) {
                String value = element.asText();
                result.append(value + "<br></br>");
            }
        }
        return result.toString();
    }

    public static String getAttrValueByXpath(HtmlElement element, String xpath) {
        String result = "";
        DomAttr domAttr = element.getFirstByXPath(xpath);
        if (domAttr != null) {
            result = domAttr.getValue();
        }
        return result;
    }

    public static List<HtmlElement> getElementByXpath(Page page, String xpath) {
        List<HtmlElement> list = ((HtmlPage) page).getByXPath(xpath);
        return list;
    }

    public static List<HtmlElement> getElementByXpath(HtmlElement element, String xpath) {
        List<HtmlElement> list = element.getByXPath(xpath);
        return list;
    }

    public static HtmlElement getFirstElementByXpath(Page page, String xpath) {
        return ((HtmlPage) page).getFirstByXPath(xpath);
    }

    public static HtmlElement getFirstElementByXpath(HtmlElement element, String xpath) {
        return element.getFirstByXPath(xpath);
    }

    /**
     * @desc:
     * @param: page
     * @author: Heliang
     * @date: 2017/11/30 下午5:44
     */
    public static InputStream getPageInputStream(Page page) {
        if (null != page) {
            try {
                return page.getWebResponse().getContentAsStream();
            } catch (IOException e) {
                log.info("-> 页面获取流出现异常", e);
                return null;
            }
        }
        return null;
    }
}

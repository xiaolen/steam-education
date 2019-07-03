package com.example.steam.parent;

import com.example.steam.crwal.util.CollectionUtil;
import com.example.steam.crwal.util.JsonUtils;
import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;

import java.io.Serializable;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author:lyd
 * @create: 2019-03-26
 **/
@Slf4j
public class AccordComplaintParent implements Serializable {

    public Object data;


    protected WebClient webClient;

    protected boolean savePageFlag = true;

    public void setProperties() {

        this.webClient = initWebClientNoProxy(webClient);
    }


    /**
     * @desc: 关闭浏览器并归还代理(如使用)
     * @param: null
     * @author: lyd
     * @date: 2019/1/17 下午13:01
     */
    protected void close() {
        try {
            if (null != webClient) {
                webClient.close();
            }
        } catch (Exception e) {
            getLogger().info("[{}]关闭浏览器出错", e);
        }

        webClient = null;
    }




    /**
     * 创建默认的webclient
     * <b>如果需要特殊处理,需要重写些方法</b>
     *
     * @return
     */
    protected WebClient initWebClientNoProxy(WebClient webClient) {
        if (null == webClient) {
            webClient = new WebClient(BrowserVersion.FIREFOX_45);

            webClient.getCookieManager().clearCookies();
            webClient.getCache().clear();
        }
        webClient.setRefreshHandler(new ImmediateRefreshHandler());
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());
        webClient.getOptions().setRedirectEnabled(true);
        webClient.getOptions().setUseInsecureSSL(true);
        webClient.getCookieManager().setCookiesEnabled(true);
        webClient.getOptions().setActiveXNative(false);
        webClient.getOptions().setAppletEnabled(false);
        // 禁用css支持
        webClient.getOptions().setCssEnabled(false);
        // js运行错误时，是否抛出异常
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setJavaScriptEnabled(false);
        webClient.getOptions().setTimeout(60000);
        return webClient;
    }
    /**
    * @Param : 获取日志
    * @description:
    * @Return:
    * @Author: lyd
    * @Date:2019/3/29
    * @time:17:53
    */
    protected Logger getLogger() {
        return log;
    }

   /**
   * @Param :
   * @description:
   * @Return:
   * @Author: lyd
   * @Date:2019/3/29
   * @time:17:52
   */
    public Page getPage(WebClient webClient, String url, HttpMethod httpMethod, List<NameValuePair> params, Map<String, String> header) {
        return getPage(webClient, url, httpMethod, params, StandardCharsets.UTF_8, header);
    }

    /**
    * @Param : 获取页面
    * @description:
    * @Return:
    * @Author: lyd
    * @Date:2019/3/29
    * @time:17:53
    */
    public Page getPage(WebClient webClient, String url, HttpMethod httpMethod, List<NameValuePair> params, Map<String, String> header, String bodyStr) {
        return getPage(webClient, url, httpMethod, params, StandardCharsets.UTF_8, header, bodyStr);
    }

    /**
     * 获取页面
     * <b>有重复尝试机制</b>
     *
     * @param webClient  访问客户端
     * @param url        访问地址
     * @param httpMethod 提交方法
     * @param params     访问参数
     * @param retryTimes 重复尝试次数
     * @param logFlag    日志标志
     * @param header     访问Header
     * @return
     * @description
     * @author lyd
     * @create 2019/1/17 下午13:01
     */
    public Page getPage(WebClient webClient, String url, HttpMethod httpMethod, List<NameValuePair> params,
                        int retryTimes, String logFlag, Map<String, String> header) {
        int rt = 1;
        while (rt <= retryTimes) {
            try {
                //  getLogger().info(logFlag + ",第[{}]尝试请求", rt);
                Page page = getPage(webClient, url, httpMethod, params, StandardCharsets.UTF_8, header);
                if (null != page) {
                    return page;
                } else {
                    getLogger().info("[{}]响应内容为空,第[]次尝试请求,内容为空", rt);

                    rt++;
                    sleep(1000);
                }
            } catch (Exception e) {
                getLogger().info("[{}]获取响应出错了,第[]次尝试请求:[{}]", rt, e.getMessage());
                rt++;
                sleep(2000);

            }
        }
        return null;
    }

    /**
     * 获取页面
     * <b>有重复尝试机制</b>
     *
     * @param webClient  访问客户端
     * @param url        访问地址
     * @param httpMethod 提交方法
     * @param params     访问参数
     * @param retryTimes 重复尝试次数
     * @param logFlag    日志标志
     * @param header     访问Header
     * @param charset    请求编码
     * @return
     * @description
     * @author
     * @create 2019/1/17 下午13:01
     */
    public Page getPage(WebClient webClient, String url, HttpMethod httpMethod, List<NameValuePair> params,
                        int retryTimes, String logFlag, Map<String, String> header, Charset charset) {
        int rt = 1;
        while (rt <= retryTimes) {
            try {
                getLogger().info(logFlag + ",第[{}]尝试请求", rt);
                Page page = getPage(webClient, url, httpMethod, params, charset, header);
                if (null != page) {
                    return page;
                } else {
                    getLogger().info("[{}]响应内容为空,第[]次尝试请求,内容为空", rt);
                    rt++;
                }
            } catch (Exception e) {
                getLogger().info("[{}]获取响应出错了,第[]次尝试请求:[{}]", rt, e.getMessage());
                rt++;
                sleep(2000);
            }
        }
        return null;
    }

    /**
     * 获取页面
     * <b>有重复尝试机制</b>
     *
     * @param webClient  访问客户端
     * @param url        访问地址
     * @param httpMethod 提交方法
     * @param params     访问参数
     * @param retryTimes 重复尝试次数
     * @param logFlag    日志标志
     * @param header     访问Header
     * @param bodyStr    Body参数
     * @return
     * @description
     * @author
     * @create 2019/1/17 下午13:01
     */
    public Page getPage(WebClient webClient, String url, HttpMethod httpMethod, List<NameValuePair> params,
                        int retryTimes, String logFlag, Map<String, String> header, String bodyStr) {
        int rt = 1;
        while (rt <= retryTimes) {
            try {
                getLogger().info(logFlag + ",第[{}]尝试请求", rt);
                Page page = getPage(webClient, url, httpMethod, params, StandardCharsets.UTF_8, header, bodyStr);
                if (null != page) {
                    return page;
                } else {
                    getLogger().info("[{}]响应内容为空,第[]次尝试请求,内容为空", rt);
                    rt++;
                }
            } catch (Exception e) {
                getLogger().info("[{}]获取响应出错了,第[]次尝试请求:[{}]", rt, e.getMessage());
                rt++;
            }
        }
        return null;
    }

    /**
     * 获取页面
     *
     * @param webClient  访问客户端
     * @param url        访问地址
     * @param httpMethod 提交方法
     * @param params     访问参数
     * @param header     访问Header
     * @return
     * @description
     * @author
     * @create 2019/1/17 下午13:01
     */
    public Page getPage(WebClient webClient, String url, HttpMethod httpMethod, List<NameValuePair> params, Map<String, String> header, Charset charset) {
        return getPage(webClient, url, httpMethod, params, charset, header);
    }

    //--------------------------------------------------------
    //以下方法无须关注
    //--------------------------------------------------------

    private Page getPage(WebClient webClient, String url, HttpMethod httpMethod, List<NameValuePair> params,
                         Charset charset, Map<String, String> header) {
        if (null == webClient) {
            webClient = initWebClient(webClient);
        }
        if (httpMethod == HttpMethod.GET) {
            return doGet(webClient, url, params, charset, header, "");
        } else {
            return doPost(webClient, url, params, charset, header, "");
        }
    }

    private Page getPage(WebClient webClient, String url, HttpMethod httpMethod, List<NameValuePair> params,
                         Charset charset, Map<String, String> header, String bodyStr) {
        if (httpMethod == HttpMethod.GET) {
            return doGet(webClient, url, params, charset, header, bodyStr);
        } else {
            return doPost(webClient, url, params, charset, header, bodyStr);
        }
    }

    private Page doPost(WebClient webClient, String pageUrl, List<NameValuePair> reqParam,
                        Charset charset, Map<String, String> header, String bodyStr) {
        try {
            URL url = new URL(pageUrl);
            WebRequest webRequest = new WebRequest(url, HttpMethod.POST);
            webRequest.setAdditionalHeader("Accept-Language", "zh-CN");

            if (charset == null) {
                charset = StandardCharsets.UTF_8;
            }
            webRequest.setCharset(charset);
            if (reqParam != null) {
                webRequest.setRequestParameters(reqParam);
            }
            if (null != header) {
                for (String key : header.keySet()) {
                    webRequest.setAdditionalHeader(key, header.get(key));
                }
            }

            if (StringUtils.isNotBlank(bodyStr)) {
                webRequest.setRequestBody(bodyStr);
            }
            Page page = webClient.getPage(webRequest);
            return page;
        } catch (Exception ex) {
            getLogger().info("[{}]GET访问[{}]-[{}]出现异常:[{}]", pageUrl, JsonUtils.beanToJson(reqParam), ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    private Page doGet(WebClient webClient, String pageUrl, List<NameValuePair> reqParam,
                       Charset charset, Map<String, String> header, String bodyStr) {
        try {
            URL url;
            if (CollectionUtil.isEmpty(reqParam)) {
                url = new URL(pageUrl);
            } else {
                url = new URL(pageUrl + "?" + EntityUtils.toString((HttpEntity) reqParam));
            }

            WebRequest webRequest = new WebRequest(url, HttpMethod.GET);
            if (null != header) {
                for (String key : header.keySet()) {
                    webRequest.setAdditionalHeader(key, header.get(key));
                }
            }
            if (null == charset) {
                charset = StandardCharsets.UTF_8;
            }
            webRequest.setCharset(charset);

            if (StringUtils.isNotBlank(bodyStr)) {
                webRequest.setRequestBody(bodyStr);
            }
            Page page = webClient.getPage(webRequest);
            return page;
        } catch (Exception ex) {
            getLogger().info("[{}]POST访问[{}]-[{}]出现异常:[{}]", pageUrl, JsonUtils.beanToJson(reqParam), ex.getMessage());
            throw new RuntimeException(ex);
        }
    }


    /**
     * 创建默认的webclient
     * <b>如果需要特殊处理,需要重写些方法</b>
     *
     * @return
     */
    private WebClient initWebClient(WebClient webClient) {
        if (null == webClient) {
            webClient = new WebClient(BrowserVersion.FIREFOX_45);
            webClient.getCookieManager().clearCookies();
            webClient.getCache().clear();
        }
        webClient.setRefreshHandler(new ImmediateRefreshHandler());
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());
        webClient.getOptions().setRedirectEnabled(true);
        webClient.getOptions().setUseInsecureSSL(true);
        webClient.getCookieManager().setCookiesEnabled(true);
        webClient.getOptions().setActiveXNative(false);
        webClient.getOptions().setAppletEnabled(false);
        // 禁用css支持
        webClient.getOptions().setCssEnabled(false);
        // js运行错误时，是否抛出异常
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setJavaScriptEnabled(false);
        webClient.getOptions().setTimeout(60000);

        return webClient;
    }

    protected void sleep(int seconds) {
        try {
            Thread.sleep(seconds);
        } catch (Exception e) {
            getLogger().info("", e);
        }
    }

    /**
     * 获取爬取耗时时长
     *
     * @return
     */
    protected String getTimeConsuming(Date startTime) {
        Date endTime = new Date();
        long lt = endTime.getTime() - startTime.getTime();
        long day = lt / (24 * 60 * 60 * 1000);
        long hour = (lt / (60 * 60 * 1000) - day * 24);
        long min = ((lt / (60 * 1000)) - day * 24 * 60 - hour * 60);
        long s = (lt / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        String strTime = "消耗时间:" + hour + "时" + min + "分" + s + "秒";
        return strTime;
    }


}

package com.example.steam.crwal.service.serviceimpl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.steam.crwal.entity.CrawlState;
import com.example.steam.crwal.entity.KeyWord;
import com.example.steam.crwal.entity.Website;
import com.example.steam.crwal.link.Links;
import com.example.steam.crwal.service.CrawlService;
import com.example.steam.crwal.tools.StringUtils;
import com.example.steam.crwal.util.PageUtils;
import com.example.steam.parent.AccordComplaintParent;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

/**
 * @Author: mly
 * @Date: Created in 2019/6/10
 */
@Slf4j
public class CrawlServiceImpl extends AccordComplaintParent implements CrawlService {

    private Set<String> imgLinks;
    private String websiteName;
    private Document doc;

    @Override
    public CrawlState choiceHomePage(LinkedList target_website) throws Exception {
        for (Object url : target_website) {
            Website type = Website.getWebsiteByUrl(url.toString());
            switch (type) {
                case JIANSHU:
                    websiteName = type.toString();
                    CrawlState jianShu = getHomePageForJianShu(url.toString());
                    if (jianShu.getcode() == 1001) {
                        crawlByKeyWord(websiteName);
                        //Links.removeWebsiteLinks(url);
                        //遍历时对其删除会造成异常
                        //log.info("已将:" + url + "从队列中删除");
                        Links.addCompleteWebsiteLink(url);
                    }
                    break;
                case JIEMODUI:
                    websiteName = type.toString();
                    CrawlState jieMoDui = getHomePageForJieMoDui(url.toString());
                    if (jieMoDui.getcode() == 1001) {
                        crawlByKeyWord(websiteName);
                        //Links.removeWebsiteLinks(url);
                        //log.info("已将:" + url + "从队列中删除");
                        Links.addCompleteWebsiteLink(url);
                    }
                    break;
                case ZHIHU:
                    websiteName = type.toString();
                    CrawlState zhuhu = getHomePageForZhiHu(url.toString());
                    if (zhuhu.getcode() == 1001) {
                        crawlByKeyWord(websiteName);
                        Links.addCompleteWebsiteLink(url);
                    }
                    break;
                case BAIDU:
                    websiteName = type.toString();
                    CrawlState baidu = getHomePageForBaiDu(url.toString());
                    break;
                case UNCULTIVATED:
                    log.info("暂未开发!");
                    //Links.removeWebsiteLinks(url);
                    Links.addAccessingAbnormalWebSites(url.toString());
                    break;
                case INVALID:
                    log.info("不是有效的网址!");
                    // Links.removeWebsiteLinks(url);
                    Links.addAccessingAbnormalWebSites(url.toString());
                    break;
                default:
                    log.info("系统异常");
                    // Links.removeWebsiteLinks(url);
                    Links.addAccessingAbnormalWebSites(url.toString());
                    return CrawlState.ERROR;
            }
        }
        if (Links.getWebsiteLinksSize() == 0) {
            return CrawlState.SUCCESS;
        } else {
            return CrawlState.ERROR;
        }
    }

    private CrawlState getHomePageForBaiDu(String url) {
        HashMap<String, String> header = new HashMap();
        header.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3");
        header.put("Accept-Encoding", "gzip, deflate, br");
        header.put("Accept-Language", "zh-CN,zh;q=0.9");
        header.put("Cache-Control", "max-age=0");
        header.put("Connection", "keep-alive");
        header.put("Host", "www.baidu.com");
        header.put("Upgrade-Insecure-Requests", "1");
        header.put("User-Agent", "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Mobile Safari/537.36");
        Page page = getPage(webClient, url, HttpMethod.GET, null, header);
        if (PageUtils.getPageInfo(page) != null) {
            log.info("获取百度主页成功!");
            return CrawlState.SUCCESS;
        } else {
            log.info("获取主页失败");
            return CrawlState.ERROR;
        }
    }

    private CrawlState getHomePageForZhiHu(String url) {
        HashMap<String, String> header = new HashMap();
        header.put(":authority", "www.zhihu.com");
        header.put(":method", "GET");
        header.put(":path", "/");
        header.put(":scheme", "https");
        header.put("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3");
        //Canceling the following comment code will cause an error
        //header.put("accept-encoding", "gzip, deflate, br");
        header.put("accept-language", "zh-CN,zh;q=0.9");
        header.put("upgrade-insecure-requests", "1");
        header.put("cache-control", "max-age=0");
        header.put("user-agent", "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Mobile Safari/537.36");
        Page page = getPage(webClient, url, HttpMethod.GET, null, header);
        if (PageUtils.getPageInfo(page) != null) {
            log.info("获取知乎主页成功!");
            return CrawlState.SUCCESS;
        } else {
            log.info("获取主页失败");
            return CrawlState.ERROR;
        }
    }

    private CrawlState getHomePageForJieMoDui(String url) {
        HashMap<String, String> header = new HashMap();
        header.put(":authority", "www.jiemodui.com");
        header.put(":method", "GET");
        header.put(":path", "/");
        header.put(":scheme", "https");
        header.put("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3");
        header.put("accept-encoding", "gzip, deflate, br");
        header.put("accept-language", "zh-CN,zh;q=0.9");
        header.put("upgrade-insecure-requests", "1");
        header.put("user-agent", "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Mobile Safari/537.36");
        Page page = getPage(webClient, url, HttpMethod.GET, null, header);
        if (PageUtils.getPageInfo(page) != null) {
            log.info("获取芥末堆主页成功!");
            return CrawlState.SUCCESS;
        } else {
            log.info("获取主页失败");
            return CrawlState.ERROR;
        }
    }

    private CrawlState getHomePageForJianShu(String url) {
        HashMap<String, String> header = new HashMap();
        header.put(":authority", "www.jianshu.com");
        header.put(":method", "GET");
        header.put(":path", "/");
        header.put(":scheme", "https");
        header.put("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3");
        header.put("accept-encoding", "gzip, deflate, br");
        header.put("accept-language", "zh-CN,zh;q=0.9");
        header.put("if-none-match", "W/\"464543eaceaaabe9ef1c92b1bb7ba11c\"");
        header.put("upgrade-insecure-requests", "1");
        header.put("user-agent", "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Mobile Safari/537.36");
        Page page = getPage(webClient, url, HttpMethod.GET, null, header);
        if (PageUtils.getPageInfo(page) != null) {
            log.info("获取简书主页成功!");
            return CrawlState.SUCCESS;
        } else {
            log.info("获取主页失败");
            return CrawlState.ERROR;
        }
    }

    public void crawlByKeyWord(String websiteName) throws Exception {
        for (KeyWord k : KeyWord.values()) {
            String value = KeyWord.getValue(k);
            Website type = Website.getWebsiteByName(websiteName);
            log.info("获取关键字:" + value);
            switch (type) {
                case JIANSHU:
                    log.info("根据关键字:" + value + " 进行搜索");
                    searchByKeywordOnTheJianShu(value);
                    break;
                case JIEMODUI:
                    log.info("根据关键字:" + value + " 进行搜索");
                    searchByKeywordOnTheJieMoDui(value);
                    break;
                case ZHIHU:
                    log.info("根据关键字:" + value + " 进行搜索");
                    searchByKeyWordOnTheZhiHu(value);
                case UNCULTIVATED:
                    break;
                case ERROR:
                    break;
            }
        }
    }

    private void searchByKeyWordOnTheZhiHu(String value) throws Exception {
        for (int i = 1; i < 21; i++) {
            HashMap<String, String> header = new HashMap();
            String url = "https://www.zhihu.com/r/search?q=" + value + "&correction=1&type=content&offset=" + i * 10;
            header.put(":authority", "www.zhihu.com");
            header.put(":method", "GET");
            header.put(":path", "/r/search?q=" + value + "&correction=1&type=content&offset=10");
            header.put(":scheme", "https");
            header.put("accept", "*/*");
            //header.put("accept-encoding", "gzip, deflate, br");
            header.put("accept-language", "zh-CN,zh;q=0.9");
            header.put("referer", "https://www.zhihu.com/search?type=content&q=" + value);
            header.put("user-agent", "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Mobile Safari/537.36");
            header.put("x-requested-with", "XMLHttpRequest");
            try {
                Page page = getPage(webClient, url, HttpMethod.GET, null, header);
                if (!PageUtils.getPageInfo(page).contains("\"htmls\":[]")) {
                    log.info("获取搜索页面成功!");
                    analysisZhiHu(PageUtils.getPageInfo(page));
                    sleep(10000);
                }
            } catch (RuntimeException e) {
                log.info("频繁访问异常");
                e.printStackTrace();
                Links.addLinksNotVisited(url);
                sleep(10000);
            }
            sleep(10000);
        }

    }

    private void analysisZhiHu(String pageInfo) throws Exception {
        JSONObject jsonObject = JSONObject.parseObject(pageInfo);
        JSONArray jsonArray = jsonObject.getJSONArray("htmls");
        for (int i = 0; i < jsonArray.size(); i++) {
            Document document = Jsoup.parse(jsonArray.getString(i));
            String aLink = document.select("a.js-title-link").attr("href");
            if (!Links.fetchLinksVisited().contains(aLink)) {
                Links.addLinksVisited(aLink);
                String title = document.select("a.js-title-link").text();
                String content = Jsoup.parse(document.select("script").html()).text();
                if (content != null) {
                    String result = createFile(title, content);
                    log.info("文件保存成功,添加到Set中");
                    Links.addAccessCompleteLinks(aLink);
                }
            }


        }
    }

    private void searchByKeywordOnTheJieMoDui(String value) throws Exception {
        HashMap<String, String> header = new HashMap();
        String url = "https://www.jiemodui.com/SearchV2/N/" + value;
        header.clear();
        header.put(":authority", "www.jiemodui.com");
        header.put(":method", "GET");
        header.put(":path", "/SearchV2/N/" + value);
        header.put(":scheme", "https");
        header.put("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3");
        header.put("accept-encoding", "gzip, deflate, br");
        header.put("accept-language", "zh-CN,zh;q=0.9");
        header.put("referer", "https://www.jiemodui.com/searchV2.html");
        header.put("upgrade-insecure-requests", "1");
        header.put("user-agent", "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Mobile Safari/537.36");
        try {
            Page page = getPage(webClient, url, HttpMethod.GET, null, header);
            if (PageUtils.getPageInfo(page) != null) {
                log.info("获取搜索页面成功!");
                getSearchInfo(value);
            }
        } catch (RuntimeException e) {
            log.info("频繁访问异常");
            Links.addLinksNotVisited(url);
        }
    }

    private void getSearchInfo(String value) throws Exception {
        for (int i = 1; i < 10; i++) {
            HashMap<String, String> header = new HashMap();
            String url = "https://www.jiemodui.com/Api/Index/search?p=" + i + "&type=N&keyword=" + value + "&block=true&highlight=1";
            header.clear();
            header.put(":authority", "www.jiemodui.com");
            header.put(":method", "GET");
            header.put(":path", "/Api/Index/search?p=2&type=N&keyword=steam%E6%95%99%E8%82%B2&block=true&highlight=1");
            header.put(":scheme", "https");
            header.put("accept", "application/json, text/javascript, */*; q=0.01");
            header.put("accept-encoding", "gzip, deflate, br");
            header.put("accept-language", "zh-CN,zh;q=0.9");
            header.put("referer", "https://www.jiemodui.com/SearchV2/N/steam%E6%95%99%E8%82%B2");
            header.put("user-agent", "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Mobile Safari/537.36");
            header.put("x-requested-with", "XMLHttpRequest");
            Page page = getPage(webClient, url, HttpMethod.GET, null, header);
            JSONObject jsonObject = JSONObject.parseObject(PageUtils.getPageInfo(page));
            JSONArray array = jsonObject.getJSONArray("list");
            if (array.size() != 0) {
                log.info("查找信息完成,并获取json成功");
                analysisJieMoDui(array);
            } else {
                log.info("没有搜索到相关的信息");
                break;
            }
            sleep(10000);
        }
    }

    private void analysisJieMoDui(JSONArray array) throws Exception {
        for (int i = 0; i < array.size(); i++) {
            JSONObject jsonObject = array.getJSONObject(i);
            String title = jsonObject.getString("brief");
            String content = jsonObject.getString("contentbak");
            String pick = jsonObject.getString("picture");
            String id = jsonObject.getString("id");
            Links.addLinksNotVisited("https://www.jiemodui.com/N/" + id);
            log.info("将链接添加到Set中避免重复");
            if (pick != null || pick != "") {
                imgLinks = new HashSet<>();
                imgLinks.add(pick);
            }
            log.info("解析成功");
            String result = createFile(title, content);
            if (result.equals("success")) {
                Links.addAccessCompleteLinks("https://www.jiemodui.com/N/" + id);
                log.info("爬取成功链接并添加到Set中");
            }

        }
    }

    private void searchByKeywordOnTheJianShu(String value) {
        for (int i = 1; i < 30; i++) {
            List<NameValuePair> params = new ArrayList<>();
            HashMap<String, String> header = new HashMap();
            String url = "https://www.jianshu.com/search/do?q=" + value + "&type=note&page=" + i + "&order_by=default";
            header.clear();
            header.put(":authority", "www.jianshu.com");
            header.put(":method", "POST");
            header.put(":path", "/search/do?q=Steam%E6%95%99%E8%82%B2&type=note&page=" + i + "&order_by=default");
            header.put(":scheme", "https");
            header.put("accept", "application/json");
            header.put("accept-encoding", "gzip, deflate, br");
            header.put("accept-language", "zh-CN,zh;q=0.9");
            header.put("origin", "https://www.jianshu.com");
            header.put("referer", "https://www.jianshu.com/");
            header.put("user-agent", "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Mobile Safari/537.36");
            params.add(new NameValuePair("q", value));//此处有多个关键词....
            params.add(new NameValuePair("type", "note"));
            params.add(new NameValuePair("page", String.valueOf(i)));
            params.add(new NameValuePair("order_by", "default"));
            log.info("根据关键字:" + value + " 爬取相关信息   第(" + i + ")页");
            try {
                Page page = getPage(webClient, url, HttpMethod.POST, params, header);
                String pageinfo = PageUtils.getPageInfo(page);
                log.info("开始解析爬取内容");
                analysisJianShu(pageinfo, url);
                sleep(10000);
            } catch (RuntimeException e) {
                log.info("java.lang.RuntimeException: 429 Too Many Requests 请求次数过多");
                log.info("访问频繁,请稍后再试:" + url);
                Links.addLinksNotVisited(url);
                sleep(10000);
            } catch (Exception e) {
                log.info("爬取异常");
                Links.addLinksNotVisited(url);
                e.printStackTrace();
                sleep(1000);
            }
            sleep(6000);
        }
    }

    private void analysisJianShu(String pageinfo, String url) throws Exception {
        if (pageinfo != null) {
            JSONObject jsonObject = JSONObject.parseObject(pageinfo);
            JSONArray jsonArray = jsonObject.getJSONArray("entries");
            for (int i = 0; i < jsonArray.size(); i++) {
                jsonObject = (JSONObject) jsonArray.get(i);
                String slug = jsonObject.getString("slug");
                log.info("获取单个页面slug,并根据slug跳转到详情页面");
                jumpPage(slug);
            }
        }
    }

    private void jumpPage(String slug) throws Exception {
        String url = "https://www.jianshu.com/p/" + slug;
        Links.addLinksVisited(url);
        log.info("将url添加到Set集合中,避免爬取重复链接");
        HashMap<String, String> header = new HashMap();
        header.put(":authority", "www.jianshu.com");
        header.put(":method", "GET");
        header.put(":path", "/p/" + slug);
        header.put(":scheme", "https");
        header.put("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3");
        header.put("accept-encoding", "gzip, deflate, br");
        header.put("if-none-match", "W/\"7af60f8a0e04df6571b54d5b6bc10ceb\"");
        header.put("referer", "https://www.jianshu.com");
        header.put("upgrade-insecure-requests", "1");
        header.put("user-agent", "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Mobile Safari/537.36");
        Page page = getPage(webClient, url, HttpMethod.GET, null, header);
        String pageinfo = PageUtils.getPageInfo(page);

        doc = Jsoup.parse(pageinfo);
        String title = doc.select("h1.title").text();
        String context = doc.select("div.note-content").text();
        doc = Jsoup.parse(doc.select("div.show-content-free").toString());
        Elements elements = doc.getElementsByTag("img");
        imgLinks = new HashSet<>();
        for (Element ele : elements) {
            imgLinks.add("https:" + ele.attr("data-original-src"));
        }
        log.info("成功跳转到详情页面 并解析相关信息");
        String result = createFile(title, context);
        if (result.equals("success")) {
            log.info("文件保存成功,添加到Set中");
            Links.addAccessCompleteLinks(url);
        }
        sleep(10000);
    }

    private String createFile(String title, String content) throws Exception {
        String path = "D:\\" + websiteName + "\\" + StringUtils.StringFilter(title) + ".txt";
        String imgsPath = "D:\\" + websiteName + "\\";
        File folder = new File(path);
        File fileParent = folder.getParentFile();
        if (!fileParent.exists()) {
            fileParent.mkdir();
        }
        FileWriter fileWriter = new FileWriter(path);
        fileWriter.write(content);
        fileWriter.flush();
        fileWriter.close();
        folder.createNewFile();
        log.info("成功将爬取内容保存至" + path);
        int result = downloadPicture(imgsPath);
        if (result == 1) {
            return "success";
        }
        return null;
    }

    private int downloadPicture(String imgsPath) {
        if (imgLinks != null) {
            for (String urlStr : imgLinks) {
                URL url = null;
                try {
                    url = new URL(urlStr);
                    String fileFile = imgsPath + urlStr.substring(urlStr.lastIndexOf("/") + 1);
                    String regex = ".*\\.(png||jpg||gif||bmp)";
                    if (fileFile.matches(regex)) {
                        DataInputStream dataInputStream = new DataInputStream(url.openStream());
                        FileOutputStream fileOutputStream = new FileOutputStream(new File(fileFile));
                        ByteArrayOutputStream output = new ByteArrayOutputStream();
                        byte[] buffer = new byte[1024];
                        int length;
                        while ((length = dataInputStream.read(buffer)) > 0) {
                            output.write(buffer, 0, length);
                        }
                        fileOutputStream.write(output.toByteArray());
                        dataInputStream.close();
                        fileOutputStream.close();
                        log.info("成功将图片保存至:" + imgsPath);
                        return 1;
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return 0;
    }
}
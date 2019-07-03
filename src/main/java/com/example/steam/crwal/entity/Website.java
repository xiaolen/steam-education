package com.example.steam.crwal.entity;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author: mly
 * @Date: Created in 2019/6/10
 */
public enum Website {
    JIANSHU("jianshu", "简书网"),
    JIEMODUI("jiemodui","芥末堆"),
    ZHIHU("zhihu","知乎"),
    BAIDU("baidu","百度"),
    INVALID("invalid","无效网址"),
    UNCULTIVATED("uncultivated","暂未开发"),
    ERROR("error", "暂时无法爬取");
    private String link;
    private String website;

    Website(String link, String website) {
        this.link = link;
        this.website = website;
    }

    public static Website getWebsiteByUrl(String url) {
        for (Website web : Website.values()) {
            if (isTopURL(url)){
                if (url.toLowerCase().contains(web.toString().toUpperCase(Locale.ENGLISH).toLowerCase())) {
                    return web;
                }
            }else {
                return INVALID;
            }
        }
        return UNCULTIVATED;
    }
    public static Website getWebsiteByName(String websiteName) {
        for (Website website :Website.values()){
            if(websiteName.toLowerCase().equals(website.getLink())){
                return website;
            }
        }
        return UNCULTIVATED;
    }

    public static boolean isTopURL(String str) {
        str = str.toLowerCase();
        String domainRules = "com.cn|net.cn|org.cn|gov.cn|com.hk|公司|中国|网络|com|net|org|int|edu|gov|mil|arpa|Asia|biz|info|name|pro|coop|aero|museum|ac|ad|ae|af|ag|ai|al|am|an|ao|aq|ar|as|at|au|aw|az|ba|bb|bd|be|bf|bg|bh|bi|bj|bm|bn|bo|br|bs|bt|bv|bw|by|bz|ca|cc|cf|cg|ch|ci|ck|cl|cm|cn|co|cq|cr|cu|cv|cx|cy|cz|de|dj|dk|dm|do|dz|ec|ee|eg|eh|es|et|ev|fi|fj|fk|fm|fo|fr|ga|gb|gd|ge|gf|gh|gi|gl|gm|gn|gp|gr|gt|gu|gw|gy|hk|hm|hn|hr|ht|hu|id|ie|il|in|io|iq|ir|is|it|jm|jo|jp|ke|kg|kh|ki|km|kn|kp|kr|kw|ky|kz|la|lb|lc|li|lk|lr|ls|lt|lu|lv|ly|ma|mc|md|me|mg|mh|ml|mm|mn|mo|mp|mq|mr|ms|mt|mv|mw|mx|my|mz|na|nc|ne|nf|ng|ni|nl|no|np|nr|nt|nu|nz|om|pa|pe|pf|pg|ph|pk|pl|pm|pn|pr|pt|pw|py|qa|re|ro|ru|rw|sa|sb|sc|sd|se|sg|sh|si|sj|sk|sl|sm|sn|so|sr|st|su|sy|sz|tc|td|tf|tg|th|tj|tk|tm|tn|to|tp|tr|tt|tv|tw|tz|ua|ug|uk|us|uy|va|vc|ve|vg|vn|vu|wf|ws|ye|yu|za|zm|zr|zw";
        String regex = "^((https|http|ftp|rtsp|mms)?://)"
                + "?(([0-9a-z_!~*'().&=+$%-]+: )?[0-9a-z_!~*'().&=+$%-]+@)?" //ftp的user@
                + "(([0-9]{1,3}\\.){3}[0-9]{1,3}" // IP形式的URL- 199.194.52.184
                + "|" // 允许IP和DOMAIN（域名）
                + "(([0-9a-z][0-9a-z-]{0,61})?[0-9a-z]+\\.)?" // 域名- www.
                + "([0-9a-z][0-9a-z-]{0,61})?[0-9a-z]\\." // 二级域名
                + "(" + domainRules + "))" // first level domain- .com or .museum
                + "(:[0-9]{1,4})?" // 端口- :80
                + "((/?)|" // a slash isn't required if there is no file name
                + "(/[0-9a-z_!~*'().;?:@&=+$,%#-]+)+/?)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher isUrl = pattern.matcher(str);
        return isUrl.matches();
    }

    public String getLink() {
        return link;
    }

    public String getWebsite() {
        return website;
    }


}

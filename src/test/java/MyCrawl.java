import com.example.steam.crwal.entity.CrawlState;
import com.example.steam.crwal.link.Links;
import com.example.steam.crwal.service.CrawlService;
import com.example.steam.crwal.service.serviceimpl.CrawlServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import javax.annotation.Resource;

/**
 * @Author: mly
 * @Date: Created in 2019/6/10
 */

@Slf4j
public class MyCrawl {

    @Resource
    private CrawlService crawlService = new CrawlServiceImpl();

    @Test
    public void crawl() throws Exception {
        Links.addWebsiteLinks("1");
        Links.addWebsiteLinks("https://www.jianshu.com/");
        Links.addWebsiteLinks("https://www.jiemodui.com/");
        Links.addWebsiteLinks("https://www.zhihu.com/");
        Links.addWebsiteLinks("https://www.baidu.com/");
        System.out.println(Links.getWebsiteLinksSize());

        CrawlState result = crawlService.choiceHomePage(Links.getWebsiteLinks());
        if (result.equals(CrawlState.SUCCESS)) {
        } else {
            log.info("爬取数据失败!");
        }
        System.out.println("访问异常的网站有:" + Links.accessingAbonrmalWebSitesSize());
        System.out.println("访问完成的网站有:" + Links.getCompleteWebsiteLinkSize());
        System.out.println("访问异常的网页有:" + Links.linksNotVisitedSize());
        System.out.println("访问完成的链接有:" + Links.accessCompleteLinksSize() + "条");
    }
}



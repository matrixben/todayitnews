package jason.luo.service;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledCrawler {
    @Autowired
    private NewsService newsService;

    /**
     * scheduled间隔时间不能小于一分钟因为crawler4j在任务未结束前再启动会无法启动
     * @throws Exception
     */
    @Scheduled(initialDelay = 50000, fixedRate = 100000)
    public void solidotCrawler() throws Exception {
        int numberOfCrawlers = 1;
        CrawlController controller = initCrawlController();
        controller.addSeed("https://www.solidot.org");

        controller.startNonBlocking(new SolidotCrawlerFactory(newsService), numberOfCrawlers);
    }

    @Scheduled(initialDelay = 300000, fixedRate = 600000)
    public void huxiuCrawler() throws Exception {
        int numberOfCrawlers = 1;
        CrawlController controller = initCrawlController();
        controller.addSeed("https://www.huxiu.com");

        controller.startNonBlocking(new HuxiuCrawlerFactory(newsService), numberOfCrawlers);
    }

    @Scheduled(initialDelay = 600000, fixedRate = 600000)
    public void ifanrCrawler() throws Exception {
        int numberOfCrawlers = 1;
        CrawlController controller = initCrawlController();
        controller.addSeed("https://www.ifanr.com");

        controller.startNonBlocking(new IfanrCrawlerFactory(newsService), numberOfCrawlers);
    }

    private CrawlController initCrawlController() throws Exception {
        String crawlStorageFolder = ".";
        CrawlConfig config = new CrawlConfig();
        config.setCrawlStorageFolder(crawlStorageFolder);
        config.setPolitenessDelay(1000);
        config.setMaxDepthOfCrawling(0);
        config.setMaxPagesToFetch(20);

        PageFetcher pageFetcher = new PageFetcher(config);
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        robotstxtConfig.setEnabled(false);
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);

        return new CrawlController(config, pageFetcher, robotstxtServer);
    }
}

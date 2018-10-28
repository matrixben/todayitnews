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
    @Scheduled(initialDelay = 5000, fixedRate = 60000)
    public void solidotCrawler() throws Exception {
        String crawlStorageFolder = ".";
        int numberOfCrawlers = 1;
        CrawlConfig config = new CrawlConfig();
        config.setCrawlStorageFolder(crawlStorageFolder);
        config.setPolitenessDelay(1000);
        config.setMaxDepthOfCrawling(0);
        config.setMaxPagesToFetch(20);

        PageFetcher pageFetcher = new PageFetcher(config);
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        robotstxtConfig.setEnabled(false);
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
        CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);

        controller.addSeed("https://www.solidot.org");

        controller.startNonBlocking(new SolidotCrawlerFactory(newsService), numberOfCrawlers);
    }
}

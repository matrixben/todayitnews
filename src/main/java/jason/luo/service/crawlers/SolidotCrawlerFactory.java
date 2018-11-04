package jason.luo.service.crawlers;

import edu.uci.ics.crawler4j.crawler.CrawlController;
import jason.luo.service.NewsService;

public class SolidotCrawlerFactory implements CrawlController.WebCrawlerFactory<SolidotCrawler> {
    private NewsService newsService;

    public SolidotCrawlerFactory(NewsService newsService) {
        this.newsService = newsService;
    }

    @Override
    public SolidotCrawler newInstance() {
        return new SolidotCrawler(this.newsService);
    }
}

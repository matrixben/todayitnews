package jason.luo.service;

import edu.uci.ics.crawler4j.crawler.CrawlController;

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

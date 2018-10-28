package jason.luo.service;

import edu.uci.ics.crawler4j.crawler.CrawlController;

public class IfanrCrawlerFactory implements CrawlController.WebCrawlerFactory<IfanrCrawler> {
    private NewsService newsService;

    public IfanrCrawlerFactory(NewsService newsService) {
        this.newsService = newsService;
    }

    @Override
    public IfanrCrawler newInstance() {
        return new IfanrCrawler(this.newsService);
    }
}

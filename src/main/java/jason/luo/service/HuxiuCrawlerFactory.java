package jason.luo.service;

import edu.uci.ics.crawler4j.crawler.CrawlController;

public class HuxiuCrawlerFactory implements CrawlController.WebCrawlerFactory<HuxiuCrawler> {
    private NewsService newsService;

    public HuxiuCrawlerFactory(NewsService newsService) {
        this.newsService = newsService;
    }

    @Override
    public HuxiuCrawler newInstance() {
        return new HuxiuCrawler(this.newsService);
    }
}

package jason.luo.service;

import edu.uci.ics.crawler4j.crawler.CrawlController;

public class H2CrawlerFactory implements CrawlController.WebCrawlerFactory<MyCrawler> {
    private NewsService newsService;

    public H2CrawlerFactory(NewsService newsService) {
        this.newsService = newsService;
    }

    @Override
    public MyCrawler newInstance() throws Exception {
        return new MyCrawler(this.newsService);
    }
}

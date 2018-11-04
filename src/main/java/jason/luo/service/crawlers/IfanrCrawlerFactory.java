package jason.luo.service.crawlers;

import edu.uci.ics.crawler4j.crawler.CrawlController;
import jason.luo.service.NewsService;

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

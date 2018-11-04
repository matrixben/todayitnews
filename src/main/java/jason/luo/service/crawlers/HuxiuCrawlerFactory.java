package jason.luo.service.crawlers;

import edu.uci.ics.crawler4j.crawler.CrawlController;
import jason.luo.service.NewsService;

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

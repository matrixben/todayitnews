package jason.luo.web;

import jason.luo.domain.News;
import jason.luo.service.H2CrawlerFactory;
import jason.luo.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

import java.util.ArrayList;
import java.util.List;

@RestController
public class HelloController {
    @Autowired
    private NewsService newsService;

    @RequestMapping("/hi")
    public String hello(){
        return "This project is going to gather all IT news' title and url " +
                "to one page for me to view.";
    }

    @RequestMapping("/findAll")
    public List<News> findAllNews(){
        List<News> newsList = new ArrayList<>();
        Iterable<News> iter = newsService.findAll();
        iter.forEach(i -> {newsList.add(i);});
        return newsList;
    }

    @RequestMapping("/findByTitle")
    public News findNewsByTitle(@RequestParam(value = "title", defaultValue = "") String title){
        return newsService.findByTitle(title);
    }
    
    @RequestMapping("/solidot")
	public void getSolidotNews() throws Exception {
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
        
        controller.startNonBlocking(new H2CrawlerFactory(newsService), numberOfCrawlers);
	}
}

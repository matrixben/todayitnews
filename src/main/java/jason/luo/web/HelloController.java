package jason.luo.web;

import jason.luo.domain.Greeting;
import jason.luo.domain.HelloMessage;
import jason.luo.domain.News;
import jason.luo.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import org.springframework.web.util.HtmlUtils;

import java.util.ArrayList;
import java.util.List;

@RestController
public class HelloController {
    @Autowired
    private NewsService newsService;

    @RequestMapping("/index")
    public String hello(){
        return "This project is going to gather all IT news' title and url " +
                "to one page for me to view.";
    }

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public Greeting greeting(HelloMessage message) throws InterruptedException {
        Thread.sleep(1000);
        return new Greeting("Hello " + HtmlUtils.htmlEscape(message.getName()) + " !");
    }

    @RequestMapping("/count")
    public long newsCount(){
        return newsService.count();
    }

    @RequestMapping("/findAll")
    public List<News> findAllNews(){
        List<News> newsList = new ArrayList<>();
        Iterable<News> iter = newsService.findAll();
        iter.forEach(i -> {newsList.add(i);});
        return newsList;
    }

    @RequestMapping("/tennews")
    public List<News> findLatestTenNews(){
        List<News> newsList = new ArrayList<>();
        Iterable<News> iter = newsService.findLatestTenNews();
        iter.forEach(i -> {newsList.add(i);});
        return newsList;
    }

    @RequestMapping("/news")
    public List<News> findNews(@RequestParam(value = "title", defaultValue = "") String title,
                               @RequestParam(value = "tag", defaultValue = "") String tag) {
        if ("".equals(title) && "".equals(tag)){
            return findAllNews();
        }
        return newsService.findNews(title, tag);
    }

    @RequestMapping("/delete")
    public String deleteNewsByTitle(@RequestParam(value = "title", defaultValue = "") String title){
        long l = newsService.deleteNewsByTitle(title);
        return String.valueOf(l) + " news: " + title + " is deleted.";
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
        
        controller.startNonBlocking(new SolidotCrawlerFactory(newsService), numberOfCrawlers);
	}

	@RequestMapping("/load")
    public void crawlDotComNews(@RequestParam(value = "host", defaultValue = "") String host) throws Exception {
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

        controller.addSeed("https://www."+ host +".com");
        if ("ifanr".equals(host)){
            controller.startNonBlocking(new IfanrCrawlerFactory(newsService), numberOfCrawlers);
        }else if ("huxiu".equals(host)){
            controller.startNonBlocking(new HuxiuCrawlerFactory(newsService), numberOfCrawlers);
        }
    }
}

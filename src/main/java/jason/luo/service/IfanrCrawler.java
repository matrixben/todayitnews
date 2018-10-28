package jason.luo.service;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import jason.luo.domain.News;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Calendar;
import java.util.Date;

public class IfanrCrawler extends WebCrawler {
    private NewsService newsService;

    public IfanrCrawler(NewsService newsService){
        this.newsService = newsService;
    }

    public boolean shouldVisit(Page refPage, WebURL url) {
        String urlStr = url.getURL().toLowerCase();
        return true;
    }

    public void visit(Page page) {
        Date today = Calendar.getInstance().getTime();
        String url = page.getWebURL().getURL();

        if (page.getParseData() instanceof HtmlParseData) {
            HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
            String html = htmlParseData.getHtml();
            Document doc = Jsoup.parse(html);
            //文章列表
            Elements articles = doc.getElementsByClass("article-item");
            for (Element article : articles) {
                String title = article.select("h3").text();
                String fullUrlStr = article.select("h3 > a").attr("href");
                String tag = article.getElementsByClass("article-image").get(0).text();

                // saveInfo(title, tag, today, fullUrlStr);
                System.out.println(title+" , "+tag+" , "+fullUrlStr);
            }


        }
    }

    private void saveInfo(String title, String tag, Date publishDate, String url){
        News news = new News();
        news.setTitle(title);
        news.setTag(tag);
        news.setPublishDate(publishDate);
        news.setSourceUrl(url);

        if (!isNewsExist(news)) {
            newsService.save(news);
        }else {
            System.out.print(news.getTitle().substring(0, 6) + "... is already in.");
        }
    }

    private boolean isNewsExist(News news) {
        News n = newsService.findNews(news.getTitle(), "").get(0);
        return (n != null && n.getTitle().equals(news.getTitle()));
    }
}

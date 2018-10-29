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
import java.util.List;
import java.util.TimeZone;

public class HuxiuCrawler extends WebCrawler {
    private NewsService newsService;

    public HuxiuCrawler(NewsService newsService){
        this.newsService = newsService;
    }

    public boolean shouldVisit(Page refPage, WebURL url) {
        String urlStr = url.getURL().toLowerCase();
        return true;
    }

    public void visit(Page page) {
        int timeOffset = TimeZone.getTimeZone("Asia/Shanghai").getRawOffset()
                - TimeZone.getTimeZone("GMT").getRawOffset();
        String url = page.getWebURL().getURL();

        if (page.getParseData() instanceof HtmlParseData) {
            HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
            String html = htmlParseData.getHtml();
            Document doc = Jsoup.parse(html);
            //最热新闻
//            Element hotest = doc.getElementsByClass("big-pic").first();
//            String title = hotest.select("h1").text();
//            String tag
//            String subUrl = hotest.select("a").attr("href");
//            String fullUrlStr = url + subUrl.substring(1);
//            System.out.println(title+" , "+tag+" , "+fullUrlStr);
            //文章列表
            Elements articles = doc.getElementsByClass("mod-art");
            for (int i = articles.size()-1; i >= 0; i--) {
                String title = articles.get(i).select("h2").text();
                String subUrl = articles.get(i).select("h2 > a").attr("href");
                Elements tags = articles.get(i).getElementsByClass("column-link-box");
                String tag = "虎嗅";  //tag可能为空或有多个
                if (!"".equals(tags.text().trim())){
                    tag = tags.get(0).child(0).text();
                }
                String fullUrlStr = url + subUrl.substring(1);

                Date today = new Date(new Date().getTime() + timeOffset);
                saveInfo(title, tag, today, fullUrlStr);
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
        List<News> l = newsService.findNews(news.getTitle(), "");

        return (!l.isEmpty() && l.get(0) != null && l.get(0).getTitle().equals(news.getTitle()));
    }
}

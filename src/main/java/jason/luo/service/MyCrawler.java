package jason.luo.service;

import java.util.LinkedHashMap;
import java.util.Map;

import jason.luo.dao.NewsDaoImpl;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import org.springframework.beans.factory.annotation.Autowired;

public class MyCrawler extends WebCrawler {
    @Autowired
    private NewsDaoImpl newsDao;

    public boolean shouldVisit(Page refPage, WebURL url) {
        String urlStr = url.getURL().toLowerCase();
        return true;
    }

    public void visit(Page page) {
        String url = page.getWebURL().getURL();
        System.out.println("URL: " + url);

        if (page.getParseData() instanceof HtmlParseData) {
            HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
            String html = htmlParseData.getHtml();
            Document doc = Jsoup.parse(html);
            Elements articles = doc.getElementsByClass("block_m");
            Map<String, String> titlesMap = new LinkedHashMap<>();
            for (Element article : articles) {
                String title = article.select("h2").text();
                String subUrl = article.select("h2 > a").attr("href");
                String tag = article.getElementsByClass("icon_float").get(0).child(0).attr("title");
                String fullUrlStr = url + subUrl.substring(1);
                titlesMap.put(title + " tag: " + tag, fullUrlStr);
            }

            for (String t : titlesMap.keySet()) {
                System.out.println(t + " : " + titlesMap.get(t));
            }
//			newsDao.saveNews();
        }
    }
}

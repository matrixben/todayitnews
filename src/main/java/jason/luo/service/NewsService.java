package jason.luo.service;

import jason.luo.dao.NewsDao;
import jason.luo.domain.News;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class NewsService {
    @Resource
    private NewsDao newsDao;

    public Iterable<News> findAll(){
        return newsDao.findAll();
    }

    public News save(News news){
        return newsDao.save(news);
    }

    public List<News> findNews(String title, String tag){
        if (!"".equals(title) && !"".equals(tag)){
            return newsDao.findNewsByTitleLikeAndTagOrderByPublishDateDesc("%"+title+"%", tag);
        }
        return newsDao.findNewsByTitleLikeOrTagOrderByPublishDateDesc("%"+title+"%", tag);
    }

    public long count() {
        return newsDao.count();
    }

    public long deleteNewsByTitle(String title) {
        return newsDao.removeByTitle(title);
    }
}

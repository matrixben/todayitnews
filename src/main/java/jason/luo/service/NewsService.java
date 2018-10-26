package jason.luo.service;

import jason.luo.dao.NewsDao;
import jason.luo.domain.News;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

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

    public News findByTitle(String title) {
        return newsDao.findByTitle(title);
    }
}

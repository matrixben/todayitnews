package jason.luo.dao;

import jason.luo.domain.News;
import org.springframework.data.repository.CrudRepository;

public interface NewsDao extends CrudRepository<News, Integer>{
    News findByTitle(String title);
}

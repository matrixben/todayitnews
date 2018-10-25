package jason.luo.dao;

import jason.luo.domain.News;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class NewsDaoImpl {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final static String SAVE_NEWS_SQL =
            "insert into news (title,tag,publish_date,source_url) values (?,?,?,?)";
    private final static String FIND_LATEST_NEWS_SQL =
            "select title,tag,sub_title,publish_date,source_url from news order by news_id desc limit ?";

    public void saveNews(News news){
        jdbcTemplate.update(SAVE_NEWS_SQL,
                news.getTitle(), news.getTag(), news.getPublishDate(), news.getSourceUrl());
    }

    public News getLatestNews(int newsNum){
        final News news = new News();
        jdbcTemplate.query(FIND_LATEST_NEWS_SQL, new Object[]{newsNum},
                new RowCallbackHandler() {
                    @Override
                    public void processRow(ResultSet rs) throws SQLException {
                        news.setTitle(rs.getString("title"));
                        news.setTag(rs.getString("tag"));
                        news.setPublishDate(rs.getDate("publish_date"));
                        news.setSourceUrl(rs.getString("source_url"));
                    }
                });
        return news;
    }
}

package com.ex.dao;

import com.ex.model.News;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by xc on 2017/2/24.
 */
@Mapper
public interface NewsDAO {
    String TABLE_NAME = "news";
    String INSERT_FIELDS = " title, link, image, like_count, comment_count, created_date, user_id ";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS,
            ") values (#{title},#{link},#{image},#{likeCount},#{commentCount},#{createdDate},#{userId})"})
    int addNews(News news);

    @Select({"select * from ", TABLE_NAME, " where user_id=#{userId}"})
    News getOneNewsFromUserId(@Param("userId") int userId);

    /*@Select({"select * from ", TABLE_NAME, " limit 10"})
    News getAllNews();*/
    List<News> getAllNews();

    List<News> selectByUserIdAndOffset(@Param("userId") int userId, @Param("offset") int offset, @Param("limit") int limit);

    @Select({"select * from ", TABLE_NAME, " where id=#{id}"})
    News getById(@Param("id") int id);

    @Update({"update ", TABLE_NAME, " set comment_count=#{commentCount} where id=#{id}"})
    int updateCommentCount(@Param("id") int id, @Param("commentCount") int commentCount);

    @Update({"update ", TABLE_NAME, " set like_count=#{likeCount} where id=#{id}"})
    int updateLikeCount(@Param(("id"))int id,@Param("likeCount") int likeCount);

}
